package com.money.cache.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("money.cache.redis")
@Data
public class RedisConfig {
    /**
     * 启用
     */
    private boolean enabled = false;

    /**
     * 过期时间：ms，主要用于@Cacheable相关注解的过期时间
     */
    private long ttl = 86400000L;
}
