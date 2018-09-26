package com.iptv.core.hls.playlist.tag;

/**
 * 播放列表类型标签
 */
public final class PlaylistTypeTag extends Tag {
    private String mType;

    /**
     * 构造函数
     */
    public PlaylistTypeTag(String type) {
        super(Name.PLAYLIST_TYPE);

        mType = type;
    }

    /**
     * 获取类型
     */
    public String getType() {
        return mType;
    }

    @Override
    public String toString() {
        return mName + ":" + mType;
    }
}
