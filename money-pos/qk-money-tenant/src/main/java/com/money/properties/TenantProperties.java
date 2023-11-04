package com.money.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "money.tenant")
@Data
public class TenantProperties {

    /**
     * 租户启用
     */
    private boolean enabled = true;

    /**
     * 租户请求头
     */
    private String header = "Y-tenant";

    /**
     * 默认租户id
     */
    private String defaultTenantId = "0";

    /**
     * 忽略表
     */
    private List<String> ignoreTable = new ArrayList<>();
}
