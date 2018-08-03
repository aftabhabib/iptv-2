package com.iptv.core.hls.playlist;

final class ByteRange {
    private long mLength;
    private long mOffset;

    public ByteRange(long length, long offset) {
        mLength = length;
        mOffset = offset;
    }

    public long getOffset() {
        return mOffset;
    }

    public long getLength() {
        return mLength;
    }

    @Override
    public String toString() {
        return "bytes=" + mOffset + "-" + (mOffset + mLength - 1);
    }
}
