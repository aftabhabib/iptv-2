package com.iptv.core.hls.playlist.tag;

/**
 * 媒体片段标签
 */
public final class InfTag extends Tag {
    private float mDuration;
    private String mTitle;

    /**
     * 构造函数
     */
    public InfTag(float duration, String title) {
        super(Name.INF);

        mDuration = duration;
        mTitle = title;
    }

    /**
     * 获取播放时长
     */
    public float getDuration() {
        return mDuration;
    }

    @Override
    public String toString() {
        return mName + ":" + String.valueOf(mDuration) + "," + mTitle;
    }
}
