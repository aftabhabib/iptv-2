package com.iptv.core.hls.playlist.datatype;

import com.iptv.core.hls.exception.MalformedFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 分辨率
 */
public final class Resolution {
    private static final Pattern REGEX_FORMAT = Pattern.compile("^\\d+x\\d+$");

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
     * 获取宽
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * 获取高
     */
    public int getHeight() {
        return mHeight;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(mWidth);
        buffer.append("x");
        buffer.append(mHeight);

        return buffer.toString();
    }

    /**
     * 来自字符串
     */
    public static Resolution valueOf(String content)
            throws MalformedFormatException, NumberFormatException {
        if (!isValidFormat(content)) {
            throw new MalformedFormatException("should be two integers " +
                    "separated by the \"x\" character");
        }

        String[] result = content.split("x");
        return new Resolution(Integer.parseInt(result[0]), Integer.parseInt(result[1]));
    }

    /**
     * 是否有效的格式
     */
    private static boolean isValidFormat(String content) {
        Matcher matcher = REGEX_FORMAT.matcher(content);
        return matcher.find();
    }
}
