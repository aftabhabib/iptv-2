package com.iptv.core.hls.playlist.datatype;

import com.iptv.core.hls.exception.MalformedFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 引用字符串
 */
public final class QuotedString {
    private static final Pattern REGEX_FORMAT = Pattern.compile("^\"(.+?)\"$");

    /**
     * 读
     */
    public static String read(String value) throws MalformedFormatException {
        if (!isValidFormat(value)) {
            throw new MalformedFormatException("should be within a pair of double quotes");
        }

        return value.substring(1, value.length() - 1);
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
    public static String write(String value) {
        return String.format("\"%s\"", value);
    }
}
