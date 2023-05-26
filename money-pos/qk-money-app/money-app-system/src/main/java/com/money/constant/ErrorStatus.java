package com.money.constant;

import com.money.common.response.IStatus;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 错误状态
 * @createTime : 2022-01-08 11:26:33
 */
public enum ErrorStatus implements IStatus {

    USER_NOT_FOUND(1000, "用户账号或密码错误"),
    OLD_PASSWORD_ERROR(1001, "旧密码错误"),
    DATA_ALREADY_EXIST(1002, "{}已存在"),
    ;

    final int code;

    final String message;

    ErrorStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
