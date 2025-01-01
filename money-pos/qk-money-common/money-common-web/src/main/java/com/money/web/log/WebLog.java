package com.money.web.log;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Web 日志
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
@Builder
public class WebLog {

    /**
     * 请求id
     */
    private String requestId;
    /**
     * 请求时间
     */
    private LocalDateTime requestTime;
    /**
     * IP
     */
    private String ip;
    /**
     * 请求方法
     */
    private String method;
    /**
     * URL
     */
    private String url;
    /**
     * URI
     */
    private String uri;
    /**
     * 请求参数
     */
    private String queryString;
    /**
     * 请求体
     */
    private String body;
    /**
     * 返回结果
     */
    private Object result;
    /**
     * 消耗时间（ms）
     */
    private long spendTime;
}
