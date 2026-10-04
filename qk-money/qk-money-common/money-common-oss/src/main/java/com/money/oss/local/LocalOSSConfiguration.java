package com.money.oss.local;

import com.money.oss.OSSDelegate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 对象存储服务配置
 *
 * @author : money
 * @since : 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class LocalOSSConfiguration {

    @Bean
    @ConditionalOnMissingBean(LocalOSSConfig.class)
    public LocalOSSConfig localOSSConfig() {
        return new LocalOSSConfig();
    }

    @Bean
    public OSSDelegate<LocalOSS> localOSS(LocalOSSConfig config) {
        return new OSSDelegate<>(new LocalOSS(config));
    }

    @Bean
    public ResourceMappingConfig resourceMappingConfig(LocalOSSConfig config) {
        return new ResourceMappingConfig(config);
    }

}
