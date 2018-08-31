package com.iptv.core.utils;

public class StringUtils {
    /**
     * 是不是有效的字符串
     */
    public static boolean isValid(String str) {
        return (str != null) && !str.isEmpty();
    }

    /**
     * 构造函数（私有属性，不允许创建实例）
     */
    private StringUtils() {
        /**
         * nothing
         */
    }
}
