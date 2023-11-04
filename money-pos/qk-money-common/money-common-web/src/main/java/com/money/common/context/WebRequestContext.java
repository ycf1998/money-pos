package com.money.common.context;

import lombok.Data;

import java.time.ZoneId;

/**
 * @author : money
 * @version : 1.0.0
 * @description : web请求上下文
 * @createTime : 2022-05-02 11:23:47
 */
@Data
public class WebRequestContext {

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 语言
     */
    private String lang;

    /**
     * 时区
     */
    private ZoneId timezone;
}
