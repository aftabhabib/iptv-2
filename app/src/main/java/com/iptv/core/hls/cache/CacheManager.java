package com.iptv.core.hls.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存管理
 */
public final class CacheManager {
    private static CacheManager sInstance = null;

    private Map<String, Cache> mCacheTable = new HashMap<>();

    /**
     * 构造函数
     */
    private CacheManager() {
        /**
         * nothing
         */
    }

    /**
     * 获取缓存
     */
    public Cache getCache(String name) {
        if (!mCacheTable.containsKey(name)) {
            mCacheTable.put(name, new SimpleCacheImpl());
        }

        return mCacheTable.get(name);
    }

    /**
     * 获取实例
     */
    public static CacheManager getInstance() {
        if (sInstance == null) {
            sInstance = new CacheManager();
        }

        return sInstance;
    }
}
