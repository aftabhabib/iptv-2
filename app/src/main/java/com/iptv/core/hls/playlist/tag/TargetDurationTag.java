package com.iptv.core.hls.playlist.tag;

/**
 * 目标时长标签
 */
public final class TargetDurationTag extends Tag {
    private int mTargetDuration;

    /**
     * 构造函数
     */
    public TargetDurationTag(int targetDuration) {
        super(Name.TARGET_DURATION);

        mTargetDuration = targetDuration;
    }

    /**
     * 获取目标时长
     */
    public int getTargetDuration() {
        return mTargetDuration;
    }

    @Override
    public String toString() {
        return mName + ":" + String.valueOf(mTargetDuration);
    }
}
