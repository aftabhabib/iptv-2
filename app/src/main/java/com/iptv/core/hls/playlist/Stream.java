package com.iptv.core.hls.playlist;

import java.util.HashMap;
import java.util.Map;

/**
 * 流
 */
public final class Stream {
    /**
     * 属性
     */
    private static final String ATTR_BANDWIDTH = "BANDWIDTH";
    private static final String ATTR_AVG_BANDWIDTH = "AVERAGE-BANDWIDTH";
    private static final String ATTR_CODECS = "CODECS";
    private static final String ATTR_RESOLUTION = "RESOLUTION";
    private static final String ATTR_FRAME_RATE = "FRAME-RATE";
    private static final String ATTR_HDCP_LEVEL = "HDCP-LEVEL";
    private static final String ATTR_AUDIO = "AUDIO";
    private static final String ATTR_VIDEO = "VIDEO";
    private static final String ATTR_SUBTITLES = "SUBTITLES";
    private static final String ATTR_CLOSED_CAPTIONS = "CLOSED-CAPTIONS";

    /**
     * HDCP类型
     */
    public static final String HDCP_TYPE0 = "TYPE-0";

    private Map<String, String> mAttributes = new HashMap<String, String>();
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
     * 设置属性
     */
    void setAttribute(String name, String value) {
        mAttributes.put(name, value);
    }

    /**
     * 设置带宽
     */
    public void setBandwidth(int bandwidth) {
        mAttributes.put(ATTR_BANDWIDTH, AttributeValue.writeDecimalInteger(bandwidth));
    }

    /**
     * 设置平均带宽
     */
    public void setAvgBandwidth(int avgBandwidth) {
        mAttributes.put(ATTR_AVG_BANDWIDTH, AttributeValue.writeDecimalInteger(avgBandwidth));
    }

