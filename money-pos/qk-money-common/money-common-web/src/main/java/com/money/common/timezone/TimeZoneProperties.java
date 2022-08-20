package com.money.common.timezone;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 时区属性
 * @createTime : 2022-05-02 11:44:01
 */
@Data
@ConfigurationProperties("money.web.timezone")
public class TimeZoneProperties {

    /**
     * 默认时区
     */
    private String defaultTimeZone = "GMT+08:00";

}
