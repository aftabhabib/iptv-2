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
     * 值
     */
    public String value() {
        return mValue;
    }

    /**
     * 根据指定的分隔符分割为字符串数组
     */
    public String[] splitValue(String regex) {
        return mValue.split(regex);
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
