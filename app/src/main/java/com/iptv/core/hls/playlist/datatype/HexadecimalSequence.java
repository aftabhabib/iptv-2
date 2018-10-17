package com.iptv.core.hls.playlist.datatype;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 16进制序列
 */
public final class HexadecimalSequence {
    private static final Pattern REGEX_FORMAT = Pattern.compile("^(0x|0X)[A-Za-z0-9]+$");

    private byte[] mValue;

    /**
     * 构造函数
     */
    public HexadecimalSequence(byte[] value) {
        mValue = value;
    }

    /**
     * 获取值
     */
    public byte[] toByteArray() {
        return mValue;
    }

    @Override
    public String toString() {
        BigInteger bigInteger = new BigInteger(mValue);
        return "0x" + bigInteger.toString(16);
    }

    /**
     * 来自字符串
     */
    public static HexadecimalSequence valueOf(String str) {
        if (!isValidFormat(str)) {
            throw new IllegalArgumentException(
                    "should be from the set [A-Za-z] or [0-9] that is prefixed with 0x or 0X");
        }

        BigInteger bigInteger = new BigInteger(str.substring(2), 16);
        return new HexadecimalSequence(bigInteger.toByteArray());
    }

    /**
     * 是否有效的格式
     */
    private static boolean isValidFormat(String str) {
        Matcher matcher = REGEX_FORMAT.matcher(str);
        return matcher.find();
    }
}
