package com.money.cache.local.hutool;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import com.money.cache.local.LocalCacheService;
import lombok.Data;

import java.util.List;
import java.util.Locale;

/**
 * @author : money
 * @version : 1.0.0
 * @description : hutool缓存
 * @createTime : 2021-09-21 12:25:40
 */
@Data
public class HutoolCacheService implements LocalCacheService {

    /**
     * 缓存
     */
    private Cache<String, Object> cache;

    private long ttl;

    public HutoolCacheService(HutoolCacheProperties properties, long ttl) {
        this.cache = this.newCache(properties);
        this.ttl = ttl;
    }

    private Cache<String, Object> newCache(HutoolCacheProperties properties) {
        String strategy = properties.getStrategy().toUpperCase(Locale.ROOT);
        int capacity = properties.getCapacity();
        switch (strategy) {
            case "FIFO": return CacheUtil.newFIFOCache(capacity, ttl);
            case "LFU": return CacheUtil.newLFUCache(capacity, ttl);
            case "TIMED": return CacheUtil.newTimedCache(ttl);
            case "WEAK": return CacheUtil.newWeakCache(ttl);
            case "LRU":
            default:
                return CacheUtil.newLRUCache(capacity, ttl);
        }
    }

    @Override
    public void set(String key, Object value, long time) {
        cache.put(key, value, time);
    }

    @Override
    public void set(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object get(String key) {
        return cache.get(key);
    }

    @Override
    public Boolean del(String key) {
        cache.remove(key);
        return true;
    }

    @Override
    public Long del(List<String> keys) {
        keys.forEach(cache::remove);
        return Long.valueOf(String.valueOf(keys.size()));
    }

    @Override
    public Boolean hasKey(String key) {
        return cache.containsKey(key);
    }
}
