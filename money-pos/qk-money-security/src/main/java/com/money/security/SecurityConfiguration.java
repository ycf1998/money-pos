package com.money.security;

import com.money.security.component.TokenStrategy;
import com.money.security.config.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 安全配置
 * @createTime : 2022-01-01 14:08:50
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({TokenConfig.class, IgnoreUrlConfig.class})
@Import(SecurityConfig.class)
public class SecurityConfiguration {

    /**
     * web mvc安全配置（解析@CurrentUser注解）
     *
     * @return {@link WebMvcConfigurer}
     */
    @Bean
    @ConditionalOnWebApplication
    WebMvcConfigurer securityWebMvcConfig() {
        return new WebMvcConfigurer() {
            /**
             * 添加参数解析器
             *
             * @param resolvers 解析器
             */
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(new CurrentUserArgumentResolver());
            }
        };
    }

    /**
     * token策略
     *
     * @return {@link TokenStrategy}
     */
    @Bean
    @ConditionalOnMissingBean(TokenStrategy.class)
    TokenStrategy tokenStrategy() {
        return new TokenStrategy() {
            @Override
            public void saveToken(String key, String token, long timeout, TimeUnit unit) {
            }

            @Override
            public boolean isExpired(String key, String targetToken) {
                return false;
            }

            @Override
            public void invalidateToken(String key, String token) {
            }
        };
    }


    /**
     * 安全异常处理配置
     *
     * @return {@link SecurityExceptionHandleConfig}
     */
    @Bean
    @ConditionalOnMissingBean(SecurityExceptionHandleConfig.class)
    SecurityExceptionHandleConfig securityExceptionConfig() {
        return new SecurityExceptionHandleConfig();
    }

    /**
     * 密码编码器
     *
     * @return {@link PasswordEncoder}
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
