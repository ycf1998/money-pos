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
    USER_ALREADY_EXIST(1001, "用户已存在"),
    OLD_PASSWORD_ERROR(1002, "旧密码错误"),
    ROLE_ALREADY_EXIST(1003, "角色已存在"),
    PERMISSION_ALREADY_EXIST(1004, "权限标识已存在"),
    TENANT_ALREADY_EXIST(1005, "租户已存在"),
    DICT_ALREADY_EXIST(1006, "字典已存在"),
    DICT_LABEL_ALREADY_EXIST(1007, "字典标签已存在");

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
