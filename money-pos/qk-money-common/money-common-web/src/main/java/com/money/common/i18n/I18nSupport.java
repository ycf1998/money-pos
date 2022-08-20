package com.money.common.i18n;

import cn.hutool.core.util.StrUtil;
import com.money.common.constant.WebRequestConstant;
import com.money.common.util.WebUtil;
import lombok.experimental.UtilityClass;
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
 * @author : money
 * @version : 1.0.0
 * @description : i18n支持
 * @createTime : 2022-05-08 11:56:25
 */
@Slf4j
@UtilityClass
public class I18nSupport {

    private I18nProperties i18nProperties;

    private final String I18N_PATH = "i18n/messages_{0}.properties";
    private final Map<String, Properties> I18N_MAP = new HashMap<>();

    /**
     * 开启多语言
     * <p>
     * 当 {@link I18nProperties} enabled为true时调用
     *
     * @param properties 属性
     */
    public static void enableI18n(I18nProperties properties) {
        I18nSupport.i18nProperties = properties;
        // 预加载
        log.info("预加载多语言文件, 支持语言：{}", properties.getSupport());
        properties.getSupport().forEach(I18nSupport::loadI18nProp);
    }

    public Properties loadI18nProp(String i18n) {
        Properties prop = I18N_MAP.get(i18n);
        if (prop != null) {
            return prop;
        }
        prop = new Properties();
        String i18nFile = MessageFormat.format(I18N_PATH, i18n);
        try {
            // 加载i18n文件
            Resource resource = new ClassPathResource(i18nFile);
            if (resource.exists()) {
                EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
                prop.putAll(PropertiesLoaderUtils.loadProperties(encodedResource));
            } else {
                log.error("未加载到 classpath:{} 语言文件", i18nFile);
            }
            I18N_MAP.put(i18n, prop);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return prop;
    }

    public String get(String key) {
        // 获取请求头里的客户端语言（不能从中WebRequestContextHolder获取，因为Spring Validation（JSR303参数校验）是在webLog切面前，此时未装填上下文）
        String lang = WebUtil.getRequest().getHeader(WebRequestConstant.HEADER_LANG);
        // 客户端未传语言 || 未开启多语言 || 不支持该语言
        if (StrUtil.isBlank(lang) || i18nProperties == null || !i18nProperties.isSupport(lang)) {
            return key;
        }
        return Optional.ofNullable(loadI18nProp(lang).getProperty(key)).orElse(key);
    }

}