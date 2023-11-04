package com.money.common.i18n;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : i18n属性
 * @createTime : 2022-05-02 11:41:25
 */
@Data
@ConfigurationProperties("money.web.i18n")
public class I18nProperties implements InitializingBean {

    /**
     * 启用
     */
    private boolean enabled = false;

    /**
     * 支持
     */
    private List<String> support = new ArrayList<>();

    /**
     * 是支持
     *
     * @param lang 朗
     * @return boolean
     */
    public boolean isSupport(String lang) {
        return support.contains(lang);
    }

    @Override
    public void afterPropertiesSet() {
        // 多语言
        if (enabled) {
            I18nSupport.enableI18n(this);
        }
    }
}
