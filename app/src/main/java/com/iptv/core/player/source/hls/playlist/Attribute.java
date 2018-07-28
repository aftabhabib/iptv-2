package com.iptv.core.player.source.hls.playlist;

class Attribute {
    public static final String ATTR_METHOD = "METHOD";
    public static final String ATTR_IV = "IV";

    public static final String ATTR_TYPE = "TYPE";
    public static final String ATTR_GROUP_ID = "GROUP-ID";
    public static final String ATTR_LANGUAGE = "LANGUAGE";
    public static final String ATTR_NAME = "NAME";
    public static final String ATTR_DEFAULT = "DEFAULT";
    public static final String ATTR_AUTO_SELECT = "AUTOSELECT";

    public static final String ATTR_URI = "URI";

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

    public static Attribute parse(String attribute) {
        String[] result = attribute.split("=");

        return new Attribute(result[0], result[1]);
    }
}
