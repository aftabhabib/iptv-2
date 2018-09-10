package com.iptv.core.hls.playlist;

import java.math.BigInteger;

/**
 * 属性值
 */
class AttributeValue {
    /**
     * “是/否”
     */
    static final String ENUM_STRING_YES = "YES";
    static final String ENUM_STRING_NO = "NO";
    /**
     * ”没有“
     */
    static final String ENUM_STRING_NONE = "NONE";

    /**
     * 读十进制的整数
     */
    static int readDecimalInteger(String attributeValue) {
        return Integer.parseInt(attributeValue);
    }

    /**
     * 写十进制的整数
     */
    static String writeDecimalInteger(int content) {
        return String.valueOf(content);
    }

    /**
     * 读十六进制的序列
     */
    static byte[] readHexadecimalSequence(String attributeValue) {
        if (!attributeValue.startsWith("0x")
                && !attributeValue.startsWith("0X")) {
            throw new IllegalArgumentException("bad value");
        }

        return new BigInteger(attributeValue.substring(2), 16).toByteArray();
    }

    /**
     * 写十六进制的序列
     */
    static String writeHexadecimalSequence(byte[] content) {
        StringBuffer buffer = new StringBuffer("0x");

        for (int i = 0; i < content.length; i++) {
            int value = content[i] & 0xff;

            if (value < 0x10) {
                buffer.append("0");
            }

            buffer.append(Integer.toHexString(value));
        }

        return buffer.toString();
    }

    /**
     * 读十进制的浮点数
     */
    static float readDecimalFloatingPoint(String attributeValue) {
        return Float.parseFloat(attributeValue);
    }

    /**
     * 写十进制的浮点数
     */
    static String writeDecimalFloatingPoint(float content) {
        return String.valueOf(content);
    }

    /**
     * 读使用双引号括起来的字符串（字符串里不能有双引号和换行符）
     */
    static String readQuotedString(String attributeValue) {
        if (!attributeValue.startsWith("\"")
                && !attributeValue.endsWith("\"")) {
            throw new IllegalArgumentException("bad value");
        }

        return attributeValue.substring(1, attributeValue.length() - 1);
    }

    /**
     * 写使用双引号括起来的字符串（字符串里不能有双引号和换行符）
     */
    static String writeQuotedString(String content) {
        if (content.contains("\"")
                || content.contains("\r")
                || content.contains("\n")) {
            throw new IllegalArgumentException("bad value");
        }

        return "\"" + content + "\"";
    }

    /**
     * 读枚举字符串（字符串里不能有双引号、逗号和空格）
     */
    static String readEnumeratedString(String attributeValue) {
        return attributeValue;
    }

    /**
     * 写枚举字符串（字符串里不能有双引号、逗号和空格）
     */
    static String writeEnumeratedString(String content) {
        return content;
    }

    /**
     * 读分辨率（使用“x”分隔的两个整数）
     */
    static VideoResolution readDecimalResolution(String attributeValue) {
        if (!attributeValue.contains("x")) {
            throw new IllegalArgumentException("bad value");
        }

        String[] result = attributeValue.split("x");
        if (result.length != 2) {
            throw new IllegalArgumentException("bad value");
        }

        return new VideoResolution(Integer.parseInt(result[0]), Integer.parseInt(result[1]));
    }

    /**
     * 写分辨率（使用“x”分隔的两个整数）
     */
    static String writeDecimalResolution(VideoResolution res) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(res.getWidth());
        buffer.append("x");
        buffer.append(res.getHeight());

        return buffer.toString();
    }

    /**
     * 构造函数（私有属性，不允许创建实例）
     */
    private AttributeValue() {
        /**
         * nothing
         */
    }
}
