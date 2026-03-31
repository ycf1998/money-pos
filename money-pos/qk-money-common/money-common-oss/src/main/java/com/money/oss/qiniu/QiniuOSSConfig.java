package com.money.oss.qiniu;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 七牛云 OSS 配置
 *
 * @author : money
 * @since : 1.0.0
 */
@ConfigurationProperties(prefix = "money.oss.qiniu")
@Data
public class QiniuOSSConfig {
    /**
     * 访问密钥
     */
    private String accessKey = "";
    /**
     * 秘密密钥
     */
    private String secretKey = "";
    /**
     * 使用 https
     */
    private boolean useHttps = false;
    /**
     * 访问域名
     */
    private String domain = "";
    /**
     * 目标空间
     */
    private String bucket = "";
    /**
     * 令牌到期时间
     */
    private long tokenExpire = 3600L;
    /**
     * 上传策略
     */
    private Map<String, Object> policy = new HashMap<>();

}
