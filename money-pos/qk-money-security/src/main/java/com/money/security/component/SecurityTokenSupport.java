package com.money.security.component;

import cn.hutool.core.util.IdUtil;
import com.money.web.util.JwtPayload;
import com.money.web.util.JwtUtil;
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
 * 安全令牌支持组件
 * <p>负责 JWT 令牌的生成、解析、验证和失效管理。</p>
 * <p>支持两种 Token 策略：
 * <ul>
 *     <li>{@code jwt}：纯 JWT 策略，Token 自包含过期时间，无法手动失效（默认）</li>
 *     <li>{@code redis}：基于 Redis 的策略，支持手动失效 Token</li>
 * </ul>
 * </p>
 * <p>使用示例：
 * <pre>{@code
 * // 生成令牌
 * String accessToken = securityTokenSupport.generateToken("username");
 * String refreshToken = securityTokenSupport.generateRefreshToken("username");
 *
 * // 解析令牌获取用户名
 * String username = securityTokenSupport.getUsername(token);
 *
 * // 使令牌失效（仅 Redis 策略有效）
 * securityTokenSupport.invalidateToken(token);
 * }</pre>
 * </p>
 *
 * @author money
 * @since 1.0.0
 */
@Component
@Getter
@Slf4j
public class SecurityTokenSupport {

    private final JwtUtil jwtUtil;
    private final TokenConfig tokenConfig;
    private final TokenStrategy tokenStrategy;

    /**
     * 构造方法，初始化令牌支持组件
     *
     * @param tokenConfig   令牌配置
     * @param tokenStrategy 令牌策略接口
     * @param strategy      策略名称（从配置读取）
     */
    @Autowired
    public SecurityTokenSupport(TokenConfig tokenConfig, TokenStrategy tokenStrategy,
                                @Value("${money.security.token.strategy}") String strategy) {
        this.tokenConfig = tokenConfig;
        this.jwtUtil = new JwtUtil(tokenConfig.getSecret());
        this.tokenStrategy = tokenStrategy;
        log.info("Token 认证策略为 {}，TokenStrategy = {}", strategy, tokenStrategy);
    }

    /**
     * 生成访问令牌
     *
     * @param username 用户名
     * @return JWT 格式的访问令牌
     */
    public String generateToken(String username) {
        String tokenId = IdUtil.objectId();
        JwtPayload jwtPayload = JwtPayload.builder(tokenConfig.getTtl())
                .id(tokenId)
                .audience(username)
                .build();
        tokenStrategy.saveToken(accessTokenCacheKey(username), tokenId, tokenConfig.getTtl(), TimeUnit.MILLISECONDS);
        return jwtUtil.generateJwt(jwtPayload);
    }

    /**
     * 生成刷新令牌
     *
     * @param username 用户名
     * @return JWT 格式的刷新令牌
     */
    public String generateRefreshToken(String username) {
        String tokenId = IdUtil.objectId();
        JwtPayload jwtPayload = JwtPayload.builder(tokenConfig.getRefreshTtl())
                .id(tokenId)
                .audience(username)
                .build();
        tokenStrategy.saveToken(refreshTokenCacheKey(username), tokenId, tokenConfig.getRefreshTtl(), TimeUnit.MILLISECONDS);
        return jwtUtil.generateJwt(jwtPayload);
    }

    /**
     * 解析并验证令牌
     *
     * @param token 完整的 JWT 令牌（包含 Bearer 前缀）
     * @return JWT 的 Claims 信息
     * @throws SignatureException 当令牌无效或已过期时抛出
     */
    public Claims resolveToken(String token) {
        // 截取 token： Bearer {jwt}
        token = token.replace(tokenConfig.getTokenType() + " ", "");
        Claims claims = jwtUtil.resolveJwt(token);
        String tokenId = claims.getId();
        String username = claims.getAudience().iterator().next();
        if (tokenStrategy.isExpired(accessTokenCacheKey(username), tokenId)
                && tokenStrategy.isExpired(refreshTokenCacheKey(username), tokenId)) {
            throw new SignatureException("Token has expired");
        }
        return claims;
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsername(String token) {
        return resolveToken(token).getAudience().iterator().next();
    }

    /**
     * 使令牌失效（仅 Redis 策略有效）
     *
     * @param token 要失效的令牌
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

    /**
     * 生成访问令牌的缓存键
     *
     * @param username 用户名
     * @return 缓存键
     */
    private String accessTokenCacheKey(String username) {
        return tokenConfig.getCacheKey() + username + ":string";
    }

    /**
     * 生成刷新令牌的缓存键
     *
     * @param username 用户名
     * @return 缓存键
     */
    private String refreshTokenCacheKey(String username) {
        return tokenConfig.getCacheKey() + ":refresh:" + username + ":string";
    }
}
