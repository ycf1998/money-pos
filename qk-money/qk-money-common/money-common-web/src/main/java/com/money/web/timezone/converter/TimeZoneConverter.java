package com.money.web.timezone.converter;

import java.time.ZoneId;

/**
 * 时区转换器
 *
 * @author : money
 * @since : 1.0.0
 */
public interface TimeZoneConverter {

    /**
     * 转换
     *
     * @param o          转换目标
     * @param format     格式
     * @param formZoneId 表单区域 ID
     * @param toZoneId   目标区域 ID
     * @return {@link Object }
     */
    Object convert(Object o, String format, ZoneId formZoneId, ZoneId toZoneId);
}
