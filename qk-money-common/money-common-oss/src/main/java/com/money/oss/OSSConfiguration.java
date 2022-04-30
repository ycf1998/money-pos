package com.money.oss;

import com.money.oss.local.LocalOSS;
import com.money.oss.local.LocalOSSConfig;
import com.money.oss.local.ResourceMappingConfig;
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
public class OSSConfiguration {

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
