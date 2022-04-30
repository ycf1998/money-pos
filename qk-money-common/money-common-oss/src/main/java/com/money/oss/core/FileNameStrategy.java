package com.money.oss.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.function.BiFunction;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 文件名称策略
 * @createTime : 2022-01-01 16:47:44
 */
@AllArgsConstructor
@Getter
public enum FileNameStrategy {
    /**
     * 原始
     */
    ORIGINAL((rawName, fileType) -> rawName + "." + fileType),
    /**
     * 时间戳
     */
    TIMESTAMP((rawName, fileType) -> getTimestamp() + "." + fileType),
    /**
     * 原始与时间戳
     */
    ORIGINAL_WITH_TIMESTAMP((rawName, fileType) -> rawName + getTimestamp() + "." + fileType);

    /**
     * 策略
     */
    private final BiFunction<String, String, String> strategy;

    /**
     * 应用生成文件名
     *
     * @param rawName  原始名字
     * @param fileType 文件类型
     * @return {@link String}
     */
    public String apply(String rawName, String fileType) {
        return getStrategy().apply(rawName, fileType);
    }

    private static String getTimestamp() {
        return String.valueOf(Instant.now().toEpochMilli());
    }
}