package com.iptv.core.resource;

import java.util.Map;

/**
 * 插件
 */
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
     * 翻译
     */
    String translate(String url);

    /**
     * 获取属性
     */
    Map<String, String> getProperties();
}
