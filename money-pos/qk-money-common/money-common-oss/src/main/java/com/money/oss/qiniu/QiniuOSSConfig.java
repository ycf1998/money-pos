package com.money.oss.qiniu;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 七牛云OSS配置
 * @createTime : 2022-01-01 16:47:24
 */
@ConfigurationProperties(prefix = "qiniu")
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
     * 使用https
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
     * 地区
     */
    private String region = "";
    /**
     * 令牌到期时间
     */
    private long tokenExpire = 3600L;
    /**
     * 上传策略
     */
    private Map<String, Object> policy = new HashMap<>();

}
