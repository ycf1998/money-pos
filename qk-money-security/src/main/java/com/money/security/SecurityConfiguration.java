package com.money.security;

import com.money.common.response.R;
import com.money.common.util.WebUtil;
import com.money.security.component.TokenStrategy;
import com.money.security.config.CurrentUserArgumentResolver;
import com.money.security.config.IgnoreUrlConfig;
import com.money.security.config.SecurityConfig;
import com.money.security.config.TokenConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
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
     * web mvc安全配置
     *
     * @return {@link WebMvcConfigurer}
     */
    @Bean
    @ConditionalOnWebApplication
    public WebMvcConfigurer securityWebMvcConfig() {
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
     * 密码编码器
     *
     * @return {@link PasswordEncoder}
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 拒绝访问处理器
     *
     * @return {@link AccessDeniedHandler}
     */
    @Bean
    AccessDeniedHandler restAccessDeniedHandler() {
        return (req, res, e) -> {
            log.info("权限不足，禁止访问");
            WebUtil.responseJson(res, HttpStatus.FORBIDDEN, R.forbidden());
        };
    }

    /**
     * 认证失败处理器
     *
     * @return {@link AuthenticationEntryPoint}
     */
    @Bean
    AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (req, res, e) -> {
            log.info("认证失败，无法访问");
            WebUtil.responseJson(res, HttpStatus.UNAUTHORIZED, R.unauthorized());
        };
    }
}
