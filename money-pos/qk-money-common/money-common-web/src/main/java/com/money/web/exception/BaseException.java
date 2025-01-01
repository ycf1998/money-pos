package com.money.web.exception;


import com.money.web.i18n.I18nSupport;
import com.money.web.response.IStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 基本异常
 *
 * @author : money
 * @since : 1.0.0
 */
@Getter
@NoArgsConstructor
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 3620837280475323035L;

    /**
     * 错误代码
     */
    private int errorCode;

    public BaseException(String message, Object... args) {
        super(I18nSupport.get(message, args));
    }

    public BaseException(IStatus status) {
        super(status.getMessage());
        this.errorCode = status.getCode();
    }

    public BaseException(IStatus status, String message, Object... args) {
        super(I18nSupport.get(message, args));
        this.errorCode = status.getCode();
    }

}
