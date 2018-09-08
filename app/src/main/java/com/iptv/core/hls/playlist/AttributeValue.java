package com.iptv.core.hls.playlist;

import java.math.BigInteger;

/**
 * 属性值
 */
public class AttributeValue {
    /**
     * an integer in base-10 arithmetic in the range from 0 to 2^64-1
     */
    public static int toDecimalInteger(String attributeValue) {
        return Integer.parseInt(attributeValue);
    }

    /**
     *
     */
    public static byte[] toHexadecimalSequence(String attributeValue) {
        if (!attributeValue.startsWith("0x")
                && !attributeValue.startsWith("0X")) {
            throw new IllegalArgumentException("bad value");
        }

        return new BigInteger(attributeValue.substring(2), 16).toByteArray();
    }

    /**
     * a non-negative floating-point number in decimal positional notation
     */
    public static float toDecimalFloatingPoint(String attributeValue) {
        return Float.parseFloat(attributeValue);
    }

    /**
     * a signed floating-point number in decimal positional notation
     */
    public static float toSignedDecimalFloatingPoint(String attributeValue) {
        return Float.parseFloat(attributeValue);
    }

    /**
     * a string of characters within a pair of double quotes
     * MUST NOT appear: line feed (0xA), carriage return (0xD), or double quote (0x22)
     */
    public static String toQuotedString(String attributeValue) {
        return attributeValue.substring(1, attributeValue.length() - 1);
    }

    /**
     * an unquoted character string
     * never contain double quotes ("), commas (,), or whitespace
     */
    public static String toEnumeratedString(String attributeValue) {
        return attributeValue;
    }

    /**
     * two decimal-integers separated by the "x" character
     */
    public static Resolution toDecimalResolution(String attributeValue) {
        if (!attributeValue.contains("x")) {
            throw new IllegalArgumentException("bad value");
        }

        String[] result = attributeValue.split("x");
        if (result.length != 2) {
            throw new IllegalArgumentException("bad value");
        }

        return new Resolution(Integer.parseInt(result[0]), Integer.parseInt(result[1]));
    }
}
