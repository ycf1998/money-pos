package com.money.oss.local;

import com.money.oss.OSSDelegate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 对象存储服务配置
 * @createTime : 2022-01-01 16:53:08
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
