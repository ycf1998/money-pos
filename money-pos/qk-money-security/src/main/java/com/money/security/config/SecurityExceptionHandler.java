package com.money.security.config;


import com.money.common.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 安全异常处理器，注解抛出的AccessDeniedException不会被AccessDeniedHandler捕获，因为已经到了controller，所以单独捕获
 * @createTime : 2022-03-10 21:23:40
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
     * @param accessDeniedException   拒绝访问异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException accessDeniedException) {
        securityExceptionHandleConfig.handle(WebUtil.getRequest(), WebUtil.getResponse(), accessDeniedException);
    }

}
