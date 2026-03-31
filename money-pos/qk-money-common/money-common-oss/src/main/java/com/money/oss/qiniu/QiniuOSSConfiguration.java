package com.money.oss.qiniu;

import com.money.oss.OSSDelegate;
import com.qiniu.common.QiniuException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 对象存储服务配置
 *
 * @author : money
 * @since : 1.0.0
 */

@ConditionalOnClass(QiniuException.class)
@EnableConfigurationProperties(QiniuOSSConfig.class)
@Configuration(proxyBeanMethods = false)
public class QiniuOSSConfiguration {

    @Bean
    public OSSDelegate<QiniuOSS> qiniuOSS(QiniuOSSConfig config) {
        return new OSSDelegate<>(new QiniuOSS(config));
    }
}
