package com.iptv.core.hls.playlist;

final class Attribute {
    /**
     * Tag EXT-X-KEY
     */
    public static final String ATTR_METHOD = "METHOD";
    public static final String ATTR_IV = "IV";

    /**
     * Tag EXT-X-MEDIA
     */
    public static final String ATTR_TYPE = "TYPE";
    public static final String ATTR_GROUP_ID = "GROUP-ID";
    public static final String ATTR_LANGUAGE = "LANGUAGE";
    public static final String ATTR_NAME = "NAME";
    public static final String ATTR_DEFAULT = "DEFAULT";
    public static final String ATTR_AUTO_SELECT = "AUTOSELECT";

    /**
     * Tag EXT-X-KEY and EXT-X-MEDIA
     */
    public static final String ATTR_URI = "URI";

    /**
     * Tag EXT-X-STREAM-INF
     */
    public static final String ATTR_BANDWIDTH = "BANDWIDTH";
    public static final String ATTR_CODECS = "CODECS";
    public static final String ATTR_RESOLUTION = "RESOLUTION";
    public static final String ATTR_AUDIO = "AUDIO";
    public static final String ATTR_VIDEO = "VIDEO";
    public static final String ATTR_SUBTITLES = "SUBTITLES";

    private String mName;
    private String mValue;

    private Attribute(String name, String value) {
        mName = name;
        mValue = value;
    }

    /**
     * 获取Attribute的名称
     */
    public String getName() {
        return mName;
    }

    /**
     * 获取Attribute的值
     */
    public String getValue() {
        return mValue;
    }

    public static Attribute parse(String attribute) {
        String[] result = attribute.split("=");

        return new Attribute(result[0], result[1]);
    }
}
