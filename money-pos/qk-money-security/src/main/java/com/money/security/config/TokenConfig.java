package com.money.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 令牌配置
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
@ConfigurationProperties("money.security.token")
public class TokenConfig {

    /**
     * Token 请求头键名
     */
    private String header = "Authorization";

    /**
     * 令牌类型
     * <pre>
     * 完整 Token："{tokenType} {accessToken}"
     */
    private String tokenType = "Bearer";

    /**
     * 密钥
     */
    private String secret = "money";

    /**
     * Access Token 过期时间（ms），默认8小时
     */
    private long ttl = 28800000L;

    /**
     * Refresh Token 过期时间（ms），默认30天
     */
    private long refreshTtl = 18144000000L;

    /**
     * 策略
     * <pre>
     * jwt：JWT（默认）
     * redis：Redis
     */
    private String strategy = "jwt";

    /**
     * 缓存键名前缀
     */
    private String cacheKey = "security:token:";

}
