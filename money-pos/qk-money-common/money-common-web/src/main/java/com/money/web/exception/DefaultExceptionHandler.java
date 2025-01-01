package com.money.web.exception;


import com.money.web.i18n.I18nSupport;
import com.money.web.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;

/**
 * 默认全局异常处理器
 *
 * @author : money
 * @since : 1.0.0
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnProperty(prefix = "money.web", name = "exception-handler", matchIfMissing = true)
public class DefaultExceptionHandler {

    /**
     * 处理其他异常
     *
     * @param e e
     * @return {@link R}<{@link String}>
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public R<String> handleOtherException(Exception e) {
        log.error("系统异常：", e);
        return R.fail();
    }

    /**
     * 处理业务异常
     *
     * @param e e
     * @return {@link R}<{@link String}>
     */
    @ExceptionHandler(BaseException.class)
    public R<String> handleBusinessException(BaseException e) {
        log.warn("业务异常：{}", e.getMessage());
        if (e.getErrorCode() == 0) {
            return R.fail(e.getMessage());
        }
        return R.fail(e.getErrorCode(), e.getMessage());
    }

    /**
     * 处理表单参数异常
     *
     * @param e e
     * @return {@link R}<{@link String}>
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public R<String> handleValidException(BindException e) {
        return R.validateFailed(getValidationMessage(e));
    }

    /**
     * 处理JSON参数异常
     *
     * @param e e
     * @return {@link R}<{@link String}>
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R<String> handleValidException(MethodArgumentNotValidException e) {
        return R.validateFailed(getValidationMessage(e));
    }

    /**
     * 处理请求参数异常
     *
     * @param e e
     * @return {@link R}<{@link String}>
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public R<String> handleValidException(ConstraintViolationException e) {
        return R.validateFailed(getValidationMessage(e));
    }

    /**
     * 得到验证消息
     *
     * @param e e
     * @return {@link String}
     */
    private String getValidationMessage(Exception e) {
        String message = "";
        if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            BindingResult bindingResult = bindException.getBindingResult();
            if (bindingResult.hasErrors()) {
                FieldError fieldError = bindingResult.getFieldError();
                if (fieldError != null) {
                    message = "[" + fieldError.getField() + "] " + I18nSupport.get(fieldError.getDefaultMessage());
                }
            }
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) e;
            Iterator<ConstraintViolation<?>> iterator = constraintViolationException.getConstraintViolations().iterator();
            if (iterator.hasNext()) {
                ConstraintViolation<?> violation = iterator.next();
                message = "[" + violation.getPropertyPath() + "] " + violation.getMessage();
            }
        }
        log.warn("参数异常：{}", message);
        return message;
    }

}
