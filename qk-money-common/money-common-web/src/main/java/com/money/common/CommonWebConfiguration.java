package com.money.common;


import com.money.common.i18n.I18nProperties;
import com.money.common.timezone.TimeZoneProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : money
 * @version : 1.0.0
 * @description : web配置
 * @createTime : 2021-09-11 10:01:34
 */
@Configuration
@EnableConfigurationProperties({CommonWebProperties.class, I18nProperties.class, TimeZoneProperties.class})
public class CommonWebConfiguration {

}
