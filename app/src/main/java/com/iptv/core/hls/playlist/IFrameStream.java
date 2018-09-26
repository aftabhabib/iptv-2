package com.iptv.core.hls.playlist;

import com.iptv.core.hls.exception.MalformedPlaylistException;
import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.datatype.QuotedString;
import com.iptv.core.hls.playlist.datatype.Resolution;
import com.iptv.core.hls.playlist.tag.Tag;

/**
 * I帧流（快速浏览）
 */
public final class IFrameStream {
    private AttributeList mAttributeList;

    /**
     * 构造函数
     */
    public IFrameStream() {
        mAttributeList = new AttributeList();
    }

    /**
     * 构造函数
     */
    public IFrameStream(AttributeList attributeList) {
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
    public void setCodec(String codec) {
        Attribute attribute = Attribute.create(Attribute.Name.CODECS, new QuotedString(codec));
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
     * 设置HDCP层次
     */
    public void setHDCPLevel(String level) {
        Attribute attribute = Attribute.create(Attribute.Name.HDCP_LEVEL, level);
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
     * 设置uri
     */
    public void setUri(String uri) {
        Attribute attribute = Attribute.create(Attribute.Name.URI, new QuotedString(uri));
        mAttributeList.put(attribute);
    }

    /**
     * 是否定义了属性
     */
    public boolean containsAttribute(String attributeName) {
        return mAttributeList.containsAttribute(attributeName);
    }

    /**
     * 获取带宽
     */
    public int getBandwidth() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.BANDWIDTH);
        return attribute.getIntegerValue();
    }

    /**
     * 获取平均带宽
     */
    public int getAvgBandwidth() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.AVG_BANDWIDTH);
        return attribute.getIntegerValue();
    }

    /**
     * 获取媒体编码格式
     */
    public String getCodec() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.CODECS);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取视频图像宽
     */
    public int getVideoWidth() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.RESOLUTION);
        return attribute.getResolutionValue().getWidth();
    }

    /**
     * 获取视频图像高
     */
    public int getVideoHeight() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.RESOLUTION);
        return attribute.getResolutionValue().getHeight();
    }

    /**
     * 获取HDCP层次
     */
    public String getHDCPLevel() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.HDCP_LEVEL);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取视频（展示）组的id
     */
    public String getVideoGroupId() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.VIDEO);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取uri
     */
    public String getUri() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.URI);
        return attribute.getQuotedStringValue().getContent();
    }

    @Override
    public String toString() {
        return Tag.Name.I_FRAME_STREAM_INF + ":" + mAttributeList.toString();
    }
}
