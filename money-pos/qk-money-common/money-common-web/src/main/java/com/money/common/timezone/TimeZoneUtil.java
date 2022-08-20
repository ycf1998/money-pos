package com.money.common.timezone;

import cn.hutool.core.collection.ListUtil;
import com.money.common.timezone.annotation.TZParam;
import com.money.common.timezone.converter.DefaultTimeZoneConverter;
import com.money.common.timezone.converter.TimeZoneConverter;
import com.money.common.vo.PageVO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 时区工具
 * @createTime : 2022-05-11 22:04:58
 */
@Slf4j
@UtilityClass
public class TimeZoneUtil {

    /**
     * map key含有关键字则转换
     */
    private final List<String> MAP_KEY = ListUtil.toList("time", "date");

    /**
     * 转换
     *
     * @param target
     * @param format
     * @param formZoneId
     * @param toZoneId
     * @return {@link Object}
     */
    public Object convert(Object target, String format, ZoneId formZoneId, ZoneId toZoneId) {
        if (target != null) {
            Class<?> targetClass = target.getClass();
            if (targetClass == LocalDateTime.class) {
                LocalDateTime localDateTime = (LocalDateTime) target;
                target = TimeZoneUtil.convertLocalDateTime(localDateTime, formZoneId, toZoneId);
            } else if (targetClass == LocalDate.class) {
                LocalDate localDate = (LocalDate) target;
                target = TimeZoneUtil.convertLocalDate(localDate, formZoneId, toZoneId);
            } else if (targetClass == String.class) {
                target = TimeZoneUtil.convertString(String.valueOf(target), format, formZoneId, toZoneId);
            } else if (List.class.isAssignableFrom(targetClass)) {
                ((List<?>) target).forEach(e -> convert(e, format, formZoneId, toZoneId));
            } else if (Map.class.isAssignableFrom(targetClass)) {
                Map<String, Object> map = (Map<String, Object>) target;
                target = convertMap(map, format, formZoneId, toZoneId);
            } else if (PageVO.class.isAssignableFrom(targetClass)) {
                PageVO<?> page = (PageVO<?>) target;
                page.getRecords().forEach(e -> convert(e, format, formZoneId, toZoneId));
            } else {
                TimeZoneUtil.convertBean(target, formZoneId, toZoneId);
            }
        }
        return target;
    }

    /**
     * Bean转换
     *
     * @param target
     * @param formZoneId
     * @param toZoneId
     */
    public void convertBean(Object target, ZoneId formZoneId, ZoneId toZoneId) {
        try {
            Class<?> targetClass = target.getClass();
            // 用户包的Bean才会做处理
            if (!targetClass.getName().contains("com.money")) {
                return;
            }
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                TZParam tzParam = field.getAnnotation(TZParam.class);
                if (tzParam != null) {
                    String format = tzParam.format();
                    Class<? extends TimeZoneConverter> converter = tzParam.converter();
                    if (converter != DefaultTimeZoneConverter.class) {
                        field.set(target, converter.newInstance().convert(field.get(target), format, formZoneId, toZoneId));
                    }
                    Class<?> type = field.getType();
                    if (field.get(target) != null) {
                        if (type == LocalDateTime.class) {
                            LocalDateTime localDateTime = (LocalDateTime) field.get(target);
                            field.set(target, convertLocalDateTime(localDateTime, formZoneId, toZoneId));
                        } else if (type == LocalDate.class) {
                            LocalDate localDate = (LocalDate) field.get(target);
                            field.set(target, convertLocalDate(localDate, formZoneId, toZoneId));
                        } else if (type == String.class) {
                            String dateTimeStr = String.valueOf(field.get(target));
                            field.set(target, convertString(dateTimeStr, format, formZoneId, toZoneId));
                        } else if (List.class.isAssignableFrom(type)) {
                            ((List<?>) field.get(target)).forEach(e -> convert(e, format, formZoneId, toZoneId));
                        } else if (Map.class.isAssignableFrom(targetClass)) {
                            Map<String, Object> map = (Map<String, Object>) field.get(target);
                            field.set(target, convertMap(map, format, formZoneId, toZoneId));
                        } else {
                            convertBean(field.get(target), formZoneId, toZoneId);
                        }
                    }
                }
            }
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("时区转换异常", e);
            throw new RuntimeException("时区转换异常");
        }
    }

    /**
     * String转换
     *
     * @param dateTimeStr
     * @param format
     * @param formZoneId
     * @param toZoneId
     * @return {@link String}
     */
    public String convertString(String dateTimeStr, String format, ZoneId formZoneId, ZoneId toZoneId) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
        } catch (Exception e) {
            return dateTimeStr;
        }
        return localDateTime.atZone(formZoneId).withZoneSameInstant(toZoneId).format(dateTimeFormatter);
    }

    /**
     * LocalDate转换
     *
     * @param localDate
     * @param formZoneId
     * @param toZoneId
     * @return {@link LocalDate}
     */
    public LocalDate convertLocalDate(LocalDate localDate, ZoneId formZoneId, ZoneId toZoneId) {
        return localDate.atTime(LocalTime.now()).atZone(formZoneId).withZoneSameInstant(toZoneId).toLocalDate();
    }

    /**
     * LocalDateTime转换
     *
     * @param localDateTime
     * @param formZoneId
     * @param toZoneId
     * @return {@link LocalDateTime}
     */
    public LocalDateTime convertLocalDateTime(LocalDateTime localDateTime, ZoneId formZoneId, ZoneId toZoneId) {
        return localDateTime.atZone(formZoneId).withZoneSameInstant(toZoneId).toLocalDateTime();
    }

    /**
     * Map转换
     *
     * @param map
     * @param format
     * @param formZoneId
     * @param toZoneId
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    public Map<String, Object> convertMap(Map<String, Object> map, String format, ZoneId formZoneId, ZoneId toZoneId) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                if (entry.getValue().getClass() == String.class) {
                    if (MAP_KEY.stream().anyMatch(k -> entry.getKey().toLowerCase().contains(k))) {
                        map.put(entry.getKey(), convertString(String.valueOf(entry.getValue()), format, formZoneId, toZoneId));
                    }
                } else {
                    map.put(entry.getKey(), convert(entry.getValue(), format, formZoneId, toZoneId));
                }
            }
        }
        return map;
    }
}