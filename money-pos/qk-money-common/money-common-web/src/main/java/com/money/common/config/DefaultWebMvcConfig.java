package com.money.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : money
 * @version : 1.0.0
 * @description : Web配置
 * @createTime : 2021-09-04 13:03:44
 */
@Configuration(proxyBeanMethods = false)
public class DefaultWebMvcConfig implements WebMvcConfigurer {

    /**
     * 跨域
     *
     * @param registry 注册表
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .maxAge(3600);
    }

}
