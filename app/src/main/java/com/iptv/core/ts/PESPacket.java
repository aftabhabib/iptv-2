package com.iptv.core.ts;

public class PESPacket {
    private long mPresentationTimeStamp;
    private long mDecodingTimeStamp;

    private byte[] mPayloadData;

    private PESPacket(long pts, long dts, byte[] payloadData) {
        mPresentationTimeStamp = pts;
        mDecodingTimeStamp = dts;

        mPayloadData = payloadData;
    }

    public static PESPacket parse(byte[] data) {
        return null;
    }
}
