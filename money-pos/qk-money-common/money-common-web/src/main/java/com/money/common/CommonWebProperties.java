package com.money.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 通用web属性
 * @createTime : 2022-05-02 11:39:18
 */
@Data
@ConfigurationProperties("money.web")
public class CommonWebProperties {

    /**
     * 全局响应处理器
     */
    private boolean responseHandler = true;

    /**
     * 全局异常处理器
     */
    private boolean exceptionHandler = true;

    /**
     * 全局请求日志切面
     */
    private boolean webLogAspect = true;

}
