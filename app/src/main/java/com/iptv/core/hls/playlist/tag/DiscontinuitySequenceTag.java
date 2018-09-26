package com.iptv.core.hls.playlist.tag;

/**
 * 不连续序号标签
 */
public final class DiscontinuitySequenceTag extends Tag {
    private long mSequenceNumber;

    /**
     * 构造函数
     */
    public DiscontinuitySequenceTag(long sequenceNumber) {
        super(Name.DISCONTINUITY_SEQUENCE);

        mSequenceNumber = sequenceNumber;
    }

    /**
     * 获取序号
     */
    public long getSequenceNumber() {
        return mSequenceNumber;
    }

    @Override
    public String toString() {
        return mName + ":" + String.valueOf(mSequenceNumber);
    }
}
