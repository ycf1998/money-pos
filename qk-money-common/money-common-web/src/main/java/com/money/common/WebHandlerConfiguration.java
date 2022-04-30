package com.money.common;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : money
 * @version : 1.0.0
 * @description : web处理器参数配置
 * @createTime : 2021-09-11 10:01:34
 */
@Data
@Configuration
@ConfigurationProperties("money.web")
public class WebHandlerConfiguration {
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
