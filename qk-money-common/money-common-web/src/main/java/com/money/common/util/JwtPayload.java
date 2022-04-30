package com.money.common.util;

import io.jsonwebtoken.Claims;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : money
 * @version : 1.0.0
 * @description : jwt载荷
 * @createTime : 2022-01-01 13:33:48
 */
public class JwtPayload implements Serializable {

    private static final long serialVersionUID = 3135427936015434777L;
    /**
     * 载体
     */
    private final Map<String, Object> claims;
    /**
     * 到期时间：单位秒
     */
    private final long expire;

    private JwtPayload(Map<String, Object> claims, long expire) {
        this.claims = claims;
        this.expire = expire;
    }

    /**
     * 构建器
     *
     * @param expire 到期：ms
     * @return {@link JwtPayload}
     */
    public static JwtPayload builder(long expire) {
        return new JwtPayload(new HashMap<>(), expire);
    }

    /**
     * id
     *
     * @param id id
     * @return {@link JwtPayload}
     */
    public JwtPayload id(String id) {
        claims.put(Claims.ID, id);
        return this;
    }

    /**
     * 接收者
     *
     * @param username 用户名
     * @return {@link JwtPayload}
     */
    public JwtPayload audience(String username) {
        claims.put(Claims.AUDIENCE, username);
        return this;
    }

    /**
     * 其他值
     *
     * @param key 关键
     * @param val 值
     * @return {@link JwtPayload}
     */
    public JwtPayload claims(String key, Object val) {
        claims.put(key, val);
        return this;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }

    public long getExpire() {
        return expire;
    }
}
