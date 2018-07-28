package com.iptv.core.player.source.hls.playlist;

public class MediaSegment {
    private float mDuration;
    private ByteRange mRange;
    private boolean mIsDiscontinuous;
    private Key mKey;
    private String mUri;

    public MediaSegment(float duration, ByteRange range,
                        boolean isDiscontinuous, Key key, String uri) {
        mDuration = duration;
        mRange = range;
        mIsDiscontinuous = isDiscontinuous;
        mKey = key;
        mUri = uri;
    }

    public void setSequenceNumber(int sequenceNumber) {

    }

    /**
     * 获取时长（单位：毫秒）
     */
    public int getDuration() {
        return (int)(mDuration * 1000);
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mUri;
    }

    /**
     * 获取范围
     */
    public ByteRange getRange() {
        return mRange;
    }

    /**
     * 是否不连续
     */
    public boolean isDiscontinuous() {
        return mIsDiscontinuous;
    }

    /**
     * 获取解密密钥
     */
    public Key getKey() {
        return mKey;
    }
}
