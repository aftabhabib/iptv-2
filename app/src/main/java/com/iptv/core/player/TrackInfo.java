package com.iptv.core.player;

/**
 * 轨道信息
 */
public class TrackInfo {
    public static final int TYPE_AUDIO = 0;
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_SUBTITLE = 2;

    private int mTrackType;
    private MetaData mMetaData;

    /**
     * 构造函数
     */
    public TrackInfo(int trackType, MetaData metaData) {
        mTrackType = trackType;
        mMetaData = metaData;
    }

    /**
     * 是不是音频轨道
     */
    public boolean isAudio() {
        return mTrackType == TYPE_AUDIO;
    }

    /**
     * 是不是视频轨道
     */
    public boolean isVideo() {
        return mTrackType == TYPE_VIDEO;
    }

    /**
     * 是不是字幕轨道
     */
    public boolean isSubtitle() {
        return mTrackType == TYPE_SUBTITLE;
    }

    /**
     * 获取元信息
     */
    public MetaData getMetaData() {
        return mMetaData;
    }
}
