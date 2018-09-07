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
    private static final String ATTR_URI = "URI";
    private static final String ATTR_GROUP_ID = "GROUP-ID";
    private static final String ATTR_LANGUAGE = "LANGUAGE";
    private static final String ATTR_ASSOCIATED_LANGUAGE = "ASSOC-LANGUAGE";
    private static final String ATTR_NAME = "NAME";
    private static final String ATTR_DEFAULT = "DEFAULT";
    private static final String ATTR_AUTO_SELECT = "AUTOSELECT";
    private static final String ATTR_FORCED = "FORCED";
    private static final String ATTR_IN_STREAM_ID = "INSTREAM-ID";
    private static final String ATTR_CHARACTERISTICS = "CHARACTERISTICS";
    private static final String ATTR_CHANNELS = "CHANNELS";

    /**
     * “是/否”的值
     */
    private static final String VALUE_YES = "YES";
    private static final String VALUE_NO = "NO";

    /**
     * 媒体类型
     */
    public static final String TYPE_VIDEO = "VIDEO";
    public static final String TYPE_AUDIO = "AUDIO";
    public static final String TYPE_SUBTITLE = "SUBTITLES";
    public static final String TYPE_CLOSED_CAPTIONS = "CLOSED-CAPTIONS";

    private MetaData mMetaData = new MetaData();

    /**
     * 构造函数
     */
    public Media(String[] attributes) throws MalformedFormatException {
        for (String attribute : attributes) {
            String[] result = attribute.split("=");
            parseAttribute(result[0], result[1]);
        }

        if (!mMetaData.containsKey(ATTR_TYPE)
                || !mMetaData.containsKey(ATTR_GROUP_ID)
                || !mMetaData.containsKey(ATTR_NAME)) {
            throw new MalformedFormatException("TYPE, GROUP-ID and NAME are required");
        }

        if (getType().equals(TYPE_CLOSED_CAPTIONS)) {
            if (mMetaData.containsKey(ATTR_URI)
                    || !mMetaData.containsKey(ATTR_IN_STREAM_ID)) {
                throw new MalformedFormatException("if TYPE is CLOSED-CAPTIONS, " +
                        "no URI attribute and INSTREAM-ID is required");
            }
        }
        else if (getType().equals(TYPE_SUBTITLE)) {
            if (!mMetaData.containsKey(ATTR_URI)) {
                throw new MalformedFormatException("if TYPE is SUBTITLES, URI is required");
            }
        }
    }

    /**
     * 解析属性
     */
    private void parseAttribute(String name, String value) throws MalformedFormatException {
        if (name.equals(ATTR_TYPE)) {
            mMetaData.putString(ATTR_TYPE, value);
        }
        else if (name.equals(ATTR_URI)) {
            mMetaData.putString(ATTR_URI, value);
        }
        else if (name.equals(ATTR_GROUP_ID)) {
            mMetaData.putString(ATTR_GROUP_ID, value);
        }
        else if (name.equals(ATTR_LANGUAGE)) {
            mMetaData.putString(ATTR_LANGUAGE, value);
        }
        else if (name.equals(ATTR_ASSOCIATED_LANGUAGE)) {
            mMetaData.putString(ATTR_ASSOCIATED_LANGUAGE, value);
        }
        else if (name.equals(ATTR_NAME)) {
            mMetaData.putString(ATTR_NAME, value);
        }
        else if (name.equals(ATTR_DEFAULT)) {
            mMetaData.putBoolean(ATTR_DEFAULT, convertValueToBoolean(value));
        }
        else if (name.equals(ATTR_AUTO_SELECT)) {
            mMetaData.putBoolean(ATTR_AUTO_SELECT, convertValueToBoolean(value));
        }
        else if (name.equals(ATTR_FORCED)) {
            mMetaData.putBoolean(ATTR_FORCED, convertValueToBoolean(value));
        }
        else if (name.equals(ATTR_IN_STREAM_ID)) {
            /**
             * "CC1", "CC2", "CC3", "CC4",
             * or "SERVICEn" where n MUST be an integer between 1 and 63
             */
            if (!value.startsWith("cc") && !value.startsWith("SERVICE")) {
                throw new MalformedFormatException("invalid INSTREAM-ID");
            }

            mMetaData.putString(ATTR_IN_STREAM_ID, value);
        }
        else if (name.equals(ATTR_CHARACTERISTICS)) {
            String[] characteristics;
            if (value.contains(",")) {
                characteristics = value.split(",");
            }
            else {
                characteristics = new String[] { value };
            }

            mMetaData.putStringArray(ATTR_CHARACTERISTICS, characteristics);
        }
        else if (name.equals(ATTR_CHANNELS)) {
            String[] parameters;
            if (value.contains("/")) {
                parameters = value.split("/");
            }
            else {
                parameters = new String[] { value };
            }

            /**
             * If TYPE is AUDIO, then the first parameter is a count of audio channels
             */
            if (getType().equals(TYPE_AUDIO)) {
                int channels = Integer.parseInt(parameters[0]);
                mMetaData.putInteger("audio-channels", channels);
            }
        }
        else {
            /**
             * ignore
             */
        }
    }

    /**
     * “是/否”字符串转换为布尔值
     */
    private static boolean convertValueToBoolean(String value) throws MalformedFormatException {
        if (value.equals(VALUE_YES)) {
            return true;
        }
        else if (value.equals(VALUE_NO)) {
            return false;
        }
        else {
            throw new MalformedFormatException("only YES or NO");
        }
    }

    /**
     * 获取类型
     */
    public String getType() {
        return mMetaData.getString(ATTR_TYPE);
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
            throw new IllegalStateException("no URI attribute");
        }

        return mMetaData.getString(ATTR_URI);
    }

    /**
     * 获取所属组的ID
     */
    public String getGroupId() {
        return mMetaData.getString(ATTR_GROUP_ID);
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
            throw new IllegalStateException("no LANGUAGE attribute");
        }

        return mMetaData.getString(ATTR_LANGUAGE);
    }

    /**
     * 是否定义了与该表现连带的语言
     */
    public boolean containsAssociatedLanguage() {
        return mMetaData.containsKey(ATTR_ASSOCIATED_LANGUAGE);
    }

    /**
     * 获取与该表现连带的语言
     */
    public String getAssociatedLanguage() {
        if (!containsAssociatedLanguage()) {
            throw new IllegalStateException("no ASSOC-LANGUAGE attribute");
        }

        return mMetaData.getString(ATTR_ASSOCIATED_LANGUAGE);
    }

    /**
     * 获取名称
     */
    public String getName() {
        return mMetaData.getString(ATTR_NAME);
    }

    /**
     * 是不是默认的选择
     */
    public boolean defaultSelect() {
        if (!mMetaData.containsKey(ATTR_DEFAULT)) {
            /**
             * implicit value
             */
            return false;
        }
        else {
            return mMetaData.getBoolean(ATTR_DEFAULT);
        }
    }

    /**
     * 是不是自动的选择
     */
    public boolean autoSelect() {
        if (!mMetaData.containsKey(ATTR_AUTO_SELECT)) {
            /**
             * implicit value
             */
            return false;
        }
        else {
            return mMetaData.getBoolean(ATTR_AUTO_SELECT);
        }
    }

    /**
     * 是不是强制的
     */
    public boolean isForced() {
        if (!mMetaData.containsKey(ATTR_FORCED)) {
            /**
             * implicit value
             */
            return false;
        }
        else {
            return mMetaData.getBoolean(ATTR_FORCED);
        }
    }

    public String getInStreamId() {
        if (!getType().equals(TYPE_CLOSED_CAPTIONS)) {
            throw new IllegalStateException("TYPE should be CLOSED-CAPTIONS");
        }

        return mMetaData.getString(ATTR_IN_STREAM_ID);
    }

    /**
     * 是否定义了特性
     */
    public boolean containsCharacteristics() {
        return mMetaData.containsKey(ATTR_CHARACTERISTICS);
    }

    /**
     * 获取特性
     */
    public String[] getCharacteristics() {
        if (!containsCharacteristics()) {
            throw new IllegalStateException("no CHARACTERISTICS attribute");
        }

        return mMetaData.getStringArray(ATTR_CHARACTERISTICS);
    }

    /**
     * 是否定义了音频轨道
     */
    public boolean containsAudioChannels() {
        if (!getType().equals(TYPE_AUDIO)) {
            throw new IllegalStateException("TYPE should be AUDIO");
        }

        return mMetaData.containsKey("audio-channels");
    }

    /**
     * 获取音频轨道
     */
    public int getAudioChannels() {
        if (!containsAudioChannels()) {
            throw new IllegalStateException("no CHANNELS attribute");
        }

        return mMetaData.getInteger("audio-channels");
    }
}
