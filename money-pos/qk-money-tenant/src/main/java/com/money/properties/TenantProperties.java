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
     * 头
     */
    private String header = "Y-tenant";

    /**
     * 忽略表
     */
    private List<String> ignoreTable = new ArrayList<>();
}
