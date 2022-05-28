package com.money.common.timezone.converter;

import com.money.common.timezone.TimeZoneUtil;

import java.time.ZoneId;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 默认时区转换器
 * @createTime : 2022-05-11 22:22:19
 */
public class DefaultTimeZoneConverter implements TimeZoneConverter {
    @Override
    public Object convert(Object o, String format, ZoneId formZoneId, ZoneId toZoneId) {
        return TimeZoneUtil.convert(o, format, formZoneId, toZoneId);
    }
}
