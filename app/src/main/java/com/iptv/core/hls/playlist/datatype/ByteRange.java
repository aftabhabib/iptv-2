package com.iptv.core.hls.playlist.datatype;

import com.iptv.core.hls.exception.MalformedPlaylistException;

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
     * 获取长度
     */
    public long getLength() {
        return mLength;
    }

    /**
     * 获取偏移
     */
    public long getOffset() {
        return mOffset;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(mLength);
        if (mOffset > 0) {
            buffer.append("@");
            buffer.append(mOffset);
        }

        return buffer.toString();
    }

    /**
     * 来自字符串
     */
    public static ByteRange valueOf(String str) throws MalformedPlaylistException {
        if (isValidFormat(str)) {
            throw new MalformedPlaylistException("should be <n>[@<o>]");
        }

        String[] results = str.split("@");
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
    private static boolean isValidFormat(String str) {
        Matcher matcher = REGEX_FORMAT.matcher(str);
        return matcher.find();
    }
}
