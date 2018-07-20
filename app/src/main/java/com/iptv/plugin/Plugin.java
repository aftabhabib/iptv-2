package com.iptv.plugin;

public interface Plugin {
    /**
     * 名称
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
