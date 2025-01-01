package com.money.constant;

import com.money.web.response.IStatus;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 错误状态
 * @createTime : 2022-01-08 11:26:33
 */
public enum SysErrorStatus implements IStatus {

    USER_NOT_FOUND(1000, "用户账号或密码错误"),
    OLD_PASSWORD_ERROR(1001, "旧密码错误"),
    DATA_ALREADY_EXIST(1002, "数据已存在"),
    DATA_NOT_FOUND(1003, "数据不存在"),
    USER_NOT_ENABLED(1004, "账号已被禁用"),
    ;

    final int code;

    final String message;

    SysErrorStatus(int code, String message) {
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
