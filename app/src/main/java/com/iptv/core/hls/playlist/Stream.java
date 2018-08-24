package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;

public final class Stream {
    private MetaData mMetaData;
    private String mUrl;

    /**
     * 构造函数
     */
    public Stream() {
        mMetaData = new MetaData();
        mUrl = "";
    }

    /**
     * 获取元信息
     */
    public MetaData getMetaData() {
        return mMetaData;
    }

    /**
     * 设置url
     */
    public void setUrl(String url) {
        mUrl = url;
    }

    /**
     * 获取url
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * 获取带宽
     */
    public int getBandwidth() {
        if (!mMetaData.containsKey(MetaData.KEY_BANDWIDTH)) {
            throw new IllegalStateException("bandwidth is required");
        }

        return mMetaData.getInteger(MetaData.KEY_BANDWIDTH);
    }

    /**
     * 获取AudioGroup的id
     */
    public String getAudioGroupId() {
        if (!mMetaData.containsKey(MetaData.KEY_AUDIO_GROUP_ID)) {
            return "";
        }
        else {
            return mMetaData.getString(MetaData.KEY_AUDIO_GROUP_ID);
        }
    }

    /**
     * 获取VideoGroup的id
     */
    public String getVideoGroupId() {
        if (!mMetaData.containsKey(MetaData.KEY_VIDEO_GROUP_ID)) {
            return "";
        }
        else {
            return mMetaData.getString(MetaData.KEY_VIDEO_GROUP_ID);
        }
    }

    /**
     * 获取SubtitleGroup的id
     */
    public String getSubtitleGroupId() {
        if (!mMetaData.containsKey(MetaData.KEY_SUBTITLE_GROUP_ID)) {
            return "";
        }
        else {
            return mMetaData.getString(MetaData.KEY_SUBTITLE_GROUP_ID);
        }
    }
}
