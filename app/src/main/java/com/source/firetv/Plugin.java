package com.source.firetv;

import java.util.Map;

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
     * 处理
     */
    String process(String url, Map<String, String> property);
}