    /**
     * 设置媒体编码格式
     */
    public void setMediaFormats(String[] formats) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < formats.length; i++) {
            if (i > 0) {
                buffer.append(",");
            }

            buffer.append(formats[i]);
        }

        mAttributes.put(ATTR_CODECS, AttributeValue.writeQuotedString(buffer.toString()));
    }

    /**
     * 设置视频分辨率
     */
    public void setVideoResolution(int width, int height) {
        VideoResolution res = new VideoResolution(width, height);
        mAttributes.put(ATTR_RESOLUTION, AttributeValue.writeDecimalResolution(res));
    }

    /**
     * 设置视频帧率
     */
    public void setVideoFrameRate(float frameRate) {
        mAttributes.put(ATTR_FRAME_RATE, AttributeValue.writeDecimalFloatingPoint(frameRate));
    }

    /**
     * 设置HDCP类型
     */
    public void setHDCPType(String type) {
        if (!type.equals(AttributeValue.ENUM_STRING_NONE)
                && !type.equals(HDCP_TYPE0)) {
            throw new IllegalArgumentException("invalid HDCP type");
        }

        mAttributes.put(ATTR_HDCP_LEVEL, AttributeValue.writeEnumeratedString(type));
    }

    /**
     * 设置音频（展示）组的id
     */
    public void setAudioGroupId(String groupId) {
        mAttributes.put(ATTR_AUDIO, AttributeValue.writeQuotedString(groupId));
    }

    /**
     * 设置视频（展示）组的id
     */
    public void setVideoGroupId(String groupId) {
        mAttributes.put(ATTR_VIDEO, AttributeValue.writeQuotedString(groupId));
    }

    /**
     * 设置字幕（展示）组的id
     */
    public void setSubtitleGroupId(String groupId) {
        mAttributes.put(ATTR_SUBTITLES, AttributeValue.writeQuotedString(groupId));
    }

    /**
     * 设置CC字幕（展示）组的id
     */
    public void setClosedCaptionGroupId(String groupId) {
        mAttributes.put(ATTR_CLOSED_CAPTIONS, AttributeValue.writeQuotedString(groupId));
    }

    /**
     * 是否定义了带宽
     */
    public boolean containsBandwidth() {
        return mAttributes.containsKey(ATTR_BANDWIDTH);
    }

    /**
     * 是否定义了平均带宽
     */
    public boolean containsAvgBandwidth() {
        return mAttributes.containsKey(ATTR_AVG_BANDWIDTH);
    }

    /**
     * 是否定义了媒体编码格式
     */
    public boolean containsMediaFormat() {
        return mAttributes.containsKey(ATTR_CODECS);
    }

    /**
     * 是否定义了视频分辨率
     */
    public boolean containsVideoResolution() {
        return mAttributes.containsKey(ATTR_RESOLUTION);
    }

    /**
     * 是否定义了视频帧率
     */
    public boolean containsVideoFrameRate() {
        return mAttributes.containsKey(ATTR_FRAME_RATE);
    }

    /**
     * 是否定义了音频（展示）组
     */
    public boolean containsAudioGroupId() {
        return mAttributes.containsKey(ATTR_AUDIO);
    }

    /**
     * 是否定义了视频（展示）组
     */
    public boolean containsVideoGroupId() {
        return mAttributes.containsKey(ATTR_VIDEO);
    }

    /**
     * 是否定义了字幕（展示）组
     */
    public boolean containsSubtitleGroupId() {
        return mAttributes.containsKey(ATTR_SUBTITLES);
    }

    /**
     * 是否定义了CC字幕（展示）组
     */
    public boolean containsClosedCaptionGroupId() {
        return mAttributes.containsKey(ATTR_CLOSED_CAPTIONS);
    }

    /**
     * 获取带宽
     */
    public int getBandwidth() {
        if (!containsBandwidth()) {
            throw new IllegalStateException("no BANDWIDTH attribute");
        }

        return AttributeValue.readDecimalInteger(mAttributes.get(ATTR_BANDWIDTH));
    }

    /**
     * 获取平均带宽
     */
    public int getAvgBandwidth() {
        if (!containsAvgBandwidth()) {
            throw new IllegalStateException("no AVERAGE-BANDWIDTH attribute");
        }

        return AttributeValue.readDecimalInteger(mAttributes.get(ATTR_AVG_BANDWIDTH));
    }

    /**
     * 获取媒体编码格式
     */
    public String[] getMediaFormats() {
        if (!containsMediaFormat()) {
            throw new IllegalStateException("no CODECS attribute");
        }

        String content = AttributeValue.readQuotedString(mAttributes.get(ATTR_CODECS));

        /**
         * comma-separated list of formats
         */
        String[] formats;
        if (content.contains(",")) {
            formats = content.split(",");
        }
        else {
            formats = new String[] { content };
        }

        return formats;
    }

    /**
     * 获取视频图像宽
     */
    public int getVideoWidth() {
        if (!containsVideoResolution()) {
            throw new IllegalStateException("no RESOLUTION attribute");
        }

        VideoResolution res = AttributeValue.readDecimalResolution(mAttributes.get(ATTR_RESOLUTION));
        return res.getWidth();
    }

    /**
     * 获取视频图像高
     */
    public int getVideoHeight() {
        if (!containsVideoResolution()) {
            throw new IllegalStateException("no RESOLUTION attribute");
        }

        VideoResolution res = AttributeValue.readDecimalResolution(mAttributes.get(ATTR_RESOLUTION));
        return res.getHeight();
    }

    /**
     * 获取视频帧率
     */
    public float getVideoFrameRate() {
        if (!containsVideoFrameRate()) {
            throw new IllegalStateException("no FRAME-RATE attribute");
        }

        return AttributeValue.readDecimalFloatingPoint(mAttributes.get(ATTR_FRAME_RATE));
    }

    /**
     * 获取HDCP的类型
     */
    public String getHDCPType() {
        String type;
        if (!mAttributes.containsKey(ATTR_HDCP_LEVEL)) {
            type = AttributeValue.ENUM_STRING_NONE;
        }
        else {
            type = AttributeValue.readEnumeratedString(mAttributes.get(ATTR_HDCP_LEVEL));
        }

        return type;
    }

    /**
     * 获取音频（展示）组的id
     */
    public String getAudioGroupId() {
        if (!containsAudioGroupId()) {
            throw new IllegalStateException("no AUDIO attribute");
        }

        return AttributeValue.readQuotedString(mAttributes.get(ATTR_AUDIO));
    }

    /**
     * 获取视频（展示）组的id
     */
    public String getVideoGroupId() {
        if (!containsVideoGroupId()) {
            throw new IllegalStateException("no VIDEO attribute");
        }

        return AttributeValue.readQuotedString(mAttributes.get(ATTR_VIDEO));
    }

    /**
     * 获取字幕（展示）组的id
     */
    public String getSubtitleGroupId() {
        if (!containsSubtitleGroupId()) {
            throw new IllegalStateException("no SUBTITLES attribute");
        }

        return AttributeValue.readQuotedString(mAttributes.get(ATTR_SUBTITLES));
    }

    /**
     * 获取CC字幕（展示）组的id
     */
    public String getClosedCaptionGroupId() {
        if (!containsClosedCaptionGroupId()) {
            throw new IllegalStateException("no CLOSED-CAPTIONS attribute");
        }

        return AttributeValue.readQuotedString(mAttributes.get(ATTR_CLOSED_CAPTIONS));
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
        }

        return builder.toString();
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
