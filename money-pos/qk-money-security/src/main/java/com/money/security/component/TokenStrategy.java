package com.money.security.component;

import com.money.security.SecurityConfiguration;

import java.util.concurrent.TimeUnit;

/**
 * 令牌策略接口
 * <p>定义 Token 存储和验证的策略。</p>
 * <p>实现类：
 * <ul>
 *     <li>默认空实现（{@link SecurityConfiguration#tokenStrategy()}）</li>
 *     <li>Redis 实现（{@link TokenStrategyByRedis}）</li>
 * </ul>
 * </p>
 *
 * @author money
 * @since 1.0.0
 * @see TokenStrategyByRedis
 */
public interface TokenStrategy {

    /**
     * 保存令牌
     *
     * @param key     缓存键
     * @param token   令牌 ID
     * @param timeout 超时时间
     * @param unit    时间单位
     */
    void saveToken(String key, String token, long timeout, TimeUnit unit);

    /**
     * 检查令牌是否已过期
     *
     * @param key         缓存键
     * @param targetToken 目标令牌 ID
     * @return {@code true} 表示令牌已过期
     */
    boolean isExpired(String key, String targetToken);

    /**
     * 使令牌失效
     *
     * @param key   缓存键
     * @param token 令牌 ID
     */
    void invalidateToken(String key, String token);
}
