package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.attribute.AttributeList;
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
     * 获取属性列表
     */
    public AttributeList getAttributeList() {
        return mAttributeList;
    }

    @Override
    protected boolean containsValue() {
        return true;
    }

    @Override
    protected String getStringValue() {
        return mAttributeList.toString();
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
            Attribute attribute = Attribute.create(
                    Attribute.Name.GROUP_ID, new QuotedString(groupId));
            mAttributeList.put(attribute);
        }

        /**
         * 设置语言
         */
        public void setLanguage(String language) {
            Attribute attribute = Attribute.create(
                    Attribute.Name.LANGUAGE, new QuotedString(language));
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
         * 创建
         */
        public MediaTag build() {
            return new MediaTag(mAttributeList);
        }
    }
}
