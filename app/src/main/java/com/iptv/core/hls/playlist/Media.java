package com.iptv.core.hls.playlist;

import com.iptv.core.hls.exception.MalformedPlaylistException;
import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.datatype.QuotedString;
import com.iptv.core.hls.playlist.tag.Tag;

/**
 * 媒体
 */
public final class Media {
    private AttributeList mAttributeList;

    /**
     * 构造函数
     */
    public Media() {
        mAttributeList = new AttributeList();
    }

    /**
     * 构造函数
     */
    public Media(AttributeList attributeList) {
        mAttributeList = attributeList;
    }

    /**
     * 设置类型
     */
    public void setType(String type) {
        Attribute attribute = Attribute.create(Attribute.Name.TYPE, type);
        mAttributeList.put(attribute);
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        Attribute attribute = Attribute.create(Attribute.Name.URI, new QuotedString(uri));
        mAttributeList.put(attribute);
    }

    /**
     * 设置媒体所属（展示）组的id
     */
    public void setGroupId(String groupId) {
        Attribute attribute = Attribute.create(Attribute.Name.GROUP_ID, new QuotedString(groupId));
        mAttributeList.put(attribute);
    }

    /**
     * 设置语言
     */
    public void setLanguage(String language) {
        Attribute attribute = Attribute.create(Attribute.Name.LANGUAGE, new QuotedString(language));
        mAttributeList.put(attribute);
    }

    /**
     * 设置连带的语言
     */
    public void setAssociatedLanguage(String language) {
        Attribute attribute = Attribute.create(
                Attribute.Name.ASSOCIATED_LANGUAGE, new QuotedString(language));
        mAttributeList.put(attribute);
    }

    /**
     * 设置名称
     */
    public void setName(String name) {
        Attribute attribute = Attribute.create(Attribute.Name.NAME, new QuotedString(name));
        mAttributeList.put(attribute);
    }

    /**
     * 设置（该表现）是默认的选择
     */
    public void setDefaultSelect(boolean state) {
        Attribute attribute = Attribute.create(Attribute.Name.DEFAULT, state);
        mAttributeList.put(attribute);
    }

    /**
     * 设置（该表现）是自动的选择
     */
    public void setAutoSelect(boolean state) {
        Attribute attribute = Attribute.create(Attribute.Name.AUTO_SELECT, state);
        mAttributeList.put(attribute);
    }

    /**
     * 设置（该表现）是强制的选择
     */
    public void setForcedSelect(boolean state) {
        Attribute attribute = Attribute.create(Attribute.Name.FORCED, state);
        mAttributeList.put(attribute);
    }

    /**
     * 设置CC字幕轨道的id
     */
    public void setClosedCaptionStreamId(String streamId) {
        Attribute attribute = Attribute.create(
                Attribute.Name.IN_STREAM_ID, new QuotedString(streamId));
        mAttributeList.put(attribute);
    }

    /**
     * 设置特性
     */
    public void setCharacteristics(String[] characteristics) {
        Attribute attribute = Attribute.create(
                Attribute.Name.CHARACTERISTICS, new QuotedString(characteristics, ','));
        mAttributeList.put(attribute);
    }

    /**
     * 设置音频声道数
     */
    public void setAudioChannels(int channels) {
        String[] parameters = new String[] { String.valueOf(channels) };

        Attribute attribute = Attribute.create(
                Attribute.Name.CHANNELS, new QuotedString(parameters, '/'));
        mAttributeList.put(attribute);
    }

    /**
     * 是否定义了属性
     */
    public boolean containsAttribute(String attributeName) {
        return mAttributeList.containsAttribute(attributeName);
    }

    /**
     * 获取类型
     */
    public String getType() {
        Attribute attribute = mAttributeList.get(Attribute.Name.TYPE);
        return attribute.getEnumeratedStringValue();
    }

    /**
     * 获取uri
     */
    public String getUri() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.URI);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取媒体所属（展示）组的id
     */
    public String getGroupId() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.GROUP_ID);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取语言
     */
    public String getLanguage() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.LANGUAGE);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取与该表现连带的语言
     */
    public String getAssociatedLanguage() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.ASSOCIATED_LANGUAGE);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取名称
     */
    public String getName() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.NAME);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 是不是默认的选择
     */
    public boolean isDefaultSelect() throws MalformedPlaylistException {
        boolean state;
        if (!mAttributeList.containsAttribute(Attribute.Name.DEFAULT)) {
            /**
             * 默认值：否
             */
            state = false;
        }
        else {
            Attribute attribute = mAttributeList.get(Attribute.Name.DEFAULT);
            state = attribute.getBooleanValue();
        }

        return state;
    }

    /**
     * 是不是自动的选择
     */
    public boolean isAutoSelect() throws MalformedPlaylistException {
        boolean state;
        if (!mAttributeList.containsAttribute(Attribute.Name.AUTO_SELECT)) {
            /**
             * 默认值：否
             */
            state = false;
        }
        else {
            Attribute attribute = mAttributeList.get(Attribute.Name.AUTO_SELECT);
            state = attribute.getBooleanValue();
        }

        return state;
    }

    /**
     * 是不是强制的选择
     */
    public boolean isForcedSelect() throws MalformedPlaylistException {
        boolean state;
        if (!mAttributeList.containsAttribute(Attribute.Name.FORCED)) {
            /**
             * 默认值：否
             */
            state = false;
        }
        else {
            Attribute attribute = mAttributeList.get(Attribute.Name.FORCED);
            state = attribute.getBooleanValue();
        }

        return state;
    }

    /**
     * 获取流内（CC字幕轨道）的id
     */
    public String getInStreamId() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.IN_STREAM_ID);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取特性
     */
    public String[] getCharacteristics() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.CHARACTERISTICS);
        return attribute.getQuotedStringValue().splitContent(",");
    }

    /**
     * 获取音频声道数
     */
    public int getAudioChannels() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.CHANNELS);

        String[] parameters = attribute.getQuotedStringValue().splitContent("/");
        return Integer.parseInt(parameters[0]);
    }

    @Override
    public String toString() {
        return Tag.Name.MEDIA + ":" + mAttributeList.toString();
    }
}
