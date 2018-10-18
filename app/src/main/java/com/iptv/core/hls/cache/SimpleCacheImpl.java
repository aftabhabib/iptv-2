package com.iptv.core.hls.cache;

import java.util.AbstractMap;

/**
 * 简单的缓存实现
 */
final class SimpleCacheImpl implements Cache {
    private AbstractMap.SimpleEntry<String, byte[]> mEntry = null;

    /**
     * 构造函数
     */
    public SimpleCacheImpl() {
        /**
         * nothing
         */
    }

    @Override
    public boolean contains(String uri) {
        if (mEntry != null) {
            return mEntry.getKey().equals(uri);
        }

        return false;
    }

    @Override
    public void put(String uri, byte[] data) {
        mEntry = new AbstractMap.SimpleEntry<>(uri, data);
    }

    @Override
    public byte[] get(String uri) {
        if (contains(uri)) {
            return mEntry.getValue();
        }

        return null;
    }
}
