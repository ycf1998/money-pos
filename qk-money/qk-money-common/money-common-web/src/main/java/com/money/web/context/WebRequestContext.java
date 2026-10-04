package com.money.web.context;

import lombok.Data;

import java.time.ZoneId;

/**
 * Web 请求上下文
 *
 * @author : money
 * @since : 1.0.0
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
