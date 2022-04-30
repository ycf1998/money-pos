package com.money.security.config;

import com.money.security.component.JwtAuthenticationFilter;
import com.money.security.component.SecurityUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 安全配置
 * @createTime : 2022-01-01 15:04:06
 */
@Slf4j
@RequiredArgsConstructor
public class  SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityUserService securityUserService;
    private final IgnoreUrlConfig ignoreUrlConfig;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationEntryPoint restAuthenticationEntryPoint;
    private final AccessDeniedHandler restAccessDeniedHandler;

    /**
     * 认证配置
     *
     * @param auth 身份验证管理
     * @throws Exception 异常
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserService).passwordEncoder(passwordEncoder);
    }

    /**
     * 全局web security配置
     *
     * @param web 网络
     */
    @Override
    public void configure(WebSecurity web) {
        WebSecurity and = web.ignoring().and();
        // 按照请求格式忽略
        ignoreUrlConfig.getPattern().forEach(url -> and.ignoring().antMatchers(url));
        // 忽略 GET
        ignoreUrlConfig.getGet().forEach(url -> and.ignoring().antMatchers(HttpMethod.GET, url));
        // 忽略 POST
        ignoreUrlConfig.getPost().forEach(url -> and.ignoring().antMatchers(HttpMethod.POST, url));
        // 忽略 DELETE
        ignoreUrlConfig.getDelete().forEach(url -> and.ignoring().antMatchers(HttpMethod.DELETE, url));
        // 忽略 PUT
        ignoreUrlConfig.getPut().forEach(url -> and.ignoring().antMatchers(HttpMethod.PUT, url));
        // 忽略 HEAD
        ignoreUrlConfig.getHead().forEach(url -> and.ignoring().antMatchers(HttpMethod.HEAD, url));
        // 忽略 PATCH
        ignoreUrlConfig.getPatch().forEach(url -> and.ignoring().antMatchers(HttpMethod.PATCH, url));
        // 忽略 OPTIONS
        ignoreUrlConfig.getOptions().forEach(url -> and.ignoring().antMatchers(HttpMethod.OPTIONS, url));
        // 忽略 TRACE
        ignoreUrlConfig.getTrace().forEach(url -> and.ignoring().antMatchers(HttpMethod.TRACE, url));
    }

    /**
     * security 安全流程策略配置
     *
     * @param http http
     * @throws Exception 异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
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
                // 自定义认证授权异常控制器
                .and()
                .exceptionHandling()
                .accessDeniedHandler(restAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                // 自定义权限拦截器JWT过滤器
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                // 自定义权限管理
//                .authorizeRequests()
//                .anyRequest()
//                .access("@rbacAuthorityService.hasPermission(request, authentication)");
    }

}