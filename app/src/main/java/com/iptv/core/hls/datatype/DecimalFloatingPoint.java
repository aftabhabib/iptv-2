package com.iptv.core.hls.datatype;

public final class DecimalFloatingPoint {
    private Float mValue;

    /**
     * 构造函数
     */
    public DecimalFloatingPoint(float value) {
        this(new Float(value));
    }

    /**
     * 构造函数
     */
    private DecimalFloatingPoint(Float value) {
        mValue = value;
    }

    /**
     * 转为float型的值
     */
    public float floatValue() {
        return mValue.floatValue();
    }

    @Override
    public String toString() {
        return Float.toString(mValue);
    }

    /**
     * 解析自字符串
     */
    public static DecimalFloatingPoint parse(String content) {
        Float value = Float.parseFloat(content);
        return new DecimalFloatingPoint(value);
    }
}
