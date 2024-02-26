package com.money.common.util;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : money
 * @version : 1.0.0
 * @description : jwt载荷
 * @createTime : 2022-01-01 13:33:48
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    /**
     * 构建器
     *
     * @param expire 到期：ms
     * @return {@link JwtPayload}
     */
    public static Builder builder(long expire) {
        return new Builder(new HashMap<>(), expire);
    }

    @AllArgsConstructor
    public static class Builder {

        /**
         * 载体
         */
        private final Map<String, Object> claims;

        /**
         * 到期时间：单位秒
         */
        private final long expire;

        /**
         * id
         *
         * @param id id
         * @return {@link Builder}
         */
        public Builder id(String id) {
            claims.put(Claims.ID, id);
            return this;
        }

        /**
         * 接收者
         *
         * @param username 用户名
         * @return {@link Builder}
         */
        public Builder audience(String username) {
            claims.put(Claims.AUDIENCE, username);
            return this;
        }

        /**
         * 其他值
         *
         * @param key 关键
         * @param val 值
         * @return {@link Builder}
         */
        public Builder claims(String key, Object val) {
            claims.put(key, val);
            return this;
        }

        public JwtPayload build() {
            return new JwtPayload(claims, expire);
        }
    }

}
