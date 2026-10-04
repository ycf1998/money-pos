package com.money.oss.local;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 本地 OSS 配置
 *
 * @author : money
 * @since : 1.0.0
 */
@ConfigurationProperties(prefix = "money.oss.local")
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
