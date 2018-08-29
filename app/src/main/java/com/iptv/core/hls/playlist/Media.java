package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;
import com.iptv.core.utils.MalformedFormatException;

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

    private MetaData mMetaData = new MetaData();
    private String mUri = null;

    /**
     * 构造函数
     */
    public Media(String attributeList) throws MalformedFormatException {
        String[] attributes = attributeList.split(",");
        for (int i = 0; i < attributes.length; i++) {
            String[] result = attributes[i].split("=");

            if (result[0].equals(ATTR_TYPE)) {
                mMetaData.putString(MetaData.KEY_MEDIA_TYPE, result[1]);
            }
            else if (result[0].equals(ATTR_GROUP_ID)) {
                mMetaData.putString(MetaData.KEY_GROUP_ID, result[1]);
            }
            else if (result[0].equals(ATTR_LANGUAGE)) {
                mMetaData.putString(MetaData.KEY_LANGUAGE, result[1]);
            }
            else if (result[0].equals(ATTR_DEFAULT)) {
                boolean defaultSelect;
                if (result[1].equals("YES")) {
                    defaultSelect = true;
                }
                else if (result[1].equals("NO")) {
                    defaultSelect = false;
                }
                else {
                    throw new MalformedFormatException("value shall be YES or NO");
                }

                mMetaData.putBoolean(MetaData.KEY_DEFAULT_SELECT, defaultSelect);
            }
            else if (result[0].equals(ATTR_URI)) {
                mUri = result[1];
            }
            else {
                /**
                 * not support yet
                 */
            }
        }

        /**
         * 检查必要参数
         */
        if (!mMetaData.containsKey(MetaData.KEY_MEDIA_TYPE)) {
            throw new MalformedFormatException("type is required");
        }
        else {
            if (getType().equals(TYPE_SUBTITLE) && (mUri == null)) {
                throw new MalformedFormatException("uri is required when type is SUBTITLES");
            }
        }

        if (!mMetaData.containsKey(MetaData.KEY_GROUP_ID)) {
            throw new MalformedFormatException("group-id is required");
        }
    }

    /**
     * 获取类型
     */
    public String getType() {
        return mMetaData.getString(MetaData.KEY_MEDIA_TYPE);
    }

    /**
     * 获取所属组的ID
     */
    public String getGroupId() {
        return mMetaData.getString(MetaData.KEY_GROUP_ID);
    }

    /**
     * 是不是默认的选择
     */
    public boolean defaultSelect() {
        if (!mMetaData.containsKey(MetaData.KEY_DEFAULT_SELECT)) {
            return false;
        }
        else {
            return mMetaData.getBoolean(MetaData.KEY_DEFAULT_SELECT);
        }
    }

    /**
     * 获取语言
     */
    public String getLanguage() {
        if (!mMetaData.containsKey(MetaData.KEY_LANGUAGE)) {
            return "";
        }
        else {
            return mMetaData.getString(MetaData.KEY_LANGUAGE);
        }
    }

    /**
     * 获取url
     */
    public String getUri() {
        return mUri;
    }
}
