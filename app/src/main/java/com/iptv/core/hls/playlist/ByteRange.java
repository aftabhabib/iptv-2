package com.iptv.core.hls.playlist;

import com.iptv.core.hls.exception.MalformedFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字节范围
 */
public final class ByteRange {
    private static final Pattern REGEX_FORMAT = Pattern.compile("^\\d+(@\\d+)?$");

    private long mOffset;
    private long mLength;

    /**
     * 构造函数
     */
    public ByteRange(long length) {
        this(-1, length);
    }

    /**
     * 构造函数
     */
    public ByteRange(long offset, long length) {
        mOffset = offset;
        mLength = length;
    }

    /**
     * 是否定义了偏移
     */
    public boolean containsOffset() {
        return mOffset >= 0;
    }

    /**
     * 获取长度
     */
    public long getLength() {
        return mLength;
    }

    /**
     * 获取偏移
     */
    public long getOffset() {
        if (!containsOffset()) {
            throw new IllegalStateException("no offset");
        }

        return mOffset;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(mLength);
        if (containsOffset()) {
            buffer.append("@");
            buffer.append(mOffset);
        }

        return buffer.toString();
    }

    /**
     * 来自字符串
     */
    public static ByteRange valueOf(String content) throws MalformedFormatException {
        if (isValidFormat(content)) {
            throw new MalformedFormatException("should be <n>[@<o>]");
        }

        String[] results = content.split("@");
        if (results.length == 1) {
            return new ByteRange(Long.parseLong(results[0]));
        }
        else {
            return new ByteRange(Long.parseLong(results[0]), Long.parseLong(results[1]));
        }
    }

    /**
     * 是否有效的格式
     */
    private static boolean isValidFormat(String content) {
        Matcher matcher = REGEX_FORMAT.matcher(content);
        return matcher.find();
    }
}
