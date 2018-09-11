package com.iptv.core.hls.datatype;

import java.math.BigInteger;

public final class HexadecimalSequence {
    private BigInteger mValue;

    /**
     * 构造函数
     */
    public HexadecimalSequence(byte[] value) {
        this(new BigInteger(value));
    }

    /**
     * 构造函数
     */
    private HexadecimalSequence(BigInteger value) {
        mValue = value;
    }

    /**
     * 转为字节数组
     */
    public byte[] toByteArray() {
        return mValue.toByteArray();
    }

    @Override
    public String toString() {
        return mValue.toString(16);
    }

    /**
     * 解析自字符串
     */
    public static HexadecimalSequence parse(String content) {
        if (!content.startsWith("0x")
                && !content.startsWith("0X")) {
            throw new IllegalArgumentException("should be prefixed with 0x or 0X");
        }

        BigInteger value = new BigInteger(content.substring(2), 16);
        return new HexadecimalSequence(value);
    }
}
