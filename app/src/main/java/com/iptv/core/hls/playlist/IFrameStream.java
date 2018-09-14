package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.attribute.AttributeName;
import com.iptv.core.hls.playlist.datatype.EnumeratedString;
import com.iptv.core.hls.playlist.datatype.Resolution;

/**
 * I帧流（快速浏览）
 */
public final class IFrameStream {
    private AttributeList mAttributeList = new AttributeList();

    /**
     * 构造函数
     */
    public IFrameStream() {
        /**
         * nothing
         */
    }

    /**
     * 设置带宽
     */
    public void setBandwidth(int bandwidth) {
        if (bandwidth <= 0) {
            throw new IllegalArgumentException("invalid bandwidth");
        }

        mAttributeList.putInteger(AttributeName.BANDWIDTH, bandwidth);
    }

    /**
     * 设置平均带宽
     */
    public void setAvgBandwidth(int bandwidth) {
        if (bandwidth <= 0) {
            throw new IllegalArgumentException("invalid bandwidth");
        }

        mAttributeList.putInteger(AttributeName.AVG_BANDWIDTH, bandwidth);
    }

    /**
     * 设置媒体编码格式
     */
    public void setCodec(String codec) {
        if ((codec == null) || codec.isEmpty()) {
            throw new IllegalArgumentException("invalid codec");
        }

        mAttributeList.putString(AttributeName.CODECS, codec);
    }

    /**
     * 设置视频分辨率
     */
    public void setVideoResolution(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("invalid width or height");
        }

        mAttributeList.putResolution(AttributeName.RESOLUTION, new Resolution(width, height));
    }

    /**
     * 设置HDCP层次
     */
    public void setHDCPLevel(String level) {
        if ((level == null) || !isValidLevel(level)) {
            throw new IllegalArgumentException("invalid level");
        }

        mAttributeList.putString(AttributeName.HDCP_LEVEL, level);
    }

    /**
     * 是否有效的HDCP层次
     */
    private static boolean isValidLevel(String level) {
        return level.equals(EnumeratedString.NONE)
                || level.equals(EnumeratedString.TYPE_0);
    }

    /**
     * 设置视频（展示）组的id
     */
    public void setVideoGroupId(String groupId) {
        if ((groupId == null) || groupId.isEmpty()) {
            throw new IllegalArgumentException("invalid group-id");
        }

        mAttributeList.putString(AttributeName.VIDEO, groupId);
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        if ((uri == null) || uri.isEmpty()) {
            throw new IllegalArgumentException("invalid uri");
        }

        mAttributeList.putString(AttributeName.URI, uri);
    }

    /**
     * 是否定义了带宽
     */
    public boolean containsBandwidth() {
        return mAttributeList.containsName(AttributeName.BANDWIDTH);
    }

    /**
     * 是否定义了平均带宽
     */
    public boolean containsAvgBandwidth() {
        return mAttributeList.containsName(AttributeName.AVG_BANDWIDTH);
    }

    /**
     * 是否定义了媒体编码格式
     */
    public boolean containsCodec() {
        return mAttributeList.containsName(AttributeName.CODECS);
    }

    /**
     * 是否定义了视频分辨率
     */
    public boolean containsVideoResolution() {
        return mAttributeList.containsName(AttributeName.RESOLUTION);
    }

    /**
     * 是否定义了HDCP层次
     */
    public boolean containsHDCPLevel() {
        return mAttributeList.containsName(AttributeName.HDCP_LEVEL);
    }

    /**
     * 是否定义了视频（展示）组
     */
    public boolean containsVideoGroupId() {
        return mAttributeList.containsName(AttributeName.VIDEO);
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mAttributeList.containsName(AttributeName.URI);
    }

    /**
     * 获取带宽
     */
    public int getBandwidth() {
        return mAttributeList.getInteger(AttributeName.BANDWIDTH);
    }

    /**
     * 获取平均带宽
     */
    public int getAvgBandwidth() {
        return mAttributeList.getInteger(AttributeName.AVG_BANDWIDTH);
    }

    /**
     * 获取媒体编码格式
     */
    public String getCodec() {
        return mAttributeList.getString(AttributeName.CODECS);
    }

    /**
     * 获取视频图像宽
     */
    public int getVideoWidth() {
        Resolution res = mAttributeList.getResolution(AttributeName.RESOLUTION);
        return res.getWidth();
    }

    /**
     * 获取视频图像高
     */
    public int getVideoHeight() {
        Resolution res = mAttributeList.getResolution(AttributeName.RESOLUTION);
        return res.getHeight();
    }

    /**
     * 获取HDCP层次
     */
    public String getHDCPLevel() {
        return mAttributeList.getString(AttributeName.HDCP_LEVEL);
    }

    /**
     * 获取视频（展示）组的id
     */
    public String getVideoGroupId() {
        return mAttributeList.getString(AttributeName.VIDEO);
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mAttributeList.getString(AttributeName.URI);
    }
}
