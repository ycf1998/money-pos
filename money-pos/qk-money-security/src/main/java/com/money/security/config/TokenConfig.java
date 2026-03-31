package com.money.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 令牌配置属性
 * <p>配置前缀为 {@code money.security.token}。</p>
 *
 * @author money
 * @since 1.0.0
 */
@Data
@ConfigurationProperties("money.security.token")
public class TokenConfig {

    /**
     * Token 请求头键名
     */
    private String header = "Authorization";

    /**
     * 令牌类型前缀
     * <p>完整 Token 格式：{@code "{tokenType} {accessToken}"}
     */
    private String tokenType = "Bearer";

    /**
     * JWT 签名密钥
     * <p>⚠️ 生产环境务必修改此默认值！
     */
    private String secret = "money";

    /**
     * Access Token 过期时间（ms），默认 8 小时
     */
    private long ttl = 28800000L;

    /**
     * Refresh Token 过期时间（ms），默认 30 天
     */
    private long refreshTtl = 18144000000L;

    /**
     * Token 策略：jwt（默认）或 redis
     */
    private String strategy = "jwt";

    /**
     * Redis 缓存键前缀（仅 redis 策略使用）
     */
    private String cacheKey = "security:token:";

}
