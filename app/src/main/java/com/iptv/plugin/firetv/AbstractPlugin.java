package com.iptv.plugin.firetv;

import com.iptv.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

abstract class AbstractPlugin implements Plugin {
    @Override
    public String decode(String url) {
        Map<String, String> property = new HashMap<String, String>();

        if (url.endsWith("#ad")) {
            /**
             * FIXME：可能与广告有关，删除
             */
            url = url.substring(0, url.length() - 3);
        }

        if (url.endsWith("jiema0")
                || url.endsWith("jiema1")
                || url.endsWith("jiema2")
                || url.endsWith("jiema3")
                || url.endsWith("jiema4")) {
            /**
             * FIXME：可能与解码有关，删除
             */
            url = url.substring(0, url.length() - 6);
        }

        if (url.contains("useragent")) {
            /**
             * HTTP头部的UserAgent字段
             */
            String[] results = url.split("useragent");

            url = results[0];
            property.put("UserAgent", results[1]);
        }

        if (url.contains("refer")) {
            /**
             * HTTP头部的Refer字段
             */
            String[] results = url.split("refer");

            url = results[0];
            property.put("Refer", results[1]);
        }

        if (url.contains("&amp;")) {
            /**
             * 转义字符串，替换
             */
            url = url.replaceAll("&amp;", "&");
        }

        return decode(url, property);
    }

    protected abstract String decode(String url, Map<String, String> property);
}
