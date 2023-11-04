package com.money.oss.core;

import cn.hutool.core.util.RandomUtil;

import java.time.Instant;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 文件名称策略
 * @createTime : 2022-01-01 16:47:44
 */
public interface FileNameStrategy {
    /**
     * 原始
     */
    FileNameStrategy ORIGINAL = (rawName, fileType) -> rawName + "." + fileType;
    /**
     * 时间戳：高并发且业务简单的情况下时间戳会相同，应选择其他策略
     */
    FileNameStrategy TIMESTAMP = (rawName, fileType) -> getTimestamp() + "." + fileType;
    /**
     * 时间戳 + 3位随机字符
     */
    FileNameStrategy TIMESTAMP_H = (rawName, fileType) -> getTimestamp() + RandomUtil.randomString(3) + "." + fileType;
    /**
     * 原始与时间戳
     */
    FileNameStrategy ORIGINAL_WITH_TIMESTAMP = (rawName, fileType) -> rawName + getTimestamp() + "." + fileType;

    /**
     * 应用生成文件名
     *
     * @param rawName  原始名字
     * @param fileType 文件类型
     * @return {@link String}
     */
    String apply(String rawName, String fileType);

    static String getTimestamp() {
        return String.valueOf(Instant.now().toEpochMilli());
    }
}