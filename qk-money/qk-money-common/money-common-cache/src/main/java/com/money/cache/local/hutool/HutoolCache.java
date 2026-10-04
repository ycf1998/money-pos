package com.money.cache.local.hutool;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.money.cache.local.LocalCache;
import lombok.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Hutool 缓存
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
public class HutoolCache implements LocalCache {

    /**
     * 配置
     */
    private HutoolCacheProperties properties;

    /**
     * 缓存
     */
    private Cache<String, Object> cache;

    public HutoolCache(HutoolCacheProperties properties) {
        this.cache = this.createCache(properties);
    }

    private Cache<String, Object> createCache(HutoolCacheProperties properties) {
        String strategy = properties.getStrategy().toUpperCase(Locale.ROOT);
        int capacity = properties.getCapacity();
        long ttl = properties.getTtl();
        return switch (strategy) {
            case "FIFO" -> CacheUtil.newFIFOCache(capacity, ttl);
            case "LFU" -> CacheUtil.newLFUCache(capacity, ttl);
            case "TIMED" -> CacheUtil.newTimedCache(ttl);
            case "WEAK" -> CacheUtil.newWeakCache(ttl);
            // 默认使用 LRU
            default -> CacheUtil.newLRUCache(capacity, ttl);
        };
    }


    @Override
    public int capacity() {
        return properties.getCapacity();
    }

    @Override
    public long timeout() {
        return properties.getTtl();
    }

    @Override
    public Object get(String key) {
        return cache.get(key);
    }

    @Override
    public Map<String, Object> getAll(Collection<String> keys) {
        if (CollUtil.isEmpty(keys)) {
            return MapUtil.newHashMap();
        }
        Map<String, Object> map = new HashMap<>(keys.size());
        keys.forEach(key -> map.put(key, cache.get(key)));
        return map;
    }

    @Override
    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    @Override
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void putAll(Map<String, Object> map) {
        map.forEach((key, value) -> cache.put(key, value));
    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        if (containsKey(key)) {
            return cache.get(key);
        }
        cache.put(key, value);
        return null;
    }

    @Override
    public boolean remove(String key) {
        if (containsKey(key)) {
            cache.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public Object getAndRemove(String key) {
        if (containsKey(key)) {
            Object value = cache.get(key);
            cache.remove(key);
            return value;
        }
        return null;
    }

    @Override
    public void removeAll(Collection<? extends String> keys) {
        if (CollUtil.isEmpty(keys)) {
            return;
        }
        keys.forEach(key -> cache.remove(key));
    }

    @Override
    public void removeAll() {
        cache.clear();
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
