package com.iptv.core.player.source.hls.playlist;

public class ByteRange {
    /**
     * required
     */
    private long mLength;

    /**
     * optional
     */
    private long mOffset;

    public ByteRange(String length) {
        this(length, "");
    }

    public ByteRange(String length, String offset) {
        mLength = Long.parseLong(length);
        mOffset = offset.isEmpty() ? -1 : Long.parseLong(offset);
    }

    public boolean isRelative() {
        return mOffset == -1;
    }

    public void setOffset(ByteRange prevRange) {
        if (!isRelative()) {
            throw new IllegalStateException("offset is already defined");
        }

        mOffset = prevRange.mOffset + prevRange.mLength;
    }

    @Override
    public String toString() {
        return "bytes=" + mOffset + "-" + (mOffset + mLength - 1);
    }
}
