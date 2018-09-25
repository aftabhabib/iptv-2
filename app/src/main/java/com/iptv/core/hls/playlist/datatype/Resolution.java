package com.iptv.core.hls.playlist.datatype;

import com.iptv.core.hls.exception.MalformedPlaylistException;

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
        return String.valueOf(mWidth) + "x" + String.valueOf(mHeight);
    }

    /**
     * 来自字符串
     */
    public static Resolution valueOf(String str)
            throws MalformedPlaylistException, NumberFormatException {
        if (!isValidFormat(str)) {
            throw new MalformedPlaylistException("should be two integers " +
                    "separated by the \"x\" character");
        }

        String[] result = str.split("x");
        return new Resolution(Integer.parseInt(result[0]), Integer.parseInt(result[1]));
    }

    /**
     * 是否有效的格式
     */
    private static boolean isValidFormat(String str) {
        Matcher matcher = REGEX_FORMAT.matcher(str);
        return matcher.find();
    }
}
