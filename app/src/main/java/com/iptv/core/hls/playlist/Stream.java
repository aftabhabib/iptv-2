package com.iptv.core.hls.playlist;

import com.iptv.core.hls.exception.MalformedFormatException;
import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.datatype.QuotedString;
import com.iptv.core.hls.playlist.datatype.Resolution;

/**
 * 流
 */
public final class Stream {
    private AttributeList mAttributeList;
    private String mUri = null;

    /**
     * 构造函数
     */
    public Stream() {
        mAttributeList = new AttributeList();
    }

    /**
     * 构造函数
     */
    public Stream(AttributeList attributeList) {
        mAttributeList = attributeList;
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
        Attribute attribute = Attribute.create(Attribute.Name.AUDIO, new QuotedString(groupId));
        mAttributeList.put(attribute);
    }

    /**
     * 设置视频（展示）组的id
     */
    public void setVideoGroupId(String groupId) {
        Attribute attribute = Attribute.create(Attribute.Name.VIDEO, new QuotedString(groupId));
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
     * 是否定义了属性
     */
    public boolean contains(String attributeName) {
        return mAttributeList.containsAttribute(attributeName);
    }

    /**
     * 获取带宽
     */
    public int getBandwidth() throws MalformedFormatException {
        Attribute attribute = mAttributeList.get(Attribute.Name.BANDWIDTH);
        return attribute.getIntegerValue();
    }

    /**
     * 获取平均带宽
     */
    public int getAvgBandwidth() throws MalformedFormatException {
        Attribute attribute = mAttributeList.get(Attribute.Name.AVG_BANDWIDTH);
        return attribute.getIntegerValue();
    }

    /**
     * 获取媒体编码格式
     */
    public String[] getCodecs() throws MalformedFormatException {
        Attribute attribute = mAttributeList.get(Attribute.Name.CODECS);
        return attribute.getQuotedStringValue().splitContent(",");
    }

    /**
     * 获取视频图像宽
     */
    public int getVideoWidth() throws MalformedFormatException {
        Attribute attribute = mAttributeList.get(Attribute.Name.RESOLUTION);
        return attribute.getResolutionValue().getWidth();
    }

    /**
     * 获取视频图像高
     */
    public int getVideoHeight() throws MalformedFormatException {
        Attribute attribute = mAttributeList.get(Attribute.Name.RESOLUTION);
        return attribute.getResolutionValue().getHeight();
    }

    /**
     * 获取视频帧率
     */
    public float getVideoFrameRate() throws MalformedFormatException {
        Attribute attribute = mAttributeList.get(Attribute.Name.FRAME_RATE);
        return attribute.getFloatValue();
    }

    /**
     * 获取HDCP层次
     */
    public String getHDCPLevel() throws MalformedFormatException {
        Attribute attribute = mAttributeList.get(Attribute.Name.HDCP_LEVEL);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取音频（展示）组的id
     */
    public String getAudioGroupId() throws MalformedFormatException {
        Attribute attribute = mAttributeList.get(Attribute.Name.AUDIO);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取视频（展示）组的id
     */
    public String getVideoGroupId() throws MalformedFormatException {
        Attribute attribute = mAttributeList.get(Attribute.Name.VIDEO);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取字幕（展示）组的id
     */
    public String getSubtitleGroupId() throws MalformedFormatException {
        Attribute attribute = mAttributeList.get(Attribute.Name.SUBTITLES);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取CC字幕（展示）组的id
     */
    public String getClosedCaptionGroupId() throws MalformedFormatException {
        Attribute attribute = mAttributeList.get(Attribute.Name.CLOSED_CAPTIONS);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        mUri = uri;
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mUri;
    }
}
