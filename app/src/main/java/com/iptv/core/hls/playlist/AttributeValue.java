package com.iptv.core.hls.playlist;

import java.math.BigInteger;

/**
 * 属性值
 */
public class AttributeValue {
    /**
     * 读十进制的整数
     */
    public static int readDecimalInteger(String attributeValue) {
        return Integer.parseInt(attributeValue);
    }

    /**
     * 写十进制的整数
     */
    public static String writeDecimalInteger(int content) {
        return String.valueOf(content);
    }

    /**
     * 读十六进制的序列
     */
    public static byte[] readHexadecimalSequence(String attributeValue) {
        if (!attributeValue.startsWith("0x")
                && !attributeValue.startsWith("0X")) {
            throw new IllegalArgumentException("bad value");
        }

        return new BigInteger(attributeValue.substring(2), 16).toByteArray();
    }

    /**
     * 写十六进制的序列
     */
    public static String writeHexadecimalSequence(byte[] content) {
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
    public static float readDecimalFloatingPoint(String attributeValue) {
        return Float.parseFloat(attributeValue);
    }

    /**
     * 写十进制的浮点数
     */
    public static String writeDecimalFloatingPoint(float content) {
        return String.valueOf(content);
    }

    /**
     * 读使用双引号括起来的字符串（字符串里不能有双引号和换行符）
     */
    public static String readQuotedString(String attributeValue) {
        if (!attributeValue.startsWith("\"")
                && !attributeValue.endsWith("\"")) {
            throw new IllegalArgumentException("bad value");
        }

        return attributeValue.substring(1, attributeValue.length() - 1);
    }

    /**
     * 写使用双引号括起来的字符串（字符串里不能有双引号和换行符）
     */
    public static String writeQuotedString(String content) {
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
    public static String readEnumeratedString(String attributeValue) {
        if (attributeValue.contains("\"")
                || attributeValue.contains(",")
                || attributeValue.contains(" ")) {
            throw new IllegalArgumentException("bad value");
        }

        return attributeValue;
    }

    /**
     * 写枚举字符串（字符串里不能有双引号、逗号和空格）
     */
    public static String writeEnumeratedString(String content) {
        if (content.contains("\"")
                || content.contains(",")
                || content.contains(" ")) {
            throw new IllegalArgumentException("bad value");
        }

        return content;
    }

    /**
     * 读分辨率（使用“x”分隔的两个整数）
     */
    public static Resolution readDecimalResolution(String attributeValue) {
        if (!attributeValue.contains("x")) {
            throw new IllegalArgumentException("bad value");
        }

        String[] result = attributeValue.split("x");
        if (result.length != 2) {
            throw new IllegalArgumentException("bad value");
        }

        return new Resolution(Integer.parseInt(result[0]), Integer.parseInt(result[1]));
    }

    /**
     * 写分辨率（使用“x”分隔的两个整数）
     */
    public static String writeDecimalResolution(Resolution resolution) {
        return String.valueOf(resolution.getWidth())
                + "x"
                + String.valueOf(resolution.getHeight());
    }

    /**
     * 构造函数（私有属性，不允许创建实例）
     */
    private AttributeValue() {
        /**
         * nothing
         */
    }

    /**
     * 视频图像分辨率
     */
    public static class Resolution {
        private int mWidth;
        private int mHeight;

        /**
         * 构造函数
         */
        public Resolution(int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        /**
         * 获取视频图像宽
         */
        public int getWidth() {
            return mWidth;
        }

        /**
         * 获取视频图像高
         */
        public int getHeight() {
            return mHeight;
        }
    }
}
