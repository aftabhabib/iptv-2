package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;
import com.iptv.core.utils.MalformedFormatException;

import java.util.ArrayList;
import java.util.List;

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

    private MetaData mMetaData = new MetaData();
    private String[] mFormats;
    private String mUri;

    private List<Media> mAudioRenditionList = new ArrayList<Media>();
    private List<Media> mVideoRenditionList = new ArrayList<Media>();
    private List<Media> mSubtitleRenditionList = new ArrayList<Media>();

    /**
     * 构造函数
     */
    public Stream(String attributeList, List<Media> renditionList) throws MalformedFormatException {
        String[] attributes = attributeList.split(",");
        for (int i = 0; i < attributes.length; i++) {
            String[] result = attributes[i].split("=");

            if (result[0].equals(ATTR_BANDWIDTH)) {
                int bandwidth = Integer.parseInt(result[1]);
                mMetaData.putInteger(MetaData.KEY_BANDWIDTH, bandwidth);
            }
            else if (result[0].equals(ATTR_CODECS)) {
                if (result[1].contains(",")) {
                    mFormats = result[1].split(",");
                }
                else {
                    mFormats = new String[] { result[1] };
                }
            }
            else if (result[0].equals(ATTR_AUDIO)) {
                if (!renditionList.isEmpty()) {
                    for (Media rendition : renditionList) {
                        if (rendition.getType().equals(Media.TYPE_AUDIO)
                                && rendition.getGroupId().equals(result[1])) {
                            mAudioRenditionList.add(rendition);
                        }
                    }
                }
            }
            else if (result[0].equals(ATTR_VIDEO)) {
                if (!renditionList.isEmpty()) {
                    for (Media rendition : renditionList) {
                        if (rendition.getType().equals(Media.TYPE_VIDEO)
                                && rendition.getGroupId().equals(result[1])) {
                            mVideoRenditionList.add(rendition);
                        }
                    }
                }
            }
            else if (result[0].equals(ATTR_SUBTITLES)) {
                if (!renditionList.isEmpty()) {
                    for (Media rendition : renditionList) {
                        if (rendition.getType().equals(Media.TYPE_SUBTITLE)
                                && rendition.getGroupId().equals(result[1])) {
                            mSubtitleRenditionList.add(rendition);
                        }
                    }
                }
            }
            else {
                /**
                 * not support yet
                 */
            }
        }

        /**
         * 检查必要参数
         */
        if (!mMetaData.containsKey(MetaData.KEY_BANDWIDTH)) {
            throw new MalformedFormatException("bandwidth is required");
        }

        if (mFormats == null) {
            throw new MalformedFormatException("codecs is required");
        }
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        mUri = uri;
    }

    /**
     * 获取带宽
     */
    public int getBandwidth() {
        return mMetaData.getInteger(MetaData.KEY_BANDWIDTH);
    }

    /**
     * 获取音频格式
     */
    public String getAudioFormat() {
        for (String format : mFormats) {
            if (Codec.isAudioFormat(format)) {
                return format;
            }
        }

        return null;
    }

    /**
     * 获取视频格式
     */
    public String getVideoFormat() {
        for (String format : mFormats) {
            if (Codec.isVideoFormat(format)) {
                return format;
            }
        }

        return null;
    }

    /**
     * 音频是否有多个表现
     */
    public boolean containsAudioRenditions() {
        return !mAudioRenditionList.isEmpty();
    }

    /**
     * 获取默认的音频表现
     */
    public Media getDefaultAudioRendition() {
        if (!containsAudioRenditions()) {
            throw new IllegalStateException("no audio renditions");
        }

        return getDefaultRendition(mAudioRenditionList);
    }

    /**
     * 视频是否有多个表现
     */
    public boolean containsVideoRenditions() {
        return !mVideoRenditionList.isEmpty();
    }

    /**
     * 获取默认的视频表现
     */
    public Media getDefaultVideoRendition() {
        if (!containsVideoRenditions()) {
            throw new IllegalStateException("no video renditions");
        }

        return getDefaultRendition(mVideoRenditionList);
    }

    /**
     * 字幕是否有多个表现
     */
    public boolean containsSubtitleRenditions() {
        return !mSubtitleRenditionList.isEmpty();
    }

    /**
     * 获取默认的字幕表现
     */
    public Media getDefaultSubtitleRendition() {
        if (!containsSubtitleRenditions()) {
            throw new IllegalStateException("no subtitle renditions");
        }

        return getDefaultRendition(mSubtitleRenditionList);
    }

    /**
     * 从表现组中获取默认的表现
     */
    private static Media getDefaultRendition(List<Media> renditionList) {
        for (Media rendition : renditionList) {
            if (rendition.defaultSelect()) {
                return rendition;
            }
        }

        throw new IllegalStateException("no default rendition");
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mUri;
    }
}
