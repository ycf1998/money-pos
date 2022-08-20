package com.money.security.component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 令牌策略通过redis
 * @createTime : 2022-01-01 15:03:39
 */
@Component
@ConditionalOnProperty(prefix = "money.security.token", name = "strategy", havingValue = "redis")
@RequiredArgsConstructor
public class TokenStrategyByRedis implements TokenStrategy {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void saveToken(String key, String token, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, token, timeout, unit);
    }

    @Override
    public boolean isExpired(String key, @NonNull String targetToken) {
        String token = redisTemplate.opsForValue().get(key);
        return !targetToken.equals(token);
    }

    @Override
    public void invalidateToken(String key, String token) {
        redisTemplate.delete(key);
    }
}
