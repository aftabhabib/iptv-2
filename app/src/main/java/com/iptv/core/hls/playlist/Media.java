package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;
import com.iptv.core.utils.MalformedFormatException;
import com.iptv.core.utils.StringUtils;

/**
 * 媒体
 */
public final class Media {
    /**
     * 属性
     */
    private static final String ATTR_TYPE = "TYPE";
    private static final String ATTR_GROUP_ID = "GROUP-ID";
    private static final String ATTR_LANGUAGE = "LANGUAGE";
    private static final String ATTR_DEFAULT = "DEFAULT";
    private static final String ATTR_URI = "URI";

    /**
     * 媒体类型
     */
    public static final String TYPE_VIDEO = "VIDEO";
    public static final String TYPE_AUDIO = "AUDIO";
    public static final String TYPE_SUBTITLE = "SUBTITLES";

    private String mType = null;
    private String mGroupId = null;
    private MetaData mMetaData = new MetaData();

    /**
     * 构造函数
     */
    public Media(String[] attributes) throws MalformedFormatException {
        for (String attribute : attributes) {
            String[] result = attribute.split("=");
            parseAttribute(result[0], result[1]);
        }

        if (!StringUtils.isValid(mType) || !StringUtils.isValid(mGroupId)) {
            throw new MalformedFormatException("TYPE and GROUP-ID is required");
        }

        if (mType.equals(TYPE_SUBTITLE) && !mMetaData.containsKey(ATTR_URI)) {
            throw new MalformedFormatException("URI is required when TYPE is SUBTITLES");
        }
    }

    /**
     * 解析属性
     */
    private void parseAttribute(String name, String value) {
        if (name.equals(ATTR_TYPE)) {
            mType = value;
        }
        else if (name.equals(ATTR_GROUP_ID)) {
            mGroupId = value;
        }
        else if (name.equals(ATTR_LANGUAGE)) {
            mMetaData.putString(ATTR_LANGUAGE, value);
        }
        else if (name.equals(ATTR_DEFAULT)) {
            mMetaData.putBoolean(ATTR_DEFAULT, getBooleanValue(value));
        }
        else if (name.equals(ATTR_URI)) {
            mMetaData.putString(ATTR_URI, value);
        }
        else {
            /**
             * ignore
             */
        }
    }

    /**
     * 获取（二值）字符串对应的布尔值
     */
    private static boolean getBooleanValue(String value) {
        if (value.equals("YES")) {
            return true;
        }
        else if (value.equals("NO")) {
            return false;
        }
        else {
            throw new IllegalArgumentException("only YES or NO");
        }
    }

    /**
     * 获取类型
     */
    public String getType() {
        return mType;
    }

    /**
     * 获取所属组的ID
     */
    public String getGroupId() {
        return mGroupId;
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mMetaData.containsKey(ATTR_URI);
    }

    /**
     * 获取url
     */
    public String getUri() {
        if (!containsUri()) {
            throw new IllegalStateException("no URI");
        }

        return mMetaData.getString(ATTR_URI);
    }

    /**
     * 是不是默认的选择
     */
    public boolean defaultSelect() {
        if (!mMetaData.containsKey(ATTR_DEFAULT)) {
            return false;
        }
        else {
            return mMetaData.getBoolean(ATTR_DEFAULT);
        }
    }

    /**
     * 是否定义了语言
     */
    public boolean containsLanguage() {
        return mMetaData.containsKey(ATTR_LANGUAGE);
    }

    /**
     * 获取语言
     */
    public String getLanguage() {
        if (!containsLanguage()) {
            throw new IllegalStateException("no LANGUAGE");
        }

        return mMetaData.getString(ATTR_LANGUAGE);
    }
}
