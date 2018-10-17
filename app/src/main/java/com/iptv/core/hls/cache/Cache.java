package com.iptv.core.hls.cache;

/**
 * 缓存
 */
public interface Cache {
    /**
     * 是否包含uri对应的数据
     */
    boolean contains(String uri);

    /**
     * 放入uri对应的数据
     */
    void put(String uri, byte[] data);

    /**
     * 获取uri对应的数据
     */
    byte[] get(String uri);
}
