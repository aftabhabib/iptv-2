package com.iptv.core.hls.playlist.tag;

/**
 * 媒体片段标签
 */
public final class InfTag extends Tag {
    private float mDuration;
    private String mTitle;

    private int mProtocolVersion;

    /**
     * 构造函数
     */
    public InfTag(int duration) {
        this(duration, "");
    }

    /**
     * 构造函数
     */
    public InfTag(int duration, String title) {
        super(Name.INF);

        mDuration = (float)duration;
        mTitle = title;

        mProtocolVersion = 1;
    }

    /**
     * 构造函数
     */
    public InfTag(float duration) {
        this(duration, "");
    }

    /**
     * 构造函数
     */
    public InfTag(float duration, String title) {
        super(Name.INF);

        mDuration = duration;
        mTitle = title;

        mProtocolVersion = 3;
    }

    /**
     * 获取播放时长
     */
    public float getDuration() {
        return mDuration;
    }

    @Override
    public int getProtocolVersion() {
        return mProtocolVersion;
    }

    @Override
    public String toString() {
        return mName + ":" + String.valueOf(mDuration) + "," + mTitle;
    }
}
