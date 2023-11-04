package com.money.security.component;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.JwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("=============================================");
        log.info("请求认证 {} {}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI());
        String token = httpServletRequest.getHeader(securityTokenSupport.getTokenConfig().getHeader());
        // 仅处理带token的请求
        if (StrUtil.isNotBlank(token)) {
            try {
                String username = securityTokenSupport.getUsername(token);
                UserDetails userDetails = securityUserService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("认证成功：{}", username);
                // 提供用户日志追踪
                MDC.put("userId", username);
            } catch (AuthenticationException | JwtException e) {
                log.error("认证失败！", e);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
