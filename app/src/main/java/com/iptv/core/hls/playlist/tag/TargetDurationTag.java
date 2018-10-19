package com.iptv.core.hls.playlist.tag;

/**
 * 最大时长标签
 */
public final class TargetDurationTag extends Tag {
    private int mDuration;

    /**
     * 构造函数
     */
    public TargetDurationTag(int duration) {
        super(Name.TARGET_DURATION);

        mDuration = duration;
    }

    /**
     * 获取时长
     */
    public int getDuration() {
        return mDuration;
    }

    @Override
    public int getProtocolVersion() {
        return 1;
    }

    @Override
    public String toString() {
        return mName + ":" + String.valueOf(mDuration);
    }
}
