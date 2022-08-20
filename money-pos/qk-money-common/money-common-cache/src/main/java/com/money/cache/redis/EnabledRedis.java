package com.money.cache.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 启用redis
 * @createTime : 2021-10-23 18:35:12
 */
@ConditionalOnProperty(prefix = "money.cache.redis", name = "enabled")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RedisConfig.class)
@EnableCaching
@Import({RedisConfiguration.class})
public class EnabledRedis {
}
