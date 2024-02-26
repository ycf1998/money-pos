package com.money.common.util;

import com.money.common.constant.ProjectConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author : money
 * @version : 1.0.0
 * @description : jwt工具
 * @createTime : 2022-01-01 13:33:50
 */
public class JwtUtil {

    /**
     * 私钥
     */
    private final SecretKey privateKey;

    /**
     * 随机密钥
     */
    public JwtUtil() {
        this.privateKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /**
     * 指定密钥
     *
     * @param secret 密钥
     */
    public JwtUtil(String secret) {
        if (secret.length() < 4) {
            throw new InvalidKeyException("密钥长度不能少于4位");
        }
        // 0.11.2 的 jjwt 为了安全性，密码必须大于 32 位，为了补充自定义密码没那么长添加固定尾缀
        this.privateKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret + "bW9uZXktMTk5ODA3MDktMjAyMTA5MTExMDU4NDA="));
    }

    /**
     * 生成jwt
     *
     * @param jwtPayload jwt载荷
     * @return {@link String}
     */
    public String generateJwt(JwtPayload jwtPayload) {
        return Jwts.builder()
                // 主题
                .setSubject(ProjectConstants.PROJECT_NAME)
                // 发行人
                .setIssuer(ProjectConstants.AUTHOR)
                // 颁发时间
                .setIssuedAt(new Date())
                // 密码
                .signWith(privateKey, SignatureAlgorithm.HS256)
                // 载体
                .setClaims(jwtPayload.getClaims())
                // 过期时间
                .setExpiration(new Date(System.currentTimeMillis() + jwtPayload.getExpire()))
                .compact();
    }

    /**
     * 解析jwt
     *
     * @param jwt jwt
     * @return {@link Claims}
     */
    public Claims resolveJwt(String jwt) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(privateKey).build().parseClaimsJws(jwt);
            return claimsJws.getBody();
        } catch (Exception e) {
            throw new SignatureException("token解析失败或者已过期");
        }
    }

}
