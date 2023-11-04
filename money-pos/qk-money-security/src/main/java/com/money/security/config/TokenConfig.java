package com.money.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 令牌配置
 * @createTime : 2022-01-06 22:44:43
 */
@Data
@ConfigurationProperties("money.security.token")
public class TokenConfig {

    /**
     * token请求头名称
     */
    private String header = "Authorization";

    /**
     * 令牌类型：完整token："{tokenType} {accessToken}"
     */
    private String tokenType = "Bearer";

    /**
     * 密钥
     */
    private String secret = "money";

    /**
     * access token过期时间 ms，默认3小时
     */
    private long ttl = 14400000L;

    /**
     * refresh token过期时间 ms，默认30天
     */
    private long refreshTtl = 18144000000L;

    /**
     * 策略
     */
    private String strategy = "jwt";

    /**
     * 缓存键名
     */
    private String cacheKey = "security:token:";

}
