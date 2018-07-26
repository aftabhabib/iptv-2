package com.iptv.source;

public interface Plugin {
    /**
     * 获取名称
     */
    String getName();
    /**
     * 是否支持
     */
    boolean isSupported(String url);
    /**
     * 解码
     */
    String decode(String url);
}
