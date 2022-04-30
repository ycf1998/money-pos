package com.money.common.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 默认Jackson配置
 * @createTime : 2022-01-01 13:45:57
 */
@Configuration(proxyBeanMethods = false)
public class DefaultJacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                // 不序列化null值，字段为null时返回json不返回该字段
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .locale(Locale.CHINA)
                .timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
                .dateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN))
                // 雪花ID长19位，超过js Number精度只有16位，所以当长度超过16位的Long类型转为String返回
                .serializerByType(Long.class, new JsonSerializer<Long>() {
                    @Override
                    public void serialize(Long aLong, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                        String value = aLong.toString();
                        if (value.length() > 16) {
                            jsonGenerator.writeString(value);
                        } else {
                            jsonGenerator.writeNumber(aLong);
                        }
                    }
                })
                .serializerByType(LocalDateTime.class,
                        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
                .deserializerByType(LocalDateTime.class,
                        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}