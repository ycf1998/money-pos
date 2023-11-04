package com.money.common.timezone.annotation;

import cn.hutool.core.date.DatePattern;
import com.money.common.timezone.converter.DefaultTimeZoneConverter;
import com.money.common.timezone.converter.TimeZoneConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 时区返回。
 * 标注在类上：类中所有的方法返回值都需要进行时区转换
 * 标注在方法上：该方法的方法返回值都需要进行时区转换
 * 优先级：方法 > 类
 * @createTime : 2022-05-11 22:09:40
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TZRep {

    /**
     * 格式（针对String类型的时间，建议使用JDK8日期类）
     *
     * @return {@link String}
     */
    String format() default DatePattern.NORM_DATETIME_PATTERN;

    /**
     * 转换器
     *
     * @return {@link Class}<{@link ?} {@link extends} {@link TimeZoneConverter}>
     */
    Class<? extends TimeZoneConverter> converter() default DefaultTimeZoneConverter.class;
}
