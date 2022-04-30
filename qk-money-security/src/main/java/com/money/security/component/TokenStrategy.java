package com.money.security.component;

import java.util.concurrent.TimeUnit;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 令牌牌战略
 * @createTime : 2022-01-01 15:03:27
 */
public interface TokenStrategy {

    /**
     * 保存令牌
     * 因为token使用的jwt，所以超时时间不应大于jwt的过期时间
     *
     * @param key     键
     * @param token   令牌
     * @param timeout 超时
     * @param unit    单位
     */
    void saveToken(String key, String token, long timeout, TimeUnit unit);

    /**
     * 是否过期
     *
     * @param key         键
     * @param targetToken 目标令牌
     * @return boolean
     */
    boolean isExpired(String key, String targetToken);

    /**
     * 令牌失效
     *
     * @param key   键
     * @param token 令牌
     */
    void invalidateToken(String key, String token);
}
