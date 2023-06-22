package com.money.common.exception;


import com.money.common.i18n.I18nSupport;
import com.money.common.response.IStatus;
import lombok.NoArgsConstructor;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 基础异常
 * @createTime : 2022-01-01 13:25:02
 */
@NoArgsConstructor
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 3620837280475323035L;

    /**
     * 错误代码
     */
    private int errorCode = 500;

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

    public int getErrorCode() {
        return errorCode;
    }
}
