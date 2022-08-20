package com.money.common.response;

import lombok.AllArgsConstructor;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 状态
 * @createTime : 2022-05-26 21:44:27
 */
@AllArgsConstructor
public class Status implements IStatus {

    private int code;

    private String message;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
