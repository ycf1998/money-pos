package com.money.security.component;

import java.util.concurrent.TimeUnit;

/**
 * 令牌策略
 *
 * @author : money
 * @since : 1.0.0
 */
public interface TokenStrategy {

    /**
     * 保存令牌
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
