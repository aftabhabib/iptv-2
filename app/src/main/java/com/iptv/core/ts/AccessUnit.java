package com.iptv.core.ts;

public class AccessUnit {
    private byte[] mData;

    private long mPresentationTimeStamp;
    private long mDecodingTimeStamp;

    /**
     * 构造函数
     */
    public AccessUnit(byte[] data, long pts, long dts) {
        mData = data;

        mPresentationTimeStamp = pts;
        mDecodingTimeStamp = dts;
    }

    /**
     * 获取pts
     */
    public long getPresentationTimeStamp() {
        return mPresentationTimeStamp;
    }

    /**
     * 获取dts
     */
    public long getDecodingTimeStamp() {
        return mDecodingTimeStamp;
    }

    /**
     * 获取数据
     */
    public byte[] data() {
        return mData;
    }
}
