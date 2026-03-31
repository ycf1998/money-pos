package com.money.web.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Jackson 配置
 *
 * @author : money
 * @since : 1.0.0
 */
@Configuration
public class DefaultJacksonConfig {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                // 字段值为 null 时不返回该字段
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .locale(Locale.CHINA)
                .timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
                .dateFormat(new java.text.SimpleDateFormat(DATE_PATTERN))
                // 雪花 ID 长 19 位，而 JS Number 类型的精度只有 16 位，所以长度超过 16 位的 Long 类型转为 String 返回
                .serializerByType(Long.class, new com.fasterxml.jackson.databind.JsonSerializer<Long>() {
                    @Override
                    public void serialize(Long value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws java.io.IOException {
                        String str = value.toString();
                        if (str.length() > 16) {
                            gen.writeString(str);
                        } else {
                            gen.writeNumber(value);
                        }
                    }
                })
                .serializerByType(LocalDateTime.class,
                        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .deserializerByType(LocalDateTime.class,
                        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .featuresToDisable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        DeserializationFeature.ACCEPT_FLOAT_AS_INT
                );
    }
}
