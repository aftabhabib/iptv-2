package com.iptv.core.hls.playlist;

import java.util.HashMap;
import java.util.Map;

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
     * 媒体类型
     */
    public static final String TYPE_VIDEO = "VIDEO";
    public static final String TYPE_AUDIO = "AUDIO";
    public static final String TYPE_SUBTITLE = "SUBTITLES";
    public static final String TYPE_CLOSED_CAPTIONS = "CLOSED-CAPTIONS";

    private Map<String, String> mAttributes = new HashMap<String, String>();

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
    void setAttribute(String name, String value) {
        mAttributes.put(name, value);
    }

    /**
     * 设置类型
     */
    public void setType(String type) {
        if (!type.equals(TYPE_AUDIO)
                && !type.equals(TYPE_VIDEO)
                && !type.equals(TYPE_SUBTITLE)
                && !type.equals(TYPE_CLOSED_CAPTIONS)) {
            throw new IllegalArgumentException("invalid type");
        }

        mAttributes.put(ATTR_TYPE, AttributeValue.writeEnumeratedString(type));
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        mAttributes.put(ATTR_URI, AttributeValue.writeQuotedString(uri));
    }

    /**
     * 设置媒体所属（展示）组的id
     */
    public void setGroupId(String groupId) {
        mAttributes.put(ATTR_GROUP_ID, AttributeValue.writeQuotedString(groupId));
    }

    /**
     * 设置语言
     */
    public void setLanguage(String language) {
        mAttributes.put(ATTR_LANGUAGE, AttributeValue.writeQuotedString(language));
    }

    /**
     * 设置连带的语言
     */
    public void setAssociatedLanguage(String language) {
        mAttributes.put(ATTR_ASSOCIATED_LANGUAGE, AttributeValue.writeQuotedString(language));
    }

    /**
     * 设置名称
     */
    public void setName(String name) {
        mAttributes.put(ATTR_NAME, AttributeValue.writeQuotedString(name));
    }

    /**
     * 设置是否默认选择
     */
    public void setDefaultSelect(boolean defaultSelect) {
        mAttributes.put(ATTR_DEFAULT, AttributeValue.writeEnumeratedString(
                defaultSelect ? AttributeValue.ENUM_STRING_YES : AttributeValue.ENUM_STRING_NO));
    }

    /**
     * 设置是否自动选择
     */
    public void setAutoSelect(boolean autoSelect) {
        mAttributes.put(ATTR_AUTO_SELECT, AttributeValue.writeEnumeratedString(
                autoSelect ? AttributeValue.ENUM_STRING_YES : AttributeValue.ENUM_STRING_NO));
    }

    /**
     * 设置是否强制选择
     */
    public void setForcedSelect(boolean forcedSelect) {
        mAttributes.put(ATTR_FORCED, AttributeValue.writeEnumeratedString(
                forcedSelect ? AttributeValue.ENUM_STRING_YES : AttributeValue.ENUM_STRING_NO));
    }

    /**
     * 设置流内（CC字幕轨道）的id
     */
    public void setInStreamId(String streamId) {
        /**
         * "CCn" where n MUST be an integer between 1 and 4
         * or "SERVICEn" where n MUST be an integer between 1 and 63
         */
        if (!streamId.startsWith("CC")
                && !streamId.startsWith("SERVICE")) {
            throw new IllegalArgumentException("invalid stream id");
        }

        mAttributes.put(ATTR_IN_STREAM_ID, AttributeValue.writeQuotedString(streamId));
    }

    /**
     * 设置特性
     */
    public void setCharacteristics(String[] characteristics) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < characteristics.length; i++) {
            if (i > 0) {
                buffer.append(",");
            }

            buffer.append(characteristics[i]);
        }

        mAttributes.put(ATTR_CHARACTERISTICS, AttributeValue.writeQuotedString(buffer.toString()));
    }

    /**
     * 设置声道数
     */
    public void setChannels(int channels) {
        String parameter = String.valueOf(channels);
        mAttributes.put(ATTR_CHANNELS, AttributeValue.writeQuotedString(parameter));
    }

    /**
     * 是否定义了类型
     */
    public boolean containsType() {
        return mAttributes.containsKey(ATTR_TYPE);
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mAttributes.containsKey(ATTR_URI);
    }

    /**
     * 是否定义了表现组的id
     */
    public boolean containsGroupId() {
        return mAttributes.containsKey(ATTR_GROUP_ID);
    }

    /**
     * 是否定义了语言
     */
    public boolean containsLanguage() {
        return mAttributes.containsKey(ATTR_LANGUAGE);
    }

    /**
     * 是否定义了连带的语言
     */
    public boolean containsAssociatedLanguage() {
        return mAttributes.containsKey(ATTR_ASSOCIATED_LANGUAGE);
    }

    /**
     * 是否定义了名称
     */
    public boolean containsName() {
        return mAttributes.containsKey(ATTR_NAME);
    }

    /**
     * 是否定义了流内（CC字幕轨道）的id
     */
    public boolean containsInStreamId() {
        return mAttributes.containsKey(ATTR_IN_STREAM_ID);
    }

    /**
     * 是否定义了特性
     */
    public boolean containsCharacteristics() {
        return mAttributes.containsKey(ATTR_CHARACTERISTICS);
    }

    /**
     * 是否定义了声道数
     */
    public boolean containsChannels() {
        return mAttributes.containsKey(ATTR_CHANNELS);
    }

    /**
     * 获取类型
     */
    public String getType() {
        if (!containsType()) {
            throw new IllegalStateException("no TYPE attribute");
        }

        return AttributeValue.readEnumeratedString(mAttributes.get(ATTR_TYPE));
    }

    /**
     * 获取uri
     */
    public String getUri() {
        if (!containsUri()) {
            throw new IllegalStateException("no URI attribute");
        }

        return AttributeValue.readQuotedString(mAttributes.get(ATTR_URI));
    }

    /**
     * 获取所属组的ID
     */
    public String getGroupId() {
        if (!containsGroupId()) {
            throw new IllegalStateException("no GROUP-ID attribute");
        }

        return AttributeValue.readQuotedString(mAttributes.get(ATTR_GROUP_ID));
    }

    /**
     * 获取语言
     */
    public String getLanguage() {
        if (!containsLanguage()) {
            throw new IllegalStateException("no LANGUAGE attribute");
        }

        return AttributeValue.readQuotedString(mAttributes.get(ATTR_LANGUAGE));
    }

    /**
     * 获取与该表现连带的语言
     */
    public String getAssociatedLanguage() {
        if (!containsAssociatedLanguage()) {
            throw new IllegalStateException("no ASSOC-LANGUAGE attribute");
        }

        return AttributeValue.readQuotedString(mAttributes.get(ATTR_ASSOCIATED_LANGUAGE));
    }

    /**
     * 获取名称
     */
    public String getName() {
        if (!containsName()) {
            throw new IllegalStateException("no NAME attribute");
        }

        return AttributeValue.readQuotedString(mAttributes.get(ATTR_NAME));
    }

    /**
     * 是不是默认选择
     */
    public boolean defaultSelect() {
        boolean isDefault;
        if (!mAttributes.containsKey(ATTR_DEFAULT)) {
            /**
             * 默认值：否
             */
            isDefault = false;
        }
        else {
            String value = AttributeValue.readEnumeratedString(mAttributes.get(ATTR_DEFAULT));
            if (value.equals(AttributeValue.ENUM_STRING_YES)) {
                isDefault = true;
            }
            else if (value.equals(AttributeValue.ENUM_STRING_NO)) {
                isDefault = false;
            }
            else {
                throw new IllegalArgumentException("only YES or NO");
            }
        }

        return isDefault;
    }

    /**
     * 是不是自动选择
     */
    public boolean autoSelect() {
        boolean isAuto;
        if (!mAttributes.containsKey(ATTR_AUTO_SELECT)) {
            /**
             * 默认值：否
             */
            isAuto = false;
        }
        else {
            String value = AttributeValue.readEnumeratedString(mAttributes.get(ATTR_AUTO_SELECT));
            if (value.equals(AttributeValue.ENUM_STRING_YES)) {
                isAuto = true;
            }
            else if (value.equals(AttributeValue.ENUM_STRING_NO)) {
                isAuto = false;
            }
            else {
                throw new IllegalArgumentException("only YES or NO");
            }
        }

        return isAuto;
    }

    /**
     * 是不是强制选择
     */
    public boolean forcedSelect() {
        boolean isForced;
        if (!mAttributes.containsKey(ATTR_FORCED)) {
            /**
             * 默认值：否
             */
            isForced = false;
        }
        else {
            String value = AttributeValue.readEnumeratedString(mAttributes.get(ATTR_FORCED));
            if (value.equals(AttributeValue.ENUM_STRING_YES)) {
                isForced = true;
            }
            else if (value.equals(AttributeValue.ENUM_STRING_NO)) {
                isForced = false;
            }
            else {
                throw new IllegalArgumentException("only YES or NO");
            }
        }

        return isForced;
    }

    /**
     * 获取流内（CC字幕轨道）的id
     */
    public String getInStreamId() {
        if (!containsInStreamId()) {
            throw new IllegalStateException("no INSTREAM-ID attribute");
        }

        return AttributeValue.readQuotedString(mAttributes.get(ATTR_IN_STREAM_ID));
    }

    /**
     * 获取特性
     */
    public String[] getCharacteristics() {
        if (!containsCharacteristics()) {
            throw new IllegalStateException("no CHARACTERISTICS attribute");
        }

        String content = AttributeValue.readQuotedString(mAttributes.get(ATTR_CHARACTERISTICS));

        /**
         * one or more Uniform Type Identifiers separated by comma (,) characters
         */
        String[] characteristics;
        if (content.contains(",")) {
            characteristics = content.split(",");
        }
        else {
            characteristics = new String[] { content };
        }

        return characteristics;
    }

    /**
     * 获取声道
     */
    public int getChannels() {
        if (!containsChannels()) {
            throw new IllegalStateException("no CHANNELS attribute");
        }

        String content = AttributeValue.readQuotedString(mAttributes.get(ATTR_CHANNELS));

        /**
         * parameters are separated by the "/" character.
         */
        String[] parameters;
        if (content.contains("/")) {
            parameters = content.split("/");
        }
        else {
            parameters = new String[] { content };
        }

        /**
         * the first parameter is a count of audio channels.
         */
        return Integer.parseInt(parameters[0]);
    }

    /**
     * 生成属性列表
     */
    public String makeAttributeList() {
        StringBuilder builder = new StringBuilder();

        int attributeCnt = 0;
        for (String name : mAttributes.keySet()) {
            /**
             * 多个attribute之间通过“,”分隔
             */
            if (attributeCnt > 0) {
                builder.append(",");
            }

            builder.append(name);
            builder.append("=");
            builder.append(mAttributes.get(name));

            attributeCnt++;
        }

        return builder.toString();
    }
}
