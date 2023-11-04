package com.money.oss.local;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 本地OSS配置
 * @createTime : 2022-01-01 16:46:59
 */
@ConfigurationProperties(prefix = "local")
@Data
public class LocalOSSConfig {
    /**
     * 目标空间 如 F:/qk-money/
     */
    private String bucket;
    /**
     * 资源处理器
     */
    private String resourceHandler;

}
