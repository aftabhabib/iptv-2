package com.iptv.core.ts;

final class PESPacket {
    private long mPresentationTimeStamp;
    private long mDecodingTimeStamp;

    private byte[] mPayloadData;

    private PESPacket(long pts, long dts, byte[] payloadData) {
        mPresentationTimeStamp = pts;
        mDecodingTimeStamp = dts;

        mPayloadData = payloadData;
    }

    public long getPresentationTimeStamp() {
        return mPresentationTimeStamp;
    }

    public long getDecodingTimeStamp() {
        return mDecodingTimeStamp;
    }

    public byte[] getPayloadData() {
        return mPayloadData;
    }

    public static PESPacket parse(byte[] data) {
        return null;
    }
}
