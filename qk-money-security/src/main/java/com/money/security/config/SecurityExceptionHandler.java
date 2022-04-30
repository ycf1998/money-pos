package com.money.security.config;


import com.money.common.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 安全异常处理器，注解抛出的AccessDeniedException不会被AccessDeniedHandler捕获，因为已经到了controller，所以单独捕获
 * @createTime : 2022-03-10 21:23:40
 */
@Slf4j
@RestControllerAdvice
@Order(-1)
public class SecurityExceptionHandler {

    /**
     * 处理其他异常
     *
     * @param e e
     * @return {@link R}<{@link String}>
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public R<String> handleAccessDeniedException(AccessDeniedException e) {
        log.error("权限不足，禁止访问", e);
        return R.forbidden();
    }

}
