package com.money.common.timezone.converter;

import java.time.ZoneId;

public interface TimeZoneConverter {

    Object convert(Object o, String format, ZoneId formZoneId, ZoneId toZoneId);
}
