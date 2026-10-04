package com.money.web.i18n;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * i18n 属性
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
@ConfigurationProperties("money.web.i18n")
public class I18nProperties {

    /**
     * 启用
     */
    private boolean enabled = false;

    /**
     * 资源文件基本名称
     */
    private String basename = "i18n/messages";

    /**
     * 支持的语言
     */
    private Set<String> support = new HashSet<>();

    /**
     * 是支持
     *
     * @param lang 语言
     * @return boolean
     */
    public boolean isSupport(String lang) {
        return support.contains(lang);
    }

}
