package com.money.security.config;

import com.money.security.component.JwtAuthenticationFilter;
import com.money.security.component.SecurityUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Spring Security 过滤器链配置
 * <p>配置 Spring Security 的安全过滤器链，定义请求的认证和授权规则。</p>
 * <p>主要配置：
 * <ul>
 *     <li>禁用 CSRF、表单登录、HTTP Basic、登出等功能</li>
 *     <li>设置 Session 策略为 {@link SessionCreationPolicy#STATELESS}（无状态）</li>
 *     <li>添加 JWT 认证过滤器</li>
 *     <li>配置自定义的认证和授权异常处理器</li>
 *     <li>根据 {@link IgnoreUrlConfig} 放行指定的 URL</li>
 * </ul>
 * </p>
 *
 * @author money
 * @see SecurityFilterChain
 * @see JwtAuthenticationFilter
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final IgnoreUrlConfig ignoreUrlConfig;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SecurityUserService securityUserService;
    private final SecurityExceptionHandleConfig securityExceptionHandleConfig;

    /**
     * 配置安全过滤器链
     *
     * @param httpSecurity HttpSecurity 构建器
     * @return SecurityFilterChain
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 关闭 csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 关闭默认登录
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // 关闭默认登出
                .logout(AbstractHttpConfigurer::disable)
                // 使用 JWT，不管理 Session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 自定义身份验证
                .userDetailsService(securityUserService)
                // 自定义权限拦截器 JWT 过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 自定义认证授权异常控制器
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(securityExceptionHandleConfig)
                        .authenticationEntryPoint(securityExceptionHandleConfig))
                // 跨域配置
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    // 允许所有来源
                    config.addAllowedOriginPattern("*");
                    // 允许所有请求头
                    config.addAllowedHeader("*");
                    // 允许所有请求方法
                    config.addAllowedMethod("*");
                    // 预检请求缓存时间
                    config.setMaxAge(3600L);
                    return config;
                }))
                // 授权规则
                .authorizeHttpRequests(auth -> {
                    // 放行忽略的 url
                    ignoreUrlConfig.getPattern().forEach(url -> auth.requestMatchers(url).permitAll());
                    // 忽略 GET
                    ignoreUrlConfig.getGet().forEach(url -> auth.requestMatchers(HttpMethod.GET, url).permitAll());
                    // 忽略 POST
                    ignoreUrlConfig.getPost().forEach(url -> auth.requestMatchers(HttpMethod.POST, url).permitAll());
                    // 忽略 DELETE
                    ignoreUrlConfig.getDelete().forEach(url -> auth.requestMatchers(HttpMethod.DELETE, url).permitAll());
                    // 忽略 PUT
                    ignoreUrlConfig.getPut().forEach(url -> auth.requestMatchers(HttpMethod.PUT, url).permitAll());
                    // 忽略 HEAD
                    ignoreUrlConfig.getHead().forEach(url -> auth.requestMatchers(HttpMethod.HEAD, url).permitAll());
                    // 忽略 PATCH
                    ignoreUrlConfig.getPatch().forEach(url -> auth.requestMatchers(HttpMethod.PATCH, url).permitAll());
                    // 忽略 OPTIONS
                    ignoreUrlConfig.getOptions().forEach(url -> auth.requestMatchers(HttpMethod.OPTIONS, url).permitAll());
                    // 忽略 TRACE
                    ignoreUrlConfig.getTrace().forEach(url -> auth.requestMatchers(HttpMethod.TRACE, url).permitAll());
                    // 其他请求都需要认证
                    auth.anyRequest().authenticated();
                });
        return httpSecurity.build();
    }

}
