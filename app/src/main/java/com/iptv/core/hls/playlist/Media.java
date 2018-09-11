package com.iptv.core.hls.playlist;

import com.iptv.core.hls.datatype.EnumeratedString;
import com.iptv.core.hls.datatype.QuotedString;

/**
 * 媒体
 */
public final class Media {
    private Attribute mAttribute = new Attribute();

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
        if (name.equals(Attribute.TYPE)) {
            mAttribute.putEnumeratedString(name, EnumeratedString.parse(value));
        }
        else if (name.equals(Attribute.URI)) {
            mAttribute.putQuotedString(name, QuotedString.parse(value));
        }
        else if (name.equals(Attribute.GROUP_ID)) {
            mAttribute.putQuotedString(name, QuotedString.parse(value));
        }
        else if (name.equals(Attribute.LANGUAGE)) {
            mAttribute.putQuotedString(name, QuotedString.parse(value));
        }
        else if (name.equals(Attribute.ASSOCIATED_LANGUAGE)) {
            mAttribute.putQuotedString(name, QuotedString.parse(value));
        }
        else if (name.equals(Attribute.NAME)) {
            mAttribute.putQuotedString(name, QuotedString.parse(name));
        }
        else if (name.equals(Attribute.DEFAULT)) {
            mAttribute.putEnumeratedString(name, EnumeratedString.parse(value));
        }
        else if (name.equals(Attribute.AUTO_SELECT)) {
            mAttribute.putEnumeratedString(name, EnumeratedString.parse(value));
        }
        else if (name.equals(Attribute.FORCED)) {
            mAttribute.putEnumeratedString(name, EnumeratedString.parse(value));
        }
        else if (name.equals(Attribute.IN_STREAM_ID)) {
            mAttribute.putQuotedString(name, QuotedString.parse(value));
        }
        else if (name.equals(Attribute.CHARACTERISTICS)) {
            mAttribute.putQuotedString(name, QuotedString.parse(value));
        }
        else if (name.equals(Attribute.CHANNELS)) {
            mAttribute.putQuotedString(name, QuotedString.parse(value));
        }
    }

    /**
     * 设置类型
     */
    public void setType(String type) {
        if (!type.equals(Attribute.TYPE_AUDIO)
                && !type.equals(Attribute.TYPE_VIDEO)
                && !type.equals(Attribute.TYPE_SUBTITLE)
                && !type.equals(Attribute.TYPE_CLOSED_CAPTIONS)) {
            throw new IllegalArgumentException("invalid type");
        }

        mAttribute.putEnumeratedString(Attribute.TYPE, new EnumeratedString(type));
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        mAttribute.putQuotedString(Attribute.URI, new QuotedString(uri));
    }

    /**
     * 设置媒体所属（展示）组的id
     */
    public void setGroupId(String groupId) {
        mAttribute.putQuotedString(Attribute.GROUP_ID, new QuotedString(groupId));
    }

    /**
     * 设置语言
     */
    public void setLanguage(String language) {
        mAttribute.putQuotedString(Attribute.LANGUAGE, new QuotedString(language));
    }

    /**
     * 设置连带的语言
     */
    public void setAssociatedLanguage(String language) {
        mAttribute.putQuotedString(Attribute.ASSOCIATED_LANGUAGE, new QuotedString(language));
    }

    /**
     * 设置名称
     */
    public void setName(String name) {
        mAttribute.putQuotedString(Attribute.NAME, new QuotedString(name));
    }

    /**
     * 设置是否默认选择
     */
    public void setDefaultSelect(boolean state) {
        mAttribute.putEnumeratedString(Attribute.DEFAULT, new EnumeratedString(state));
    }

    /**
     * 设置是否自动选择
     */
    public void setAutoSelect(boolean state) {
        mAttribute.putEnumeratedString(Attribute.AUTO_SELECT, new EnumeratedString(state));
    }

    /**
     * 设置是否强制选择
     */
    public void setForcedSelect(boolean state) {
        mAttribute.putEnumeratedString(Attribute.FORCED, new EnumeratedString(state));
    }

    /**
     * 设置流内（CC字幕轨道）的id
     */
    public void setInStreamId(String streamId) {
        /**
         * 有效值是"CC1" ~ "CC4" or "SERVICE1" ~ "SERVICE63"
         */
        if (!streamId.startsWith("CC")
                && !streamId.startsWith("SERVICE")) {
            throw new IllegalArgumentException("invalid stream id");
        }

        mAttribute.putQuotedString(Attribute.IN_STREAM_ID, new QuotedString(streamId));
    }

    /**
     * 设置特性
     */
    public void setCharacteristics(String[] characteristics) {
        mAttribute.putQuotedString(Attribute.CHARACTERISTICS,
                new QuotedString(characteristics, ","));
    }

    /**
     * 设置声道数
     */
    public void setChannels(int channels) {
        String parameter = String.valueOf(channels);
        mAttribute.putQuotedString(Attribute.CHANNELS, new QuotedString(parameter));
    }

    /**
     * 是否定义了类型
     */
    public boolean containsType() {
        return mAttribute.containsName(Attribute.TYPE);
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mAttribute.containsName(Attribute.URI);
    }

    /**
     * 是否定义了媒体所属（展示）组的id
     */
    public boolean containsGroupId() {
        return mAttribute.containsName(Attribute.GROUP_ID);
    }

    /**
     * 是否定义了语言
     */
    public boolean containsLanguage() {
        return mAttribute.containsName(Attribute.LANGUAGE);
    }

    /**
     * 是否定义了连带的语言
     */
    public boolean containsAssociatedLanguage() {
        return mAttribute.containsName(Attribute.ASSOCIATED_LANGUAGE);
    }

    /**
     * 是否定义了名称
     */
    public boolean containsName() {
        return mAttribute.containsName(Attribute.NAME);
    }

    /**
     * 是否定义了流内（CC字幕轨道）的id
     */
    public boolean containsInStreamId() {
        return mAttribute.containsName(Attribute.IN_STREAM_ID);
    }

    /**
     * 是否定义了特性
     */
    public boolean containsCharacteristics() {
        return mAttribute.containsName(Attribute.CHARACTERISTICS);
    }

    /**
     * 是否定义了声道数
     */
    public boolean containsChannels() {
        return mAttribute.containsName(Attribute.CHANNELS);
    }

    /**
     * 获取类型
     */
    public String getType() {
        EnumeratedString type = mAttribute.getEnumeratedString(Attribute.TYPE);
        return type.value();
    }

    /**
     * 获取uri
     */
    public String getUri() {
        QuotedString uri = mAttribute.getQuotedString(Attribute.URI);
        return uri.value();
    }

    /**
     * 获取媒体所属（展示）组的id
     */
    public String getGroupId() {
        QuotedString groupId = mAttribute.getQuotedString(Attribute.GROUP_ID);
        return groupId.value();
    }

    /**
     * 获取语言
     */
    public String getLanguage() {
        QuotedString language = mAttribute.getQuotedString(Attribute.LANGUAGE);
        return language.value();
    }

    /**
     * 获取与该表现连带的语言
     */
    public String getAssociatedLanguage() {
        QuotedString language = mAttribute.getQuotedString(Attribute.ASSOCIATED_LANGUAGE);
        return language.value();
    }

    /**
     * 获取名称
     */
    public String getName() {
        QuotedString name = mAttribute.getQuotedString(Attribute.NAME);
        return name.value();
    }

    /**
     * 是不是默认选择
     */
    public boolean defaultSelect() {
        if (!mAttribute.containsName(Attribute.DEFAULT)) {
            /**
             * 默认值：否
             */
            return false;
        }
        else {
            EnumeratedString defaultSelect = mAttribute.getEnumeratedString(Attribute.DEFAULT);
            return defaultSelect.toBoolean();
        }
    }

    /**
     * 是不是自动选择
     */
    public boolean autoSelect() {
        if (!mAttribute.containsName(Attribute.AUTO_SELECT)) {
            /**
             * 默认值：否
             */
            return false;
        }
        else {
            EnumeratedString autoSelect = mAttribute.getEnumeratedString(Attribute.AUTO_SELECT);
            return autoSelect.toBoolean();
        }
    }

    /**
     * 是不是强制选择
     */
    public boolean forcedSelect() {
        if (!mAttribute.containsName(Attribute.FORCED)) {
            /**
             * 默认值：否
             */
            return false;
        }
        else {
            EnumeratedString forcedSelect = mAttribute.getEnumeratedString(Attribute.FORCED);
            return forcedSelect.toBoolean();
        }
    }

    /**
     * 获取流内（CC字幕轨道）的id
     */
    public String getInStreamId() {
        QuotedString streamId = mAttribute.getQuotedString(Attribute.IN_STREAM_ID);
        return streamId.value();
    }

    /**
     * 获取特性
     */
    public String[] getCharacteristics() {
        QuotedString characteristics = mAttribute.getQuotedString(Attribute.CHARACTERISTICS);
        /**
         * one or more Uniform Type Identifiers separated by comma (,) characters
         */
        return characteristics.split(",");
    }

    /**
     * 获取声道
     */
    public int getChannels() {
        QuotedString channels = mAttribute.getQuotedString(Attribute.CHANNELS);
        /**
         * Parameters are separated by the "/" character.
         * And the first parameter is a count of audio channels.
         */
        String[] parameters = channels.split("/");
        return Integer.parseInt(parameters[0]);
    }

    /**
     * 生成属性列表
     */
    public String makeAttributeList() {
        return mAttribute.toString();
    }
}
