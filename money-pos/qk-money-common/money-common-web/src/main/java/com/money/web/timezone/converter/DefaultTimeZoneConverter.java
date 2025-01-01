package com.money.web.timezone.converter;

import com.money.web.timezone.TimeZoneUtil;

import java.time.ZoneId;

/**
 * 默认时区转换器
 *
 * @author : money
 * @since : 1.0.0
 */
public class DefaultTimeZoneConverter implements TimeZoneConverter {
    @Override
    public Object convert(Object o, String format, ZoneId formZoneId, ZoneId toZoneId) {
        return TimeZoneUtil.convert(o, format, formZoneId, toZoneId);
    }
}
