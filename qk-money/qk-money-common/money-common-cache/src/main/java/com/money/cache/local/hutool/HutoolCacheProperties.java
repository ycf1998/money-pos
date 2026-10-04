package com.money.cache.local.hutool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Hutool 缓存属性
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "money.cache.local.hutool")
public class HutoolCacheProperties {

    /**
     * 策略
     */
    private String strategy = "LRU";

    /**
     * 容量
     */
    private int capacity = 102400;

    /**
     * 过期时间：ms，0 代表永不过期
     */
    private long ttl = 0L;
}
