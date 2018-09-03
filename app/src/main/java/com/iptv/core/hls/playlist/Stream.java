package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;
import com.iptv.core.utils.MalformedFormatException;

/**
 * 流
 */
public final class Stream {
    /**
     * 属性
     */
    private static final String ATTR_BANDWIDTH = "BANDWIDTH";
    private static final String ATTR_CODECS = "CODECS";
    private static final String ATTR_AUDIO = "AUDIO";
    private static final String ATTR_VIDEO = "VIDEO";
    private static final String ATTR_SUBTITLES = "SUBTITLES";

    private int mBandwidth = -1;
    private String mUri = null;
    private MetaData mMetaData = new MetaData();

    private Media[] mAudioRenditions = null;
    private Media[] mVideoRenditions = null;
    private Media[] mSubtitleRenditions = null;

    /**
     * 构造函数
     */
    public Stream(String[] attributes) throws MalformedFormatException {
        for (String attribute : attributes) {
            String[] result = attribute.split("=");
            parseAttribute(result[0], result[1]);
        }

        if (mBandwidth == -1) {
            throw new MalformedFormatException("BANDWIDTH is required");
        }

        if (!mMetaData.containsKey("audio-format") && !mMetaData.containsKey("video-format")) {
            throw new MalformedFormatException("CODECS is required");
        }
    }

    /**
     * 解析属性
     */
    private void parseAttribute(String name, String value) {
        if (name.equals(ATTR_BANDWIDTH)) {
            mBandwidth = Integer.parseInt(value);
        }
        else if (name.equals(ATTR_CODECS)) {
            String[] formats;
            if (value.contains(",")) {
                formats = value.split(",");
            }
            else {
                formats = new String[] { value };
            }

            for (String format : formats) {
                if (Codec.isAudioFormat(format)) {
                    mMetaData.putString("audio-format", format);
                }
                else if (Codec.isVideoFormat(format)) {
                    mMetaData.putString("video-format", format);
                }
                else {
                    /**
                     * ignore
                     */
                }
            }
        }
        else if (name.equals(ATTR_AUDIO)) {
            mMetaData.putString(ATTR_AUDIO, value);
        }
        else if (name.equals(ATTR_VIDEO)) {
            mMetaData.putString(ATTR_VIDEO, value);
        }
        else if (name.equals(ATTR_SUBTITLES)) {
            mMetaData.putString(ATTR_SUBTITLES, value);
        }
        else {
            /**
             * ignore
             */
        }
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        mUri = uri;
    }

    /**
     * 设置音频表现
     */
    public void setAudioRenditions(Media[] renditions) {
        mAudioRenditions = renditions;
    }

    /**
     * 设置视频表现
     */
    public void setVideoRenditions(Media[] renditions) {
        mVideoRenditions = renditions;
    }

    /**
     * 设置字幕表现
     */
    public void setSubtitleRenditions(Media[] renditions) {
        mSubtitleRenditions = renditions;
    }

    /**
     * 获取带宽
     */
    public int getBandwidth() {
        return mBandwidth;
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mUri;
    }

    /**
     * 是否定义了音频格式
     */
    public boolean containsAudioFormat() {
        return mMetaData.containsKey("audio-format");
    }

    /**
     * 获取音频格式
     */
    public String getAudioFormat() {
        if (!containsAudioFormat()) {
            throw new IllegalStateException("no AUDIO-CODEC");
        }

        return mMetaData.getString("audio-format");
    }

    /**
     * 是否定义了视频格式
     */
    public boolean containsVideoFormat() {
        return mMetaData.containsKey("video-format");
    }

    /**
     * 获取视频格式
     */
    public String getVideoFormat() {
        if (!containsVideoFormat()) {
            throw new IllegalStateException("no VIDEO-CODEC");
        }

        return mMetaData.getString("video-format");
    }

    /**
     * 是否有多个音频表现
     */
    public boolean containsAudioRenditions() {
        return mMetaData.containsKey(ATTR_AUDIO);
    }

    /**
     * 获取音频组的id
     */
    public String getAudioGroupId() {
        if (!containsAudioRenditions()) {
            throw new IllegalStateException("no AUDIO");
        }

        return mMetaData.getString(ATTR_AUDIO);
    }

    /**
     * 是否有多个视频表现
     */
    public boolean containsVideoRenditions() {
        return mMetaData.containsKey(ATTR_VIDEO);
    }

    /**
     * 获取视频组的id
     */
    public String getVideoGroupId() {
        if (!containsSubtitleRenditions()) {
            throw new IllegalStateException("no VIDEO");
        }

        return mMetaData.getString(ATTR_VIDEO);
    }

    /**
     * 是否有多个字幕表现
     */
    public boolean containsSubtitleRenditions() {
        return mMetaData.containsKey(ATTR_SUBTITLES);
    }

    /**
     * 获取字幕组的id
     */
    public String getSubtitleGroupId() {
        if (!containsSubtitleRenditions()) {
            throw new IllegalStateException("no SUBTITLES");
        }

        return mMetaData.getString(ATTR_SUBTITLES);
    }

    /**
     * 获取默认的音频表现
     */
    public Media getDefaultAudioRendition() {
        if (!containsAudioRenditions()) {
            throw new IllegalStateException("no AUDIO");
        }

        return getDefaultRendition(mAudioRenditions);
    }

    /**
     * 获取默认的视频表现
     */
    public Media getDefaultVideoRendition() {
        if (!containsVideoRenditions()) {
            throw new IllegalStateException("no VIDEO");
        }

        return getDefaultRendition(mVideoRenditions);
    }

    /**
     * 获取默认的字幕表现
     */
    public Media getDefaultSubtitleRendition() {
        if (!containsSubtitleRenditions()) {
            throw new IllegalStateException("no SUBTITLES");
        }

        return getDefaultRendition(mSubtitleRenditions);
    }

    /**
     * 从表现组中获取默认的表现（一定有）
     */
    private static Media getDefaultRendition(Media[] renditions) {
        for (Media rendition : renditions) {
            if (rendition.defaultSelect()) {
                return rendition;
            }
        }

        throw new IllegalStateException("no DEFAULT rendition");
    }

    /**
     * 获取指定语言的音频表现
     */
    public Media getAudioRenditionByLanguage(String language) {
        if (!containsAudioRenditions()) {
            throw new IllegalStateException("no AUDIO");
        }

        return getRenditionByLanguage(mAudioRenditions, language);
    }

    /**
     * 获取指定语言的字幕表现
     */
    public Media getSubtitleRenditionByLanguage(String language) {
        if (!containsSubtitleRenditions()) {
            throw new IllegalStateException("no SUBTITLES");
        }

        return getRenditionByLanguage(mSubtitleRenditions, language);
    }

    /**
     * 从表现组中获取指定语言的表现（可能没有）
     */
    private static Media getRenditionByLanguage(Media[] renditions, String language) {
        for (Media rendition : renditions) {
            if (rendition.containsLanguage()
                    && rendition.getLanguage().equals(language)) {
                return rendition;
            }
        }

        return null;
    }
}
