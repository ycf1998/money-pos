package com.money.cache.local;

import com.money.cache.local.hutool.HutoolCacheProperties;
import com.money.cache.local.hutool.HutoolCacheService;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 本地缓存配置
 * @createTime : 2021-10-23 18:26:43
 */
@Data
@Configuration
@ConfigurationProperties("money.cache.local")
@EnableConfigurationProperties({HutoolCacheProperties.class})
public class LocalCacheConfiguration {

    /**
     * 本地缓存提供者 hutool（默认）、caffeine
     */
    private String provider = "hutool";

    /**
     * 过期时间：ms，0代表永不过期
     */
    private long ttl = 0L;

    @Bean
    @ConditionalOnProperty(prefix="money.cache.local", name = "provider", havingValue="hutool", matchIfMissing = true)
    public HutoolCacheService hutoolCacheService(HutoolCacheProperties properties) {
        return new HutoolCacheService(properties, ttl);
    }
}
