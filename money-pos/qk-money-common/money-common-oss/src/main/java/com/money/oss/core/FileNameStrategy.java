package com.money.oss.core;

import cn.hutool.core.util.IdUtil;
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
     * 时间戳 + 3 位随机数（减少高并发下的碰撞）
     */
    FileNameStrategy TIMESTAMP = (rawName, fileType) -> getTimestamp() + RandomUtil.randomString(3) + "." + fileType;
    /**
     * 雪花 ID
     */
    FileNameStrategy SNOWFLAKE_ID = (rawName, fileType) -> IdUtil.getSnowflakeNextIdStr() + "." + fileType;
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