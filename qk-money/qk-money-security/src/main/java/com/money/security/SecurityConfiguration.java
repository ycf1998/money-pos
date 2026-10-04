package com.money.security;

import com.money.security.annotation.CurrentUser;
import com.money.security.component.TokenStrategy;
import com.money.security.config.CurrentUserArgumentResolver;
import com.money.security.config.IgnoreUrlConfig;
import com.money.security.config.SecurityConfig;
import com.money.security.config.SecurityExceptionHandleConfig;
import com.money.security.config.TokenConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 安全模块自动配置类
 * <p>配置 Spring Security 相关的 Bean，包括密码编码器、Token 策略、异常处理器等。</p>
 * <p>主要功能：
 * <ul>
 *     <li>启用方法级别的安全注解（{@code @PreAuthorize}、{@code @PostAuthorize} 等）</li>
 *     <li>注册 {@link CurrentUser} 注解的参数解析器</li>
 *     <li>提供默认的 Token 策略实现（空实现，可被 Redis 实现替换）</li>
 *     <li>提供 BCrypt 密码编码器</li>
 * </ul>
 * </p>
 *
 * @author money
 * @since 1.0.0
 * @see EnableMethodSecurity
 * @see SecurityConfig
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity
@EnableConfigurationProperties({TokenConfig.class, IgnoreUrlConfig.class})
@Import(SecurityConfig.class)
public class SecurityConfiguration {

    /**
     * 注册 {@link CurrentUser} 注解的参数解析器
     */
    @Bean
    @ConditionalOnWebApplication
    WebMvcConfigurer securityWebMvcConfig() {
        return new WebMvcConfigurer() {
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(new CurrentUserArgumentResolver());
            }
        };
    }

    /**
     * 默认 Token 策略（空实现）
     * <p>当没有配置 Redis 策略时使用此实现，Token 无法手动失效。</p>
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
     */
    @Bean
    @ConditionalOnMissingBean(SecurityExceptionHandleConfig.class)
    SecurityExceptionHandleConfig securityExceptionConfig() {
        return new SecurityExceptionHandleConfig();
    }

    /**
     * BCrypt 密码编码器
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
