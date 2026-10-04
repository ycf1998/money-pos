package com.money.cache.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 启用 Redis
 *
 * @author : money
 * @since : 1.0.0
 */
@ConditionalOnProperty(prefix = "money.cache.redis", name = "enabled")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RedisConfig.class)
@EnableCaching
@Import({RedisConfiguration.class})
public class EnabledRedis {
}
