package com.iptv.core.player.source.hls.playlist;

import java.util.ArrayList;
import java.util.List;

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
    public static final String ATTR_SUBTITLE = "SUBTITLES";

    private String mKey;
    private String mValue;

    private Attribute(String key, String value) {
        mKey = key;
        mValue = value;
    }

    public String getKey() {
        return mKey;
    }

    public String getValue() {
        return mValue;
    }

    public static List<Attribute> parseList(String attributes) {
        List<Attribute> attributeList = new ArrayList<>(10);

        String[] attributeArray = attributes.split(",");
        for (int i = 0; i < attributeArray.length; i++) {
            String[] result = attributeArray[i].split("=");

            attributeList.add(new Attribute(result[0], result[1]));
        }

        return attributeList;
    }
}
