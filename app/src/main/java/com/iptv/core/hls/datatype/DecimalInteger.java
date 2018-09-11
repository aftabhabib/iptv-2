package com.iptv.core.hls.datatype;

import java.math.BigInteger;

/**
 * 8字节长的无符号整数
 */
public final class DecimalInteger {
    private BigInteger mValue;

    /**
     * 构造函数
     */
    public DecimalInteger(int value) {
        this(BigInteger.valueOf(value));
    }

    /**
     * 构造函数
     */
    public DecimalInteger(long value) {
        this(BigInteger.valueOf(value));
    }

    /**
     * 构造函数
     */
    private DecimalInteger(BigInteger value) {
        mValue = value;
    }

    /**
     * 转为int型的值
     */
    public int intValue() {
        return mValue.intValue();
    }

    /**
     * 转为long型的值
     */
    public long longValue() {
        return mValue.longValue();
    }

    @Override
    public String toString() {
        return mValue.toString();
    }

    /**
     * 解析自字符串
     */
    public static DecimalInteger parse(String content) {
        BigInteger value = new BigInteger(content, 10);
        return new DecimalInteger(value);
    }
}
