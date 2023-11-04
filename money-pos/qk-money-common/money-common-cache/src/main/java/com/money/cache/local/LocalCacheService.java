package com.money.cache.local;

import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 本地缓存服务
 * @createTime : 2021-09-29 15:13:03
 */
public interface LocalCacheService {
    /**
     * 保存属性
     */
    void set(String key, Object value, long time);

    /**
     * 保存属性
     */
    void set(String key, Object value);

    /**
     * 获取属性
     */
    Object get(String key);

    /**
     * 删除属性
     */
    Boolean del(String key);

    /**
     * 批量删除属性
     */
    Long del(List<String> keys);

    /**
     * 判断是否有该属性
     */
    Boolean hasKey(String key);
}
