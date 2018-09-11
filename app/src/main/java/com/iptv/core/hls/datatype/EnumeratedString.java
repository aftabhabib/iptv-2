package com.iptv.core.hls.datatype;

import com.iptv.core.hls.playlist.Attribute;

public final class EnumeratedString {
    private String mValue;

    /**
     * 构造函数
     */
    public EnumeratedString(String value) {
        mValue = value;
    }

    /**
     * 构造函数
     */
    public EnumeratedString(boolean value) {
        mValue = value ? Attribute.VALUE_YES : Attribute.VALUE_NO;
    }

    /**
     * 值
     */
    public String value() {
        return mValue;
    }

    /**
     * 转为布尔型的值
     */
    public boolean toBoolean() {
        if (mValue.equals(Attribute.VALUE_YES)) {
            return true;
        }
        else if (mValue.equals(Attribute.VALUE_NO)) {
            return false;
        }
        else {
            throw new IllegalStateException("only YES or NO");
        }
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
