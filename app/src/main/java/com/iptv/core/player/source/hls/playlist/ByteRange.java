package com.iptv.core.player.source.hls.playlist;

public class ByteRange {
    private long mLength;
    private long mOffset;

    private ByteRange(long length, long offset) {
        mLength = length;
        mOffset = offset;
    }

    public long getLength() {
        return mLength;
    }

    public long getOffset() {
        return mOffset;
    }

    public static ByteRange parse(String byteRange) {
        String[] result = byteRange.split("@");

        long length = Long.parseLong(result[0]);

        long offset;
        if (result.length == 1) {
            /**
             * 依赖上一个片段的定义，即：currOffset = prevOffset + prevLength
             */
            offset = -1;
        }
        else {
            offset = Long.parseLong(result[1]);
        }

        return new ByteRange(length, offset);
    }
}
