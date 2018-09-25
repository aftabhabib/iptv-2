package com.iptv.core.hls.playlist.datatype;

import com.iptv.core.hls.exception.MalformedFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 引用字符串
 */
public final class QuotedString {
    private static final Pattern REGEX_FORMAT = Pattern.compile("^\"(.+?)\"$");

    private String mContent;

    /**
     * 构造函数
     */
    public QuotedString(String[] parts, char separator) {
        StringBuffer buffer = new StringBuffer();

        boolean isFirstPart = true;
        for (String part : parts) {
            if (!isFirstPart) {
                buffer.append(separator);
            }

            buffer.append(part);

            if (isFirstPart) {
                isFirstPart = false;
            }
        }

        mContent = buffer.toString();
    }

    /**
     * 构造函数
     */
    public QuotedString(String content) {
        mContent = content;
    }

    /**
     * 获取内容
     */
    public String getContent() {
        return mContent;
    }

    /**
     * 根据指定的分隔符拆分内容
     */
    public String[] splitContent(String regex) {
        return mContent.split(regex);
    }

    @Override
    public String toString() {
        return "\"" + mContent + "\"";
    }

    /**
     * 来自字符串
     */
    public static QuotedString valueOf(String str) throws MalformedFormatException {
        if (!isValidFormat(str)) {
            throw new MalformedFormatException("should be within a pair of double quotes");
        }

        return new QuotedString(str.substring(1, str.length() - 1));
    }

    /**
     * 是否有效的格式
     */
    private static boolean isValidFormat(String str) {
        Matcher matcher = REGEX_FORMAT.matcher(str);
        return matcher.find();
    }
}
