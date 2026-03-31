package com.money.web.log;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志切面属性
 * <p>配置前缀：{@code money.web.web-log-aspect}</p>
 *
 * @author money
 * @since 1.0.0
 */
@Data
@ConfigurationProperties("money.web.web-log-aspect")
public class WebLogAspectProperties {

    /**
     * 是否启用日志切面
     */
    private boolean enabled = true;

    /**
     * 日志模式
     */
    private Mode mode = Mode.ALL;

    /**
     * 脱敏字段列表
     * <p>列表为空时表示不启用脱敏</p>
     */
    private List<String> desensitizeFields = new ArrayList<>();

    /**
     * 日志模式枚举
     */
    public enum Mode {

        /**
         * 完整打印
         */
        ALL,

        /**
         * 简单打印（不打印请求体和响应结果）
         */
        SIMPLE,

        /**
         * 不打印 GET 请求的响应结果
         */
        IGNORE_GET_RESULT,
    }

}
