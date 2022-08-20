package com.money.security.component;

import cn.hutool.core.util.IdUtil;
import com.money.common.util.JwtPayload;
import com.money.common.util.JwtUtil;
import com.money.security.config.TokenConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 安全令牌支持
 * @createTime : 2022-01-06 22:19:19
 */
@Component
@Getter
@Slf4j
public class SecurityTokenSupport {

    private final JwtUtil jwtUtil;
    private final TokenConfig tokenConfig;
    private final TokenStrategy tokenStrategy;

    @Autowired
    public SecurityTokenSupport(TokenConfig tokenConfig, TokenStrategy tokenStrategy, @Value("${money.security.token.strategy}") String strategy) {
        this.tokenConfig = tokenConfig;
        this.jwtUtil = new JwtUtil(tokenConfig.getSecret());
        // 获取指定Token策略
        this.tokenStrategy = tokenStrategy;
        log.info("Token认证策略为 {}，TokenStrategy = {}", strategy, tokenStrategy);
    }

    /**
     * 访问令牌的缓存键
     *
     * @param username 用户名
     * @return {@link String}
     */
    private String accessTokenCacheKey(String username) {
        return tokenConfig.getCacheKey() + username + ":string";
    }

    /**
     * 刷新令牌缓存键
     *
     * @param username 用户名
     * @return {@link String}
     */
    private String refreshTokenCacheKey(String username) {
        return tokenConfig.getCacheKey() + ":refresh:" + username + ":string";
    }

    /**
     * 生成令牌
     *
     * @param username 用户名
     * @return {@link String}
     */
    public String generateToken(String username) {
        String tokenId = IdUtil.objectId();
        JwtPayload jwtPayload = JwtPayload.builder(tokenConfig.getTtl())
                // tokenId
                .id(tokenId)
                // username
                .audience(username);
        tokenStrategy.saveToken(accessTokenCacheKey(username), tokenId, tokenConfig.getTtl(), TimeUnit.MILLISECONDS);
        return jwtUtil.generateJwt(jwtPayload);
    }

    /**
     * 生成刷新令牌
     *
     * @param username 用户名
     * @return {@link String}
     */
    public String generateRefreshToken(String username) {
        String tokenId = IdUtil.objectId();
        JwtPayload jwtPayload = JwtPayload.builder(tokenConfig.getRefreshTtl())
                // tokenId
                .id(tokenId)
                // username
                .audience(username);
        tokenStrategy.saveToken(refreshTokenCacheKey(username), tokenId, tokenConfig.getRefreshTtl(), TimeUnit.MILLISECONDS);
        return jwtUtil.generateJwt(jwtPayload);
    }

    /**
     * 解析令牌
     *
     * @param token 令牌
     * @return {@link Claims}
     */
    public Claims resolveToken(String token) {
        // 截取token： Bearer {jwt}
        token = token.replace(tokenConfig.getTokenType() + " ", "");
        Claims claims = jwtUtil.resolveJwt(token);
        String tokenId = claims.getId();
        String username = claims.getAudience();
        if (tokenStrategy.isExpired(accessTokenCacheKey(username), tokenId)
                && tokenStrategy.isExpired(refreshTokenCacheKey(username), tokenId)) {
            throw new SignatureException("token已过期!");
        }
        return claims;
    }

    /**
     * 获得用户名
     *
     * @param token 令牌
     * @return {@link String}
     */
    public String getUsername(String token) {
        return resolveToken(token).getAudience();
    }

    /**
     * 使令牌失效
     *
     * @param token 令牌
     */
    public void invalidateToken(String token) {
        try {
            String username = getUsername(token);
            tokenStrategy.invalidateToken(accessTokenCacheKey(username), token);
            tokenStrategy.invalidateToken(refreshTokenCacheKey(username), token);
        } catch (SignatureException e) {
            // 若已过期不操作
        }
    }

}
