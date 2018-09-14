package com.iptv.core.hls.playlist.datatype;

import com.iptv.core.hls.exception.MalformedFormatException;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 16进制序列
 */
public final class HexadecimalSequence {
    private static final Pattern REGEX_FORMAT = Pattern.compile("^(0x|0X)[A-Za-z0-9]+$");

    /**
     * 读
     */
    public static byte[] read(String value) throws MalformedFormatException {
        if (!isValidFormat(value)) {
            throw new MalformedFormatException("should be prefixed with 0x or 0X");
        }

        BigInteger bigInteger = new BigInteger(value.substring(2), 16);
        return bigInteger.toByteArray();
    }

    /**
     * 是否有效的格式
     */
    private static boolean isValidFormat(String content) {
        Matcher matcher = REGEX_FORMAT.matcher(content);
        return matcher.find();
    }

    /**
     * 写
     */
    public static String write(byte[] value) {
        BigInteger bigInteger = new BigInteger(value);
        return "0x" + bigInteger.toString(16);
    }
}
