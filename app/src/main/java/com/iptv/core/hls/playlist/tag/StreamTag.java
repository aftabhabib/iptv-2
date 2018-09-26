package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.datatype.QuotedString;
import com.iptv.core.hls.playlist.datatype.Resolution;

/**
 * 流标签
 */
public final class StreamTag extends Tag {
    private AttributeList mAttributeList;

    /**
     * 构造函数
     */
    public StreamTag(AttributeList attributeList) {
        super(Name.STREAM_INF);

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
         * 设置带宽
         */
        public void setBandwidth(int bandwidth) {
            Attribute attribute = Attribute.create(Attribute.Name.BANDWIDTH, bandwidth);
            mAttributeList.put(attribute);
        }

        /**
         * 设置平均带宽
         */
        public void setAvgBandwidth(int bandwidth) {
            Attribute attribute = Attribute.create(Attribute.Name.AVG_BANDWIDTH, bandwidth);
            mAttributeList.put(attribute);
        }

        /**
         * 设置媒体编码格式
         */
        public void setCodecs(String[] codecs) {
            Attribute attribute = Attribute.create(
                    Attribute.Name.CODECS, new QuotedString(codecs, ','));
            mAttributeList.put(attribute);
        }

        /**
         * 设置视频分辨率
         */
        public void setVideoResolution(int width, int height) {
            Attribute attribute = Attribute.create(
                    Attribute.Name.RESOLUTION, new Resolution(width, height));
            mAttributeList.put(attribute);
        }

        /**
         * 设置视频帧率
         */
        public void setVideoFrameRate(float frameRate) {
            Attribute attribute = Attribute.create(Attribute.Name.FRAME_RATE, frameRate);
            mAttributeList.put(attribute);
        }

        /**
         * 设置HDCP层次
         */
        public void setHDCPLevel(String level) {
            Attribute attribute = Attribute.create(Attribute.Name.HDCP_LEVEL, level);
            mAttributeList.put(attribute);
        }

        /**
         * 设置音频（展示）组的id
         */
        public void setAudioGroupId(String groupId) {
            Attribute attribute = Attribute.create(
                    Attribute.Name.AUDIO, new QuotedString(groupId));
            mAttributeList.put(attribute);
        }

        /**
         * 设置视频（展示）组的id
         */
        public void setVideoGroupId(String groupId) {
            Attribute attribute = Attribute.create(
                    Attribute.Name.VIDEO, new QuotedString(groupId));
            mAttributeList.put(attribute);
        }

        /**
         * 设置字幕（展示）组的id
         */
        public void setSubtitleGroupId(String groupId) {
            Attribute attribute = Attribute.create(
                    Attribute.Name.SUBTITLES, new QuotedString(groupId));
            mAttributeList.put(attribute);
        }

        /**
         * 设置CC字幕（展示）组的id
         */
        public void setClosedCaptionGroupId(String groupId) {
            Attribute attribute = Attribute.create(
                    Attribute.Name.CLOSED_CAPTIONS, new QuotedString(groupId));
            mAttributeList.put(attribute);
        }

        /**
         * 创建
         */
        public StreamTag build() {
            return new StreamTag(mAttributeList);
        }
    }
}
