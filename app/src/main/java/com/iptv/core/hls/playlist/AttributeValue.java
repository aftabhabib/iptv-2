package com.iptv.core.hls.playlist;

import java.math.BigInteger;

/**
 * 属性值
 */
public class AttributeValue {
    /**
     * an integer in base-10 arithmetic in the range from 0 to 2^64-1
     */
    public static int readDecimalInteger(String attributeValue) {
        return Integer.parseInt(attributeValue);
    }

    public static String writeDecimalInteger(int content) {
        return String.valueOf(content);
    }

    /**
     *
     */
    public static byte[] readHexadecimalSequence(String attributeValue) {
        if (!attributeValue.startsWith("0x")
                && !attributeValue.startsWith("0X")) {
            throw new IllegalArgumentException("bad value");
        }

        return new BigInteger(attributeValue.substring(2), 16).toByteArray();
    }

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
     * a floating-point number in decimal positional notation
     */
    public static float readDecimalFloatingPoint(String attributeValue) {
        return Float.parseFloat(attributeValue);
    }

    public static String writeDecimalFloatingPoint(float content) {
        return String.valueOf(content);
    }

    /**
     * a string of characters within a pair of double quotes
     * MUST NOT appear: line feed (0xA), carriage return (0xD), or double quote (0x22)
     */
    public static String readQuotedString(String attributeValue) {
        return attributeValue.substring(1, attributeValue.length() - 1);
    }

    public static String writeQuotedString(String content) {
        return "\"" + content + "\"";
    }

    /**
     * an unquoted character string
     * never contain double quotes ("), commas (,), or whitespace
     */
    public static String readEnumeratedString(String attributeValue) {
        return attributeValue;
    }

    public static String writeEnumeratedString(String content) {
        return content;
    }

    /**
     * two decimal-integers separated by the "x" character
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
}
