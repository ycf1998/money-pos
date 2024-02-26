package com.money.cache.local.hutool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : money
 * @version : 1.0.0
 * @description : hutool缓存属性
 * @createTime : 2021-09-21 13:40:02
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
     * 过期时间：ms，0代表永不过期
     */
    private long ttl = 0L;
}
