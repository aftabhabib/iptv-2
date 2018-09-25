package com.iptv.core.hls.playlist.attribute;

import com.iptv.core.hls.exception.MalformedFormatException;
import com.iptv.core.hls.playlist.datatype.HexadecimalSequence;
import com.iptv.core.hls.playlist.datatype.QuotedString;
import com.iptv.core.hls.playlist.datatype.Resolution;

/**
 * 属性
 */
public final class Attribute {
    private String mName;
    private String mValue;

    /**
     * 构造函数
     */
    private Attribute(String name, String value) {
        mName = name;
        mValue = value;
    }

    /**
     * 获取属性名
     */
    public String getName() {
        return mName;
    }

    /**
     * 获取整型属性值
     */
    public int getIntegerValue() throws MalformedFormatException {
        try {
            return Integer.parseInt(mValue);
        }
        catch (NumberFormatException e) {
            throw new MalformedFormatException("should be decimal integer");
        }
    }

    /**
     * 获取长整型属性值
     */
    public long getLongValue() throws MalformedFormatException {
        try {
            return Long.parseLong(mValue);
        }
        catch (NumberFormatException e) {
            throw new MalformedFormatException("should be decimal integer");
        }
    }

    /**
     * 获取浮点型属性值
     */
    public float getFloatValue() throws MalformedFormatException {
        try {
            return Float.parseFloat(mValue);
        }
        catch (NumberFormatException e) {
            throw new MalformedFormatException("should be decimal float");
        }
    }

    /**
     * 获取16进制序列型属性值
     */
    public byte[] getHexadecimalSequenceValue() throws MalformedFormatException {
        return HexadecimalSequence.read(mValue);
    }

    /**
     * 获取枚举字符串型属性值
     */
    public String getEnumeratedStringValue() {
        return mValue;
    }

    /**
     * 获取引用字符串型属性值
     */
    public String getQuotedStringValue() throws MalformedFormatException {
        return QuotedString.read(mValue);
    }

    /**
     * 获取分辨率型属性值
     */
    public Resolution getResolutionValue() throws MalformedFormatException {
        return Resolution.valueOf(mValue);
    }

    @Override
    public String toString() {
        return mName + "=" + mValue;
    }

    /**
     * 根据字符串创建属性
     */
    public static Attribute parse(String strAttribute) throws MalformedFormatException {
        String[] result = strAttribute.split("=");
        if (result.length != 2) {
            throw new MalformedFormatException("should be <name>=<value>");
        }

        return new Attribute(result[0], result[1]);
    }

    /**
     * 创建属性
     */
    public static Attribute create(String name, int value) {
        return new Attribute(name, String.valueOf(value));
    }

    /**
     * 创建属性
     */
    public static Attribute create(String name, long value) {
        return new Attribute(name, String.valueOf(value));
    }

    /**
     * 创建属性
     */
    public static Attribute create(String name, float value) {
        return new Attribute(name, String.valueOf(value));
    }

    /**
     * 创建属性
     */
    public static Attribute create(String name, byte[] value) {
        return new Attribute(name, HexadecimalSequence.write(value));
    }

    /**
     * 创建属性
     */
    public static Attribute create(String name, String value) {
        return new Attribute(name, value);
    }

    /**
     * 创建属性
     */
    public static Attribute create(String name, QuotedString value) {
        return new Attribute(name, value.toString());
    }

    /**
     * 创建属性
     */
    public static Attribute create(String name, Resolution value) {
        return new Attribute(name, value.toString());
    }

    /**
     * 属性名
     */
    public static class Name {
        public static final String METHOD = "METHOD";
        public static final String IV = "IV";
        public static final String URI = "URI";
        public static final String KEY_FORMAT = "KEYFORMAT";
        public static final String KEY_FORMAT_VERSIONS = "KEYFORMATVERSIONS";
        public static final String TYPE = "TYPE";
        public static final String GROUP_ID = "GROUP-ID";
        public static final String LANGUAGE = "LANGUAGE";
        public static final String ASSOCIATED_LANGUAGE = "ASSOC-LANGUAGE";
        public static final String NAME = "NAME";
        public static final String DEFAULT = "DEFAULT";
        public static final String AUTO_SELECT = "AUTOSELECT";
        public static final String FORCED = "FORCED";
        public static final String IN_STREAM_ID = "INSTREAM-ID";
        public static final String CHARACTERISTICS = "CHARACTERISTICS";
        public static final String CHANNELS = "CHANNELS";
        public static final String BANDWIDTH = "BANDWIDTH";
        public static final String AVG_BANDWIDTH = "AVERAGE-BANDWIDTH";
        public static final String CODECS = "CODECS";
        public static final String RESOLUTION = "RESOLUTION";
        public static final String FRAME_RATE = "FRAME-RATE";
        public static final String HDCP_LEVEL = "HDCP-LEVEL";
        public static final String AUDIO = "AUDIO";
        public static final String VIDEO = "VIDEO";
        public static final String SUBTITLES = "SUBTITLES";
        public static final String CLOSED_CAPTIONS = "CLOSED-CAPTIONS";
        public static final String BYTE_RANGE = "BYTERANGE";

        /**
         * 构造函数（私有属性，不允许创建实例）
         */
        private Name() {
            /**
             * nothing
             */
        }
    }
}
