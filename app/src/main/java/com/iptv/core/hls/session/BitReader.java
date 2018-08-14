package com.iptv.core.hls.session;

import java.util.Arrays;

public final class BitReader {
    private byte[] mData;

    private int mBytePos = 0;
    private int mBitPos = 7;

    public BitReader(byte[] data) {
        mData = data;
    }

    public int readBitsAsInt(int numOfBits) {
        if (numOfBits <= 0 || numOfBits > 31) {
            throw new IllegalArgumentException();
        }

        if (availableBits() < numOfBits) {
            throw new IllegalStateException();
        }

        int value = 0;

        for (; numOfBits > 0; numOfBits--) {
            value |= (readBit() << (numOfBits - 1));
        }

        return value;
    }

    public long readBitsAsLong(int numOfBits) {
        if (numOfBits <= 31 || numOfBits > 63) {
            throw new IllegalArgumentException();
        }

        if (availableBits() < numOfBits) {
            throw new IllegalStateException();
        }

        long value = 0;

        for (; numOfBits > 0; numOfBits--) {
            value |= (readBit() << (numOfBits - 1));
        }

        return value;
    }

    public void skipBits(int numOfBits) {
        if (availableBits() < numOfBits) {
            throw new IllegalStateException();
        }

        for (; numOfBits > 0; numOfBits--) {
            mBitPos--;
            if (mBitPos < 0) {
                mBytePos++;

                mBitPos = 7;
            }
        }
    }

    public int availableBits() {
        return (mData.length - mBytePos - 1) * 8 + (mBitPos + 1);
    }

    private int readBit() {
        int value = (mData[mBytePos] >> mBitPos) & 0x01;

        mBitPos--;
        if (mBitPos < 0) {
            mBytePos++;

            mBitPos = 7;
        }

        return value;
    }

    public int readUnsignedByte() {
        if (!isByteAlign() || availableBytes() < 1) {
            throw new IllegalStateException();
        }

        return (mData[mBytePos++] & 0xFF);
    }

    public int readUnsignedShort() {
        if (!isByteAlign() || availableBytes() < 2) {
            throw new IllegalStateException();
        }

        return ((mData[mBytePos++] & 0xFF) << 8)
                | (mData[mBytePos++] & 0xFF);
    }

    public int readUnsignedInt24() {
        if (!isByteAlign() || availableBytes() < 3) {
            throw new IllegalStateException();
        }

        return ((mData[mBytePos++] & 0xFF) << 16)
                | ((mData[mBytePos++] & 0xFF) << 8)
                | (mData[mBytePos++] & 0xFF);
    }

    public long readUnsignedInt() {
        if (!isByteAlign() || availableBytes() < 4) {
            throw new IllegalStateException();
        }

        return ((mData[mBytePos++] & 0xFFL) << 24)
                | ((mData[mBytePos++] & 0xFFL) << 16)
                | ((mData[mBytePos++] & 0xFFL) << 8)
                | (mData[mBytePos++] & 0xFFL);
    }

    public byte[] readBytes(int numOfBytes) {
        if (!isByteAlign() || availableBytes() < numOfBytes) {
            throw new IllegalStateException();
        }

        byte[] value = Arrays.copyOfRange(mData, mBytePos, mBytePos + numOfBytes);
        mBytePos += numOfBytes;

        return value;
    }

    public void skipBytes(int numOfBytes) {
        if (!isByteAlign() || availableBytes() < numOfBytes) {
            throw new IllegalStateException();
        }

        mBytePos += numOfBytes;
    }

    private boolean isByteAlign() {
        return mBitPos == 7;
    }

    public int availableBytes() {
        return mData.length - mBytePos;
    }

    public boolean isEOF() {
        return (mBytePos == mData.length) && (mBitPos == 7);
    }
}
