package com.money.cache.local;

import java.util.Collection;
import java.util.Map;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 本地缓存服务
 * @createTime : 2021-09-29 15:13:03
 */
public interface LocalCache {

    /**
     * 返回缓存容量，{@code 0}表示无大小限制
     *
     * @return 返回缓存容量，{@code 0}表示无大小限制
     */
    int capacity();

    /**
     * 缓存失效时长， {@code 0} 表示没有设置，单位毫秒
     *
     * @return 缓存失效时长， {@code 0} 表示没有设置，单位毫秒
     */
    long timeout();

    /**
     * 从缓存中获取与指定键相关联的值。
     *
     * @param key 键
     * @return {@link Object} 与指定键相关联的值，如果键不存在则返回null
     */
    Object get(String key);

    /**
     * 从缓存中获取与指定键相关联的值。
     *
     * @param keys 键
     * @return {@link Map}<{@link String}, {@link Object}> 包含缓存中所有键值对映射关系的Map
     */
    Map<String, Object> getAll(Collection<String> keys);

    /**
     * 检查缓存中是否包含指定的键。
     *
     * @param key 键
     * @return boolean 如果缓存中包含指定的键，则返回true；否则返回false
     */
    boolean containsKey(String key);

    /**
     * 将指定的键值对存储到缓存中。
     * 如果缓存中已经存在相同的键，则会覆盖原有的值。
     *
     * @param key   键
     * @param value 值
     */
    void put(String key, Object value);

    /**
     * 将所有键值对存储到缓存中。
     *
     * @param map 包含要存储的键值对的Map
     */
    void putAll(Map<String, Object> map);

    /**
     * 将指定的键值对存储到缓存中，如果该键尚未存在。
     *
     * @param key   键
     * @param value 值
     * @return Object 如果该键之前不存在，则返回null；如果该键已存在，则返回之前与该键关联的值
     */
    Object putIfAbsent(String key, Object value);

    /**
     * 从缓存中移除与指定键相关联的值。
     *
     * @param key 键
     * @return boolean 如果成功移除键值对，则返回被移除的值；如果键不存在，则返回null
     */
    boolean remove(String key);

    /**
     * 从缓存中移除与指定键相关联的值，并获取旧值
     *
     * @param key 键
     * @return {@link Object} 如果成功移除键值对，则返回被移除的值；如果键不存在，则返回null
     */
    Object getAndRemove(String key);

    /**
     * 从缓存中移除与指定键相关联的值
     *
     * @param keys 键
     */
    void removeAll(Collection<? extends String> keys);

    /**
     * 从缓存中移除所有键值对。
     */
    void removeAll();

    /**
     * 清空缓存，移除其中所有的键值对。
     */
    void clear();
}
