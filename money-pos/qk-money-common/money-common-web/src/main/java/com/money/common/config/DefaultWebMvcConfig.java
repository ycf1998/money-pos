package com.money.common.config;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        WebMvcConfigurer.super.addFormatters(registry);
        registry.addConverter(String.class, Date.class, DateUtil::parseDateTime);
        registry.addConverter(String.class, LocalDateTime.class, DateUtil::parseLocalDateTime);
        registry.addConverter(String.class, LocalDate.class, source -> LocalDate.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        registry.addConverter(String.class, LocalTime.class, source -> LocalTime.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
    }
}
