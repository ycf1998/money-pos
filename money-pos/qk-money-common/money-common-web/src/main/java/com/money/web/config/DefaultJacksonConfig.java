package com.money.web.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
 * 默认 Jackson 配置
 *
 * @author : money
 * @since : 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class DefaultJacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                // 字段值为 null 时不返回该字段
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .locale(Locale.CHINA)
                .timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
                .dateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN))
                // 雪花 ID 长 19 位，而 JS Number 类型的精度只有 16 位，所以长度超过 16 位的 Long 类型转为 String 返回
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
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, DeserializationFeature.ACCEPT_FLOAT_AS_INT);
    }
}