package com.money.cache.local;

import com.money.cache.local.hutool.HutoolCache;
import com.money.cache.local.hutool.HutoolCacheProperties;
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

    @Bean
    @ConditionalOnProperty(prefix="money.cache.local", name = "provider", havingValue="hutool", matchIfMissing = true)
    public HutoolCache hutoolCacheService(HutoolCacheProperties properties) {
        return new HutoolCache(properties);
    }
}
