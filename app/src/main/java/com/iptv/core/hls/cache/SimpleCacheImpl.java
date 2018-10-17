package com.iptv.core.hls.cache;

/**
 * 缓存模块的一个简单实现
 */
final class SimpleCacheImpl implements Cache {
    private int mLimit;

    /**
     * 构造函数
     */
    public SimpleCacheImpl(int limit) {
        mLimit = limit;
    }

    @Override
    public boolean contains(String uri) {
        return false;
    }

    @Override
    public void put(String uri, byte[] data) {

    }

    @Override
    public byte[] get(String uri) {
        return null;
    }
}
