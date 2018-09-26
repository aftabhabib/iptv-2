package com.iptv.core.hls.playlist.tag;

/**
 * 媒体序号标签
 */
public final class MediaSequenceTag extends Tag {
    private long mSequenceNumber;

    /**
     * 构造函数
     */
    public MediaSequenceTag(long sequenceNumber) {
        super(Name.MEDIA_SEQUENCE);

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
