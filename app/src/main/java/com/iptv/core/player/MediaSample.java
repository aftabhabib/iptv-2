package com.iptv.core.player;

import java.nio.ByteBuffer;

public abstract class MediaSample {
    private static final int MAX_SAMPLE_SIZE = 128 * 1000;

    private int mCodecType;

    private long mPresentationTimestamp;
    private long mDecodingTimestamp;

    private ByteBuffer mDataBuffer;

    public MediaSample(int codecType, long pts, long dts) {
        mCodecType = codecType;

        mPresentationTimestamp = pts;
        mDecodingTimestamp = dts;

        mDataBuffer.allocateDirect(MAX_SAMPLE_SIZE);
    }

    public void write(byte[] buf) {
        mDataBuffer.put(buf, 0, buf.length);
    }

    public void write(byte[] buf, int offset, int size) {
        mDataBuffer.put(buf, offset, size);
    }
}
