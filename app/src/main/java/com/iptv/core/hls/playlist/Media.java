package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;

public final class Media {
    private static final String TYPE_VIDEO = "VIDEO";
    private static final String TYPE_AUDIO = "AUDIO";
    private static final String TYPE_SUBTITLES = "SUBTITLES";

    private MetaData mMetaData;
    private String mUrl;

    /**
     * 构造函数
     */
    public Media() {
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
     * 是不是音频
     */
    public boolean isAudio() {
        if (!mMetaData.containsKey(MetaData.KEY_MEDIA_TYPE)) {
            throw new IllegalStateException("type is required");
        }

        return mMetaData.getString(MetaData.KEY_MEDIA_TYPE).equals(TYPE_AUDIO);
    }

    /**
     * 是不是视频
     */
    public boolean isVideo() {
        if (!mMetaData.containsKey(MetaData.KEY_MEDIA_TYPE)) {
            throw new IllegalStateException("type is required");
        }

        return mMetaData.getString(MetaData.KEY_MEDIA_TYPE).equals(TYPE_VIDEO);
    }

    /**
     * 是不是字幕
     */
    public boolean isSubtitle() {
        if (!mMetaData.containsKey(MetaData.KEY_MEDIA_TYPE)) {
            throw new IllegalStateException("type is required");
        }

        return mMetaData.getString(MetaData.KEY_MEDIA_TYPE).equals(TYPE_SUBTITLES);
    }

    /**
     * 获取所属组的ID
     */
    public String getGroupId() {
        if (!mMetaData.containsKey(MetaData.KEY_GROUP_ID)) {
            throw new IllegalStateException("group-id is required");
        }

        return mMetaData.getString(MetaData.KEY_GROUP_ID);
    }

    /**
     * 是不是默认的选择
     */
    public boolean defaultSelect() {
        if (!mMetaData.containsKey(MetaData.KEY_DEFAULT_SELECT)) {
            return false;
        }
        else {
            return mMetaData.getBoolean(MetaData.KEY_DEFAULT_SELECT);
        }
    }

    /**
     * 获取语言
     */
    public String getLanguage() {
        if (!mMetaData.containsKey(MetaData.KEY_LANGUAGE)) {
            return "";
        }
        else {
            return mMetaData.getString(MetaData.KEY_LANGUAGE);
        }
    }
}
