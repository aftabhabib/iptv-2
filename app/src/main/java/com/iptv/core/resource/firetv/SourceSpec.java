package com.iptv.core.resource.firetv;

import java.util.HashMap;
import java.util.Map;

final class SourceSpec {
    private static final String PROPERTY_AD = "#ad";
    private static final String PROPERTY_JIEMA = "jiema";
    private static final String PROPERTY_USERAGENT = "useragent";
    private static final String PROPERTY_REFER = "refer";

    private String mUrl;
    private Map<String, String> mProperties;

    /**
     * 构造函数
     */
    private SourceSpec(String url, Map<String, String> properties) {
        mUrl = url;
        mProperties = properties;
    }

    /**
     * 获取url
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * 获取属性
     */
    public Map<String, String> getProperties() {
        return mProperties;
    }

    /**
     * 解码
     */
    public static SourceSpec decode(String source) {
        String url = source;
        Map<String, String> properties = new HashMap<String, String>();

        if (url.contains(PROPERTY_AD)) {
            /**
             * 广告，源代码中没有与之相关的定义
             */
            String[] results = url.split(PROPERTY_AD);

            url = results[0];
        }

        if (url.contains(PROPERTY_JIEMA)) {
            /**
             * 解码类型，源代码中会创建与之相关的播放器
             */
            String[] results = url.split(PROPERTY_JIEMA);

            url = results[0];
        }

        if (url.contains(PROPERTY_USERAGENT)) {
            /**
             * HTTP/HTTPS协议，请求头部中的User-Agent字段
             */
            String[] results = url.split(PROPERTY_USERAGENT);

            url = results[0];
            properties.put("User-Agent", results[1]);
        }

        if (url.contains(PROPERTY_REFER)) {
            /**
             * HTTP/HTTPS协议，请求头部中的Refer字段
             */
            String[] results = url.split(PROPERTY_REFER);

            url = results[0];
            properties.put("Refer", results[1]);
        }

        if (url.contains("&amp;")) {
            /**
             * HTML语法中的转义字符，替换
             */
            url = url.replaceAll("&amp;", "&");
        }

        return new SourceSpec(url, properties);
    }
}
