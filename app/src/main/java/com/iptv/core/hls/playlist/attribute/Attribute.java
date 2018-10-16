package com.iptv.core.hls.playlist.attribute;

import com.iptv.core.hls.exception.MalformedPlaylistException;
import com.iptv.core.hls.exception.WrongDataTypeException;
import com.iptv.core.hls.playlist.datatype.HexadecimalSequence;
import com.iptv.core.hls.playlist.datatype.QuotedString;
import com.iptv.core.hls.playlist.datatype.Resolution;

/**
 * 属性
 */
public final class Attribute {
    private String mName;
    private Object mValue;

    /**
     * 构造函数
     */
    public Attribute(String name, Integer value) {
        mName = name;
        mValue = value;
    }

    /**
     * 构造函数
     */
    public Attribute(String name, Float value) {
        mName = name;
        mValue = value;
    }

    /**
     * 构造函数
     */
    public Attribute(String name, Long value) {
        mName = name;
        mValue = value;
    }

    /**
     * 构造函数
     */
    public Attribute(String name, String value) {
        mName = name;
        mValue = value;
    }

    /**
     * 构造函数
     */
    public Attribute(String name, HexadecimalSequence value) {
        mName = name;
        mValue = value;
    }

    /**
     * 构造函数
     */
    public Attribute(String name, QuotedString value) {
        mName = name;
        mValue = value;
    }

    /**
     * 构造函数
     */
    public Attribute(String name, Resolution value) {
        mName = name;
        mValue = value.toString();
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
    public Integer getIntegerValue() {
        if (mValue instanceof Integer) {
            return (Integer)mValue;
        }
        else {
            throw new WrongDataTypeException("not Integer type");
        }
    }

    /**
     * 获取长整型属性值
     */
    public Long getLongValue() {
        if (mValue instanceof Long) {
            return (Long)mValue;
        }
        else {
            throw new WrongDataTypeException("not Long type");
        }
    }

    /**
     * 获取浮点型属性值
     */
    public Float getFloatValue() {
        if (mValue instanceof Float) {
            return (Float)mValue;
        }
        else {
            throw new WrongDataTypeException("not Float type");
        }
    }

    /**
     * 获取字符串型属性值
     */
    public String getStringValue() {
        if (mValue instanceof String) {
            return (String)mValue;
        }
        else {
            throw new WrongDataTypeException("not String type");
        }
    }

    /**
     * 获取16进制序列型属性值
     */
    public HexadecimalSequence getHexadecimalSequenceValue() {
        if (mValue instanceof HexadecimalSequence) {
            return (HexadecimalSequence)mValue;
        }
        else {
            throw new WrongDataTypeException("not HexadecimalSequence type");
        }
    }

    /**
     * 获取引用字符串型属性值
     */
    public QuotedString getQuotedStringValue() {
        if (mValue instanceof QuotedString) {
            return (QuotedString)mValue;
        }
        else {
            throw new WrongDataTypeException("not QuotedString type");
        }
    }

    /**
     * 获取分辨率型属性值
     */
    public Resolution getResolutionValue() {
        if (mValue instanceof Resolution) {
            return (Resolution)mValue;
        }
        else {
            throw new WrongDataTypeException("not Resolution type");
        }
    }

    @Override
    public String toString() {
        return mName + "=" + mValue.toString();
    }

    /**
     * 根据字符串创建属性
     */
    public static Attribute parse(String strAttribute) throws MalformedPlaylistException {
        String[] result = strAttribute.split("=");
        if (result.length != 2) {
            throw new MalformedPlaylistException("should be <name>=<value>");
        }

        return new Attribute(result[0], result[1]);
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
