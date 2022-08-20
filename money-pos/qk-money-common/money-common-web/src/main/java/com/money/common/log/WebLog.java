package com.money.common.log;


import lombok.Builder;
import lombok.Data;

/**
 * @author : money
 * @version : 1.0.0
 * @description : web日志
 * @createTime : 2022-01-01 13:26:24
 */
@Data
@Builder
public class WebLog {

    /**
     * 操作时间
     */
    private String operationTime;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 操作描述
     */
    private String description;
    /**
     * 请求类型
     */
    private String method;
    /**
     * 根路径
     */
    private String basePath;
    /**
     * URI
     */
    private String uri;
    /**
     * URL
     */
    private String url;
    /**
     * 请求参数
     */
    private Object parameter;
    /**
     * 返回结果
     */
    private Object result;
    /**
     * 消耗时间
     */
    private long spendTime;
}
