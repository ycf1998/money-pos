package com.money.security.config;


import com.money.web.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 安全例外处理器
 * <p>
 * 由于如 @PreAuthorize 注解抛出的 AccessDeniedException 已经在 Controller 层，无法被 AccessDeniedHandler 处理，因此需单独捕获处理
 *
 * @author : money
 * @since : 1.0.0
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@Order(-1)
public class SecurityExceptionHandler {

    private final SecurityExceptionHandleConfig securityExceptionHandleConfig;

    /**
     * 处理拒绝访问异常
     *
     * @param accessDeniedException 拒绝访问异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException accessDeniedException) {
        securityExceptionHandleConfig.handle(WebUtil.getRequest(), WebUtil.getResponse(), accessDeniedException);
    }

}
