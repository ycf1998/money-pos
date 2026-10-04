package com.money.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 多租户配置属性
 * <p>配置前缀为 {@code money.tenant}。</p>
 *
 * @author money
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "money.tenant")
@Data
public class TenantProperties {

    /**
     * 是否启用多租户功能
     */
    private boolean enabled = true;

    /**
     * 租户 ID 请求头名称
     */
    private String header = "Y-tenant";

    /**
     * 默认租户 ID
     */
    private String defaultTenantId = "0";

    /**
     * 忽略租户隔离的表名列表
     */
    private List<String> ignoreTable = new ArrayList<>();
}
