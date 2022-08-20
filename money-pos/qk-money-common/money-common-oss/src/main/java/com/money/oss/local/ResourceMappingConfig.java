package com.money.oss.local;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 资源映射配置
 * @createTime : 2022-01-06 23:06:22
 */
@RequiredArgsConstructor
public class ResourceMappingConfig implements WebMvcConfigurer {

    private final LocalOSSConfig localOSSConfig;

    /**
     * 静态资源映射
     *
     * @param registry 注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(localOSSConfig.getResourceHandler())
                .addResourceLocations("file:" + localOSSConfig.getBucket());
    }
}
