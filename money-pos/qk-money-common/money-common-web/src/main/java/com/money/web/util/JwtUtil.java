package com.money.web.util;

import com.money.web.constant.ProjectConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 工具
 *
 * @author : money
 * @since : 1.0.0
 */
public class JwtUtil {

    /**
     * 私钥
     */
    private final SecretKey secretKey;

    /**
     * 指定密钥
     *
     * @param secret 密钥
     */
    public JwtUtil(String secret) {
        if (secret.length() < 4) {
            throw new InvalidKeyException("密钥长度不能少于 4 位");
        }
        // 为了安全性，密钥必须大于 32 字节，为自定义密码补充固定尾缀
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret + "bW9uZXktMTk5ODA3MDktMjAyMTA5MTExMDU4NDA="));
    }

    /**
     * 生成 jwt
     *
     * @param jwtPayload jwt 载荷
     * @return {@link String}
     */
    public String generateJwt(JwtPayload jwtPayload) {
        return Jwts.builder()
                // 主题
                .subject(ProjectConstants.PROJECT_NAME)
                // 发行人
                .issuer(ProjectConstants.AUTHOR)
                // 颁发时间
                .issuedAt(new Date())
                // 载体
                .claims(jwtPayload.getClaims())
                // 过期时间
                .expiration(new Date(System.currentTimeMillis() + jwtPayload.getExpire()))
                // 签名密钥和算法
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 解析 jwt
     *
     * @param jwt jwt
     * @return {@link Claims}
     */
    public Claims resolveJwt(String jwt) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwt);
            return claimsJws.getPayload();
        } catch (Exception e) {
            throw new SignatureException("token 解析失败或者已过期");
        }
    }

}
