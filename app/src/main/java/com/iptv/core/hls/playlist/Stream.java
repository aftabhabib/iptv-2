package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.attribute.AttributeName;
import com.iptv.core.hls.playlist.datatype.EnumeratedString;
import com.iptv.core.hls.playlist.datatype.Resolution;

/**
 * 流
 */
public final class Stream {
    private AttributeList mAttributeList = new AttributeList();
    private String mUri = null;

    /**
     * 构造函数
     */
    public Stream() {
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
    public void setCodecs(String[] codecs) {
        if ((codecs == null) || (codecs.length == 0)) {
            throw new IllegalArgumentException("invalid codecs");
        }

        mAttributeList.putStringArray(AttributeName.CODECS, codecs);
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
     * 设置视频帧率
     */
    public void setVideoFrameRate(float frameRate) {
        if (frameRate <= 0.0f || frameRate > 60.0f) {
            throw new IllegalArgumentException("invalid frame rate");
        }

        mAttributeList.putFloat(AttributeName.FRAME_RATE, frameRate);
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
     * 设置音频（展示）组的id
     */
    public void setAudioGroupId(String groupId) {
        if ((groupId == null) || groupId.isEmpty()) {
            throw new IllegalArgumentException("invalid group-id");
        }

        mAttributeList.putString(AttributeName.AUDIO, groupId);
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
     * 设置字幕（展示）组的id
     */
    public void setSubtitleGroupId(String groupId) {
        if ((groupId == null) || groupId.isEmpty()) {
            throw new IllegalArgumentException("invalid group-id");
        }

        mAttributeList.putString(AttributeName.SUBTITLES, groupId);
    }

    /**
     * 设置CC字幕（展示）组的id
     */
    public void setClosedCaptionGroupId(String groupId) {
        if ((groupId == null) || groupId.isEmpty()) {
            throw new IllegalArgumentException("invalid group-id");
        }

        mAttributeList.putString(AttributeName.CLOSED_CAPTIONS, groupId);
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        if ((uri == null) || uri.isEmpty()) {
            throw new IllegalArgumentException("invalid uri");
        }

        mUri = uri;
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
    public boolean containsCodecs() {
        return mAttributeList.containsName(AttributeName.CODECS);
    }

    /**
     * 是否定义了视频分辨率
     */
    public boolean containsVideoResolution() {
        return mAttributeList.containsName(AttributeName.RESOLUTION);
    }

    /**
     * 是否定义了视频帧率
     */
    public boolean containsVideoFrameRate() {
        return mAttributeList.containsName(AttributeName.FRAME_RATE);
    }

    /**
     * 是否定义了HDCP层次
     */
    public boolean containsHDCPLevel() {
        return mAttributeList.containsName(AttributeName.HDCP_LEVEL);
    }

    /**
     * 是否定义了音频（展示）组
     */
    public boolean containsAudioGroupId() {
        return mAttributeList.containsName(AttributeName.AUDIO);
    }

    /**
     * 是否定义了视频（展示）组
     */
    public boolean containsVideoGroupId() {
        return mAttributeList.containsName(AttributeName.VIDEO);
    }

    /**
     * 是否定义了字幕（展示）组
     */
    public boolean containsSubtitleGroupId() {
        return mAttributeList.containsName(AttributeName.SUBTITLES);
    }

    /**
     * 是否定义了CC字幕（展示）组
     */
    public boolean containsClosedCaptionGroupId() {
        return mAttributeList.containsName(AttributeName.CLOSED_CAPTIONS);
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mUri != null;
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
    public String[] getCodecs() {
        return mAttributeList.getStringArray(AttributeName.CODECS);
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
     * 获取视频帧率
     */
    public float getVideoFrameRate() {
        return mAttributeList.getFloat(AttributeName.FRAME_RATE);
    }

    /**
     * 获取HDCP层次
     */
    public String getHDCPLevel() {
        return mAttributeList.getString(AttributeName.HDCP_LEVEL);
    }

    /**
     * 获取音频（展示）组的id
     */
    public String getAudioGroupId() {
        return mAttributeList.getString(AttributeName.AUDIO);
    }

    /**
     * 获取视频（展示）组的id
     */
    public String getVideoGroupId() {
        return mAttributeList.getString(AttributeName.VIDEO);
    }

    /**
     * 获取字幕（展示）组的id
     */
    public String getSubtitleGroupId() {
        return mAttributeList.getString(AttributeName.SUBTITLES);
    }

    /**
     * 获取CC字幕（展示）组的id
     */
    public String getClosedCaptionGroupId() {
        return mAttributeList.getString(AttributeName.CLOSED_CAPTIONS);
    }

    /**
     * 获取uri
     */
    public String getUri() {
        if (!containsUri()) {
            throw new IllegalStateException("no uri");
        }

        return mUri;
    }
}
