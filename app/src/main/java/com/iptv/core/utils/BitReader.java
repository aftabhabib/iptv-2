package com.iptv.core.utils;

public final class BitReader {
    private static final int BITS_PER_BYTE = 8;

    private byte[] mBuffer;
    private int mOffset;
    private int mLength;

    private int mAvailableBitsInByte = BITS_PER_BYTE;

    /**
     * 构造函数
     */
    public BitReader(byte[] buffer) {
        this(buffer, buffer.length);
    }

    /**
     * 构造函数
     */
    public BitReader(byte[] buffer, int length) {
        this(buffer, 0, length);
    }

    /**
     * 构造函数
     */
    public BitReader(byte[] buffer, int offset, int length) {
        if (offset < 0 || offset >= buffer.length) {
            throw new IllegalArgumentException("invalid offset");
        }

        if (length < 0 || offset + length > buffer.length) {
            throw new IllegalArgumentException("invalid length");
        }

        mBuffer = buffer;
        mOffset = offset;
        mLength = length;
    }

    /**
     * 还有多少比特可读
     */
    public int available() {
        return mAvailableBitsInByte + (mLength - 1) * BITS_PER_BYTE;
    }

    /**
     * 读一比特
     */
    private int readBit() {
        int value = (mBuffer[mOffset] >> (mAvailableBitsInByte - 1)) & 0x01;

        mAvailableBitsInByte--;

        if (mAvailableBitsInByte == 0) {
            mOffset++;
            mLength--;

            if (mLength > 0) {
                mAvailableBitsInByte = BITS_PER_BYTE;
            }
        }

        return value;
    }

    /**
     * 跳过若干比特
     */
    public void skip(int count) {
        if (count < 0 || count > available()) {
            throw new IllegalArgumentException("invalid count");
        }

        while (count > 0) {
            readBit();

            count--;
        }
    }

    /**
     * 读若干比特，输出为int型
     */
    public int readInt(int count) {
        if (count < 0 || count > available()) {
            throw new IllegalArgumentException("invalid count");
        }

        if (count > 31) {
            throw new IllegalArgumentException("count should be less than 32");
        }

        int value = 0;

        while (count > 0) {
            value = (value << 1) | readBit();
        }

        return value;
    }

    /**
     * 读若干比特，输出为long型
     */
    public long readLong(int count) {
        if (count < 0 || count > available()) {
            throw new IllegalArgumentException("invalid count");
        }

        if (count > 63) {
            throw new IllegalArgumentException("count should be less than 64");
        }

        long value = 0;

        while (count > 0) {
            value = (value << 1) | readBit();
        }

        return value;
    }
}
