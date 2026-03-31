package com.money.security.config;

import com.money.web.response.R;
import com.money.web.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 安全异常处理配置
 * <p>实现 {@link AuthenticationEntryPoint} 和 {@link AccessDeniedHandler}，统一处理认证和授权失败。</p>
 * <p>响应：
 * <ul>
 *     <li>认证失败：HTTP 401，返回 {@code R.unauthorized()}</li>
 *     <li>授权失败：HTTP 403，返回 {@code R.forbidden()}</li>
 * </ul>
 * </p>
 *
 * @author money
 * @since 1.0.0
 */
@Slf4j
public class SecurityExceptionHandleConfig implements AccessDeniedHandler, AuthenticationEntryPoint {

    /**
     * 处理认证失败（未登录或 Token 无效）
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        log.warn("认证失败，拒绝访问：{} {}", request.getMethod(), request.getRequestURI());
        WebUtil.responseJson(response, HttpStatus.UNAUTHORIZED, R.unauthorized());
    }

    /**
     * 处理授权失败（权限不足）
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        log.warn("权限不足，禁止访问：{} {}", request.getMethod(), request.getRequestURI());
        WebUtil.responseJson(response, HttpStatus.FORBIDDEN, R.forbidden());
    }

}
