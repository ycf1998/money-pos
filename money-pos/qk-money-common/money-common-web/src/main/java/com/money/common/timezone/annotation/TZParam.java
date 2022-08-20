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
 * @description : 时区参数
 * 标注在方法入参上，表示需要进行时区转换
 * 当参数为对象时，会解析对象，对对象里标注了该注解的成员变量进行时区转换
 * @createTime : 2022-05-11 22:09:49
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TZParam {

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
