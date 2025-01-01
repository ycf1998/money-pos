package com.money.web.i18n;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.money.web.context.WebRequestContextHolder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * i18n 支持
 *
 * @author : money
 * @since : 1.0.0
 */
@Slf4j
public class I18nSupport {

    private final Map<String, Properties> I18N_MAP = new HashMap<>();

    private final I18nProperties i18nProperties;

    public I18nSupport(@NonNull I18nProperties properties) {
        this.i18nProperties = properties;
        // 预加载
        log.info("预加载多语言文件, 支持语言：{}", properties.getSupport());
        properties.getSupport().forEach(this::loadI18nProp);
    }

    /**
     * 加载多语言资源文件
     *
     * @param lang 语言
     * @return {@link Properties }
     */
    private Properties loadI18nProp(String lang) {
        Properties prop = I18N_MAP.get(lang);
        if (prop != null) {
            return prop;
        }
        prop = new Properties();
        String i18nFile = MessageFormat.format(i18nProperties.getBasename() + "_{0}.properties", lang);
        try {
            // 加载i18n文件
            Resource resource = new ClassPathResource(i18nFile);
            if (resource.exists()) {
                EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
                prop.putAll(PropertiesLoaderUtils.loadProperties(encodedResource));
            } else {
                log.error("未加载到 classpath:{} 语言文件", i18nFile);
            }
            I18N_MAP.put(lang, prop);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return prop;
    }

    /**
     * 获取
     *
     * @param lang 语言
     * @param key  键
     * @param args 参数
     * @return {@link String }
     */
    public String get(String lang, String key, Object... args) {
        String val;
        // 客户端未传语言 || 未开启多语言 || 不支持该语言
        if (StrUtil.isBlank(lang) || !i18nProperties.isSupport(lang)) {
            val = StrUtil.indexedFormat(key, args);
        } else {
            val = Optional.ofNullable(loadI18nProp(lang).getProperty(key)).orElse(key);
        }
        // 使用 {0}..{n} 占位
        return MessageFormat.format(val, args);
    }

    // ================================ 静态方法 ========================================

    /**
     * 获取
     *
     * @param key  键
     * @param args 参数
     * @return {@link String }
     */
    public static String get(String key, Object... args) {
        String lang = WebRequestContextHolder.getContext().getLang();
        return SpringUtil.getBean(I18nSupport.class).get(lang, key, args);
    }

}