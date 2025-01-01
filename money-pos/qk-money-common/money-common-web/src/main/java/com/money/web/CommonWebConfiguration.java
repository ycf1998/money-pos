package com.money.web;


import com.money.web.i18n.I18nProperties;
import com.money.web.i18n.I18nSupport;
import com.money.web.log.DefaultWebLogAspect;
import com.money.web.log.WebLogAspectProperties;
import com.money.web.timezone.TimeZoneAspect;
import com.money.web.timezone.TimeZoneProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 通用 Web 配置
 *
 * @author : money
 * @since : 1.0.0
 */
@Setter
@Getter
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties("money.web")
@EnableConfigurationProperties({WebLogAspectProperties.class, I18nProperties.class, TimeZoneProperties.class})
public class CommonWebConfiguration {

    /**
     * 全局响应处理器
     */
    private boolean responseHandler = true;

    /**
     * 全局异常处理器
     */
    private boolean exceptionHandler = true;

    /**
     * 默认web日志切面
     *
     * @param webLogAspectProperties webLogAspectProperties
     * @return {@link DefaultWebLogAspect }
     */
    @Bean
    @Order(-1)
    @ConditionalOnProperty(prefix = "money.web.web-log-aspect", name = "enabled", matchIfMissing = true)
    public DefaultWebLogAspect defaultWebLogAspect(WebLogAspectProperties webLogAspectProperties) {
        return new DefaultWebLogAspect(webLogAspectProperties);
    }

    /**
     * 多语言支持
     *
     * @param i18nProperties i18nProperties
     * @return {@link I18nSupport }
     */
    @Bean
    @ConditionalOnProperty(prefix = "money.web.i18n", name = "enabled")
    public I18nSupport i18nSupport(I18nProperties i18nProperties) {
        return new I18nSupport(i18nProperties);
    }

    /**
     * 多时区支持
     *
     * @param timeZoneProperties timeZoneProperties
     * @return {@link TimeZoneAspect }
     */
    @Bean
    @ConditionalOnProperty(prefix = "money.web.time-zone", name = "enabled")
    public TimeZoneAspect timeZoneAspect(TimeZoneProperties timeZoneProperties) {
        return new TimeZoneAspect(timeZoneProperties);
    }

}
