package com.money.security.config;

import com.money.web.response.R;
import com.money.web.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 安全异常处理配置
 *
 * @author : money
 * @since : 1.0.0
 */
@Slf4j
public class SecurityExceptionHandleConfig implements AccessDeniedHandler, AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        log.warn("认证失败，拒绝访问：{} {}", request.getMethod(), request.getRequestURI());
        WebUtil.responseJson(response, HttpStatus.UNAUTHORIZED, R.unauthorized());
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        log.warn("权限不足，禁止访问：{} {}", request.getMethod(), request.getRequestURI());
        WebUtil.responseJson(response, HttpStatus.FORBIDDEN, R.forbidden());
    }

}
