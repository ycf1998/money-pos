package com.money.security.config;

import com.money.security.component.JwtAuthenticationFilter;
import com.money.security.component.SecurityUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 安全配置
 * @createTime : 2022-01-01 15:04:06
 */
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final IgnoreUrlConfig ignoreUrlConfig;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SecurityUserService securityUserService;
    private final SecurityExceptionHandleConfig securityExceptionHandleConfig;

    /**
     * 过滤器链
     *
     * @param httpSecurity http安全性
     * @return {@link SecurityFilterChain}
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests(authorize -> {
                    // 放行忽略的url
                    this.ignore(authorize);
                    // 其他请求都需要认证
                    authorize.anyRequest().authenticated();
                })
                // 支持跨域
                .cors()
                // 关闭csrf
                .and().csrf().disable()
                // 关闭默认登录
                .formLogin().disable()
                .httpBasic().disable()
                // 关闭默认登出
                .logout().disable()
                // 使用JWT，不管理Session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 自定义身份验证
                .userDetailsService(securityUserService)
                // 自定义权限拦截器JWT过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 自定义认证授权异常控制器
                .exceptionHandling()
                .accessDeniedHandler(securityExceptionHandleConfig)
                .authenticationEntryPoint(securityExceptionHandleConfig);
        return httpSecurity.build();
    }

    /**
     * 忽略url
     *
     * @param registry 注册表
     */
    private void ignore(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
        // 按照请求格式忽略
        ignoreUrlConfig.getPattern().forEach(url -> registry.antMatchers(url).permitAll());
        // 忽略 GET
        ignoreUrlConfig.getGet().forEach(url -> registry.antMatchers(HttpMethod.GET, url).permitAll());
        // 忽略 POST
        ignoreUrlConfig.getPost().forEach(url -> registry.antMatchers(HttpMethod.POST, url).permitAll());
        // 忽略 DELETE
        ignoreUrlConfig.getDelete().forEach(url -> registry.antMatchers(HttpMethod.DELETE, url).permitAll());
        // 忽略 PUT
        ignoreUrlConfig.getPut().forEach(url -> registry.antMatchers(HttpMethod.PUT, url).permitAll());
        // 忽略 HEAD
        ignoreUrlConfig.getHead().forEach(url -> registry.antMatchers(HttpMethod.HEAD, url).permitAll());
        // 忽略 PATCH
        ignoreUrlConfig.getPatch().forEach(url -> registry.antMatchers(HttpMethod.PATCH, url).permitAll());
        // 忽略 OPTIONS
        ignoreUrlConfig.getOptions().forEach(url -> registry.antMatchers(HttpMethod.OPTIONS, url).permitAll());
        // 忽略 TRACE
        ignoreUrlConfig.getTrace().forEach(url -> registry.antMatchers(HttpMethod.TRACE, url).permitAll());
    }

}