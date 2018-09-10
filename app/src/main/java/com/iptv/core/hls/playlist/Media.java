package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;

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
    public Media() {
        /**
         * nothing
         */
    }

    /**
     * 设置属性
     */
    public void setAttribute(String name, String value) {
        if (name.equals(ATTR_TYPE)) {
            setType(AttributeValue.readEnumeratedString(value));
        }
        else if (name.equals(ATTR_URI)) {
            setUri(AttributeValue.readQuotedString(value));
        }
        else if (name.equals(ATTR_GROUP_ID)) {
            setGroupId(AttributeValue.readQuotedString(value));
        }
        else if (name.equals(ATTR_LANGUAGE)) {
            setLanguage(AttributeValue.readQuotedString(value));
        }
        else if (name.equals(ATTR_ASSOCIATED_LANGUAGE)) {
            setAssociatedLanguage(AttributeValue.readQuotedString(value));
        }
        else if (name.equals(ATTR_NAME)) {
            setName(AttributeValue.readQuotedString(value));
        }
        else if (name.equals(ATTR_DEFAULT)) {
            String content = AttributeValue.readEnumeratedString(value);
            setDefaultSelect(enumStringToBoolean(content));
        }
        else if (name.equals(ATTR_AUTO_SELECT)) {
            String content = AttributeValue.readEnumeratedString(value);
            setAutoSelect(enumStringToBoolean(content));
        }
        else if (name.equals(ATTR_FORCED)) {
            String content = AttributeValue.readEnumeratedString(value);
            setForcedSelect(enumStringToBoolean(content));
        }
        else if (name.equals(ATTR_IN_STREAM_ID)) {
            setInStreamId(AttributeValue.readQuotedString(value));
        }
        else if (name.equals(ATTR_CHARACTERISTICS)) {
            String content = AttributeValue.readQuotedString(value);

            String[] characteristics;
            if (value.contains(",")) {
                characteristics = content.split(",");
            }
            else {
                characteristics = new String[] { content };
            }

            setCharacteristics(characteristics);
        }
        else if (name.equals(ATTR_CHANNELS)) {
            String content = AttributeValue.readQuotedString(value);

            String[] parameters;
            if (value.contains("/")) {
                parameters = content.split("/");
            }
            else {
                parameters = new String[] { content };
            }

            /**
             * If TYPE is AUDIO, then the first parameter is a count of audio channels
             * No other CHANNELS parameters are currently defined
             */
            int channels = Integer.parseInt(parameters[0]);
            setChannels(channels);
        }
        else {
            /**
             * ignore
             */
        }
    }

    /**
     * 设置类型
     */
    public void setType(String type) {
        if (!type.equals(TYPE_AUDIO)
                && !type.equals(TYPE_VIDEO)
                && !type.equals(TYPE_SUBTITLE)
                && !type.equals(TYPE_CLOSED_CAPTIONS)) {
            throw new IllegalArgumentException("bad value");
        }

        mMetaData.putString(ATTR_TYPE, type);
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        mMetaData.putString(ATTR_URI, uri);
    }

    /**
     * 设置媒体所属表现组的id
     */
    public void setGroupId(String groupId) {
        mMetaData.putString(ATTR_GROUP_ID, groupId);
    }

    /**
     * 设置语言
     */
    public void setLanguage(String language) {
        mMetaData.putString(ATTR_LANGUAGE, language);
    }

    /**
     * 设置连带的语言
     */
    public void setAssociatedLanguage(String language) {
        mMetaData.putString(ATTR_ASSOCIATED_LANGUAGE, language);
    }

    /**
     * 设置名称
     */
    public void setName(String name) {
        mMetaData.putString(ATTR_NAME, name);
    }

    /**
     * 设置是否默认选择
     */
    public void setDefaultSelect(boolean defaultSelect) {
        mMetaData.putBoolean(ATTR_DEFAULT, defaultSelect);
    }

    /**
     * 设置是否自动选择
     */
    public void setAutoSelect(boolean autoSelect) {
        mMetaData.putBoolean(ATTR_AUTO_SELECT, autoSelect);
    }

    /**
     * 设置是否强制选择
     */
    public void setForcedSelect(boolean forcedSelect) {
        mMetaData.putBoolean(ATTR_FORCED, forcedSelect);
    }

    /**
     * 设置流内部的id
     */
    public void setInStreamId(String streamId) {
        mMetaData.putString(ATTR_IN_STREAM_ID, streamId);
    }

    /**
     * 设置特性
     */
    public void setCharacteristics(String[] characteristics) {
        mMetaData.putStringArray(ATTR_CHARACTERISTICS, characteristics);
    }

    /**
     * 设置声道
     */
    public void setChannels(int channels) {
        mMetaData.putInteger("audio-channels", channels);
    }

    /**
     * 是否定义了类型
     */
    public boolean containsType() {
        return mMetaData.containsKey(ATTR_TYPE);
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mMetaData.containsKey(ATTR_URI);
    }

    /**
     * 是否定义了表现组的id
     */
    public boolean containsGroupId() {
        return mMetaData.containsKey(ATTR_GROUP_ID);
    }

    /**
     * 是否定义了语言
     */
    public boolean containsLanguage() {
        return mMetaData.containsKey(ATTR_LANGUAGE);
    }

    /**
     * 是否定义了连带的语言
     */
    public boolean containsAssociatedLanguage() {
        return mMetaData.containsKey(ATTR_ASSOCIATED_LANGUAGE);
    }

    /**
     * 是否定义了名称
     */
    public boolean containsName() {
        return mMetaData.containsKey(ATTR_NAME);
    }

    /**
     * 是否定义了流内部的id
     */
    public boolean containsInStreamId() {
        return mMetaData.containsKey(ATTR_IN_STREAM_ID);
    }

    /**
     * 是否定义了特性
     */
    public boolean containsCharacteristics() {
        return mMetaData.containsKey(ATTR_CHARACTERISTICS);
    }

    /**
     * 是否定义了声道
     */
    public boolean containsChannels() {
        return mMetaData.containsKey("audio-channels");
    }

    /**
     * 获取类型
     */
    public String getType() {
        if (!containsType()) {
            throw new IllegalStateException("no TYPE attribute");
        }

        return mMetaData.getString(ATTR_TYPE);
    }

    /**
     * 获取uri
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
        if (!containsGroupId()) {
            throw new IllegalStateException("no GROUP-ID attribute");
        }

        return mMetaData.getString(ATTR_GROUP_ID);
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
        if (!containsName()) {
            throw new IllegalStateException("no NAME attribute");
        }

        return mMetaData.getString(ATTR_NAME);
    }

    /**
     * 是不是默认选择
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
     * 是不是自动选择
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
     * 是不是强制选择
     */
    public boolean forcedSelect() {
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

    /**
     * 获取流内部的id
     */
    public String getInStreamId() {
        if (!containsInStreamId()) {
            throw new IllegalStateException("no INSTREAM-ID attribute");
        }

        return mMetaData.getString(ATTR_IN_STREAM_ID);
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
     * 获取声道
     */
    public int getChannels() {
        if (!containsChannels()) {
            throw new IllegalStateException("no CHANNELS attribute");
        }

        return mMetaData.getInteger("audio-channels");
    }

    /**
     * 生成属性列表
     */
    public String makeAttributeList() {
        StringBuilder builder = new StringBuilder();

        for (String key : mMetaData.keySet()) {
            /**
             * attribute之间通过“,”分隔
             */
            if (builder.length() > 0) {
                builder.append(",");
            }

            if (key.equals(ATTR_TYPE)) {
                String type = getType();

                builder.append(ATTR_TYPE
                        + "="
                        + AttributeValue.writeEnumeratedString(type));
            }
            else if (key.equals(ATTR_URI)) {
                String uri = getUri();

                builder.append(ATTR_URI
                        + "="
                        + AttributeValue.writeQuotedString(uri));
            }
            else if (key.equals(ATTR_GROUP_ID)) {
                String groupId = getGroupId();

                builder.append(ATTR_GROUP_ID
                        + "="
                        + AttributeValue.writeQuotedString(groupId));
            }
            else if (key.equals(ATTR_LANGUAGE)) {
                String language = getLanguage();

                builder.append(ATTR_LANGUAGE
                        + "="
                        + AttributeValue.writeQuotedString(language));
            }
            else if (key.equals(ATTR_ASSOCIATED_LANGUAGE)) {
                String language = getAssociatedLanguage();

                builder.append(ATTR_ASSOCIATED_LANGUAGE
                        + "="
                        + AttributeValue.writeQuotedString(language));
            }
            else if (key.equals(ATTR_NAME)) {
                String name = getName();

                builder.append(ATTR_NAME
                        + "="
                        + AttributeValue.writeQuotedString(name));
            }
            else if (key.equals(ATTR_DEFAULT)) {
                boolean defaultSelect = defaultSelect();

                builder.append(ATTR_DEFAULT
                        + "="
                        + AttributeValue.writeQuotedString(booleanToEnumString(defaultSelect)));
            }
            else if (key.equals(ATTR_AUTO_SELECT)) {
                boolean autoSelect = autoSelect();

                builder.append(ATTR_AUTO_SELECT
                        + "="
                        + AttributeValue.writeQuotedString(booleanToEnumString(autoSelect)));
            }
            else if (key.equals(ATTR_FORCED)) {
                boolean forcedSelect = forcedSelect();

                builder.append(ATTR_FORCED
                        + "="
                        + AttributeValue.writeQuotedString(booleanToEnumString(forcedSelect)));
            }
            else if (key.equals(ATTR_IN_STREAM_ID)) {
                String streamId = getInStreamId();

                builder.append(ATTR_IN_STREAM_ID
                        + "="
                        + AttributeValue.writeQuotedString(streamId));
            }
            else if (key.equals(ATTR_CHARACTERISTICS)) {
                String[] characteristics = getCharacteristics();

                StringBuffer contentBuffer = new StringBuffer();
                for (int i = 0; i < characteristics.length; i++) {
                    if (i > 0) {
                        contentBuffer.append(",");
                    }

                    contentBuffer.append(String.valueOf(characteristics[i]));
                }

                builder.append(ATTR_CHARACTERISTICS
                        + "="
                        + AttributeValue.writeQuotedString(contentBuffer.toString()));
            }
            else if (key.equals("audio-channels")) {
                int channels = getChannels();

                builder.append(ATTR_CHANNELS
                        + "="
                        + AttributeValue.writeQuotedString(String.valueOf(channels)));
            }
            else {
                /**
                 * ignore
                 */
            }
        }

        return builder.toString();
    }

    /**
     * “是/否”字符串转换为布尔值
     */
    private static boolean enumStringToBoolean(String value) {
        if (value.equals(VALUE_YES)) {
            return true;
        }
        else if (value.equals(VALUE_NO)) {
            return false;
        }
        else {
            throw new IllegalArgumentException("bad value");
        }
    }

    /**
     * 布尔值转换为“是/否”字符串
     */
    private static String booleanToEnumString(boolean value) {
        return value ? VALUE_YES : VALUE_NO;
    }
}
