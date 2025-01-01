package com.money.web.timezone;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 时区属性
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
@ConfigurationProperties("money.web.timezone")
public class TimeZoneProperties {

    /**
     * 启用
     */
    private boolean enabled = false;

    /**
     * 默认时区
     */
    private String defaultTimeZone = "GMT+08:00";

}
