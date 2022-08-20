package com.money.common.exception;


import com.money.common.response.IStatus;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 基础异常
 * @createTime : 2022-01-01 13:25:02
 */
public class BaseException extends RuntimeException {

    /**
     * 错误代码
     */
    private int errorCode = 500;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(IStatus status) {
        super(status.getMessage());
        this.errorCode = status.getCode();
    }

    public int getErrorCode() {
        return errorCode;
    }
}
