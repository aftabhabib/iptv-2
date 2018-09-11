package com.iptv.core.hls.datatype;

public final class QuotedString {
    private String mValue;

    /**
     * 构造函数
     */
    public QuotedString(String value) {
        mValue = value;
    }

    /**
     * 构造函数
     */
    public QuotedString(int[] value, String separator) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < value.length; i++) {
            if (i > 0) {
                buffer.append(separator);
            }

            buffer.append(String.valueOf(value[i]));
        }

        mValue = buffer.toString();
    }

    /**
     * 构造函数
     */
    public QuotedString(String[] value, String separator) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < value.length; i++) {
            if (i > 0) {
                buffer.append(separator);
            }

            buffer.append(value[i]);
        }

        mValue = buffer.toString();
    }

    /**
     * 值
     */
    public String value() {
        return mValue;
    }

    /**
     * 根据指定的分隔符分割
     */
    public String[] splitValue(String regex) {
        return mValue.split(regex);
    }

    /**
     * 根据指定的分隔符分割，再转为int型
     */
    public int[] splitValueToIntArray(String regex) {
        String[] src = splitValue(regex);
        int[] dst = new int[src.length];

        for (int i = 0; i < src.length; i++) {
            dst[i] = Integer.parseInt(src[i]);
        }

        return dst;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("\"");
        buffer.append(mValue);
        buffer.append("\"");

        return buffer.toString();
    }

    /**
     * 解析自字符串
     */
    public static QuotedString parse(String content) {
        if (!content.startsWith("\"")
                && !content.endsWith("\"")) {
            throw new IllegalArgumentException("should be in a pair of double quotes");
        }

        String value = content.substring(1, content.length() - 1);
        return new QuotedString(value);
    }
}
