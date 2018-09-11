package com.iptv.core.hls.datatype;

public final class EnumeratedString {
    private String mValue;

    /**
     * 构造函数
     */
    public EnumeratedString(String value) {
        mValue = value;
    }

    /**
     * 值
     */
    public String value() {
        return mValue;
    }

    @Override
    public String toString() {
        return mValue;
    }

    /**
     * 解析自字符串
     */
    public static EnumeratedString parse(String content) {
        return new EnumeratedString(content);
    }
}
