package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.datatype.AudioChannelParameter;
import com.iptv.core.hls.playlist.datatype.EnumeratedString;
import com.iptv.core.hls.playlist.datatype.QuotedString;

/**
 * 媒体标签
 */
public final class MediaTag extends Tag {
    private AttributeList mAttributeList;

    /**
     * 构造函数
     */
    public MediaTag(AttributeList attributeList) {
        super(Name.MEDIA);

        mAttributeList = attributeList;
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
        return attribute.getStringValue();
    }

    /**
     * 获取uri
     */
    public String getUri() {
        Attribute attribute = mAttributeList.get(Attribute.Name.URI);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取媒体所属（展示）组的id
     */
    public String getGroupId() {
        Attribute attribute = mAttributeList.get(Attribute.Name.GROUP_ID);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取语言
     */
    public String getLanguage() {
        Attribute attribute = mAttributeList.get(Attribute.Name.LANGUAGE);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取与该表现连带的语言
     */
    public String getAssociatedLanguage() {
        Attribute attribute = mAttributeList.get(Attribute.Name.ASSOCIATED_LANGUAGE);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取名称
     */
    public String getTitle() {
        Attribute attribute = mAttributeList.get(Attribute.Name.NAME);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 是不是默认的选择
     */
    public String isDefaultSelect() {
        Attribute attribute = mAttributeList.get(Attribute.Name.DEFAULT);
        return attribute.getStringValue();
    }

    /**
     * 是不是自动的选择
     */
    public String isAutoSelect() {
        Attribute attribute = mAttributeList.get(Attribute.Name.AUTO_SELECT);
        return attribute.getStringValue();
    }

    /**
     * 是不是强制的选择
     */
    public String isForcedSelect() {
        Attribute attribute = mAttributeList.get(Attribute.Name.FORCED);
        return attribute.getStringValue();
    }

    /**
     * 获取流内（CC字幕轨道）的id
     */
    public String getInStreamId() {
        Attribute attribute = mAttributeList.get(Attribute.Name.IN_STREAM_ID);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取特性
     */
    public String[] getCharacteristics() {
        Attribute attribute = mAttributeList.get(Attribute.Name.CHARACTERISTICS);
        return attribute.getQuotedStringValue().getContent().split(",");
    }

    /**
     * 获取音频声道数
     */
    public AudioChannelParameter getAudioChannels() {
        Attribute attribute = mAttributeList.get(Attribute.Name.CHANNELS);
        return AudioChannelParameter.valueOf(attribute.getQuotedStringValue().getContent());
    }

    @Override
    public int getProtocolVersion() {
        int protocolVersion = 4;

        if (containsAttribute(Attribute.Name.FORCED)
                || containsAttribute(Attribute.Name.CHARACTERISTICS)) {
            protocolVersion = 5;
        }

        if (containsAttribute(Attribute.Name.ASSOCIATED_LANGUAGE)
                || containsAttribute(Attribute.Name.IN_STREAM_ID)) {
            protocolVersion = 6;
        }

        if (containsAttribute(Attribute.Name.CHANNELS)) {
            protocolVersion = 7;
        }

        return protocolVersion;
    }

    @Override
    public String toString() {
        return mName + ":" + mAttributeList.toString();
    }

    /**
     * 建造者
     */
    public static class Builder {
        private AttributeList mAttributeList = new AttributeList();

        /**
         * 构造函数
         */
        public Builder() {
            /**
             * nothing
             */
        }

        /**
         * 设置类型
         */
        public void setType(String type) {
            Attribute attribute = new Attribute(Attribute.Name.TYPE, type);
            mAttributeList.put(attribute);
        }

        /**
         * 设置uri
         */
        public void setUri(String uri) {
            Attribute attribute = new Attribute(Attribute.Name.URI, new QuotedString(uri));
            mAttributeList.put(attribute);
        }

        /**
         * 设置媒体所属（展示）组的id
         */
        public void setGroupId(String groupId) {
            Attribute attribute = new Attribute(Attribute.Name.GROUP_ID,
                    new QuotedString(groupId));
            mAttributeList.put(attribute);
        }

        /**
         * 设置语言
         */
        public void setLanguage(String language) {
            Attribute attribute = new Attribute(Attribute.Name.LANGUAGE,
                    new QuotedString(language));
            mAttributeList.put(attribute);
        }

        /**
         * 设置连带的语言
         */
        public void setAssociatedLanguage(String language) {
            Attribute attribute = new Attribute(Attribute.Name.ASSOCIATED_LANGUAGE,
                    new QuotedString(language));
            mAttributeList.put(attribute);
        }

        /**
         * 设置名称
         */
        public void setName(String name) {
            Attribute attribute = new Attribute(Attribute.Name.NAME, new QuotedString(name));
            mAttributeList.put(attribute);
        }

        /**
         * 设置（该表现）是默认的选择
         */
        public void setDefaultSelect(boolean state) {
            Attribute attribute = new Attribute(Attribute.Name.DEFAULT,
                    state ? EnumeratedString.YES : EnumeratedString.NO);
            mAttributeList.put(attribute);
        }

        /**
         * 设置（该表现）是自动的选择
         */
        public void setAutoSelect(boolean state) {
            Attribute attribute = new Attribute(Attribute.Name.AUTO_SELECT,
                    state ? EnumeratedString.YES : EnumeratedString.NO);
            mAttributeList.put(attribute);
        }

        /**
         * 设置（该表现）是强制的选择
         */
        public void setForcedSelect(boolean state) {
            Attribute attribute = new Attribute(Attribute.Name.FORCED,
                    state ? EnumeratedString.YES : EnumeratedString.NO);
            mAttributeList.put(attribute);
        }

        /**
         * 设置CC字幕轨道的id
         */
        public void setClosedCaptionStreamId(String streamId) {
            Attribute attribute = new Attribute(Attribute.Name.IN_STREAM_ID,
                    new QuotedString(streamId));
            mAttributeList.put(attribute);
        }

        /**
         * 设置特性
         */
        public void setCharacteristics(String[] characteristics) {
            Attribute attribute = new Attribute(Attribute.Name.CHARACTERISTICS,
                    new QuotedString(characteristics, ','));
            mAttributeList.put(attribute);
        }

        /**
         * 设置音频声道数
         */
        public void setAudioChannels(AudioChannelParameter parameter) {
            Attribute attribute = new Attribute(Attribute.Name.CHANNELS,
                    new QuotedString(parameter.toString()));
            mAttributeList.put(attribute);
        }

        /**
         * 创建
         */
        public MediaTag build() {
            return new MediaTag(mAttributeList);
        }
    }
}
