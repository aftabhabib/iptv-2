package com.iptv.core.hls.playlist.tag;

/**
 * 媒体片段标签
 */
public final class InfTag extends Tag {
    private float mDuration;

    /**
     * 构造函数
     */
    public InfTag(float duration) {
        super(Name.INF);

        mDuration = duration;
    }

    /**
     * 获取播放时长
     */
    public float getDuration() {
        return mDuration;
    }

    @Override
    public String toString() {
        return mName + ":" + String.valueOf(mDuration) + ",";
    }
}
