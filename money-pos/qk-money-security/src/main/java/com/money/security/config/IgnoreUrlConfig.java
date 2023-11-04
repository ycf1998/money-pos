package com.money.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 忽略url配置
 * @createTime : 2022-01-01 15:04:03
 */
@Data
@ConfigurationProperties("money.security.ignore")
public class IgnoreUrlConfig {
    /**
     * 需要忽略的 URL 格式，不考虑请求方法
     */
    private List<String> pattern = new ArrayList<>();

    /**
     * 需要忽略的 GET 请求
     */
    private List<String> get = new ArrayList<>();

    /**
     * 需要忽略的 POST 请求
     */
    private List<String> post = new ArrayList<>();

    /**
     * 需要忽略的 DELETE 请求
     */
    private List<String> delete = new ArrayList<>();

    /**
     * 需要忽略的 PUT 请求
     */
    private List<String> put = new ArrayList<>();

    /**
     * 需要忽略的 HEAD 请求
     */
    private List<String> head = new ArrayList<>();

    /**
     * 需要忽略的 PATCH 请求
     */
    private List<String> patch = new ArrayList<>();

    /**
     * 需要忽略的 OPTIONS 请求
     */
    private List<String> options = new ArrayList<>();

    /**
     * 需要忽略的 TRACE 请求
     */
    private List<String> trace = new ArrayList<>();
}
