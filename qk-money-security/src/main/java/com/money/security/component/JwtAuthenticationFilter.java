package com.money.security.component;

import cn.hutool.core.util.StrUtil;
import com.money.common.response.R;
import com.money.common.util.WebUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author : money
 * @version : 1.0.0
 * @description : jwt身份验证过滤器
 * @createTime : 2022-01-01 14:24:19
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityTokenSupport securityTokenSupport;
    private final SecurityUserService securityUserService;

    /**
     * 解决SpringSecurity全局忽略url配置无效问题
     * （因为托管给Spring的过滤器被注册到servlet，在最前面就已经拦截了，还没到security）
     *
     * @param filter 过滤器
     * @return {@link FilterRegistrationBean}<{@link JwtAuthenticationFilter}>
     */
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> registration(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.info("=============================================");
        log.info("请求认证 {} {}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI());
        String token = httpServletRequest.getHeader(securityTokenSupport.getTokenConfig().getHeader());
        if (StrUtil.isBlank(token)) {
            log.info("未携带token，认证失败");
            WebUtil.responseJson(httpServletResponse, HttpStatus.UNAUTHORIZED, R.unauthorized());
            return;
        }
        try {
            String username = securityTokenSupport.getUsername(token);
            log.info("解析token成功，认证用户为：{}", username);
            UserDetails userDetails = securityUserService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("认证成功！");
            // 提供用户日志追踪
            MDC.put("userId", username);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (AuthenticationException | JwtException e) {
            log.error("认证失败！", e);
            WebUtil.responseJson(httpServletResponse, HttpStatus.UNAUTHORIZED, R.unauthorized());
        }
    }
}
