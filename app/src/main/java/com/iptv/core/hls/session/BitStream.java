package com.iptv.core.hls.session;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public final class BitStream {
    private InputStream mInput;

    private int mByteValue = 0;
    private int mBitsLeft = 0;

    public BitStream(InputStream input) {
        mInput = input;
    }

    private int readBit() throws IOException {
        if (mBitsLeft == 0) {
            int ret = mInput.read();
            if (ret < 0) {
                throw new EOFException();
            }

            mByteValue = ret;
            mBitsLeft = 8;
        }

        int value = (mByteValue >> (mBitsLeft - 1)) & 0x01;
        mBitsLeft--;

        return value;
    }

    public long readBits(int numOfBits) throws IOException {
        if (numOfBits <= 0 || numOfBits > 63) {
            throw new IllegalArgumentException();
        }

        long value = 0;
        do {
            value |= (readBit() << (numOfBits - 1));
            numOfBits--;
        }
        while (numOfBits > 0);

        return value;
    }

    public int readUnsignedByte() throws IOException {
        return (int)(readBits(8) & 0xFFL);
    }

    public int readUnsignedShort() throws IOException {
        return (int)(readBits(16) & 0xFFFL);
    }

    public int readUnsignedInt24() throws IOException {
        return (int)(readBits(24) & 0xFFFFFFL);
    }

    public long readUnsignedInt() throws IOException {
        return readBits(32) & 0xFFFFFFFFL;
    }

    public void readFully(byte[] buffer) throws IOException {
        if (!isByteAlign()) {
            throw new IllegalStateException();
        }

        int offset = 0;
        do {
            int ret = mInput.read(buffer, offset, buffer.length - offset);
            if (ret < 0) {
                throw new EOFException();
            }

            offset += ret;
        }
        while (offset < buffer.length);
    }

    public void skipBits(int numOfBits) throws IOException {
        if (numOfBits <= mBitsLeft) {
            /**
             * 在当前字节内
             */
            mBitsLeft -= numOfBits;
        }
        else {
            /**
             * 越过当前字节
             */
            numOfBits -= mBitsLeft;
            mBitsLeft = 0;

            /**
             * 越过若干字节
             */
            while (numOfBits > 8) {
                long ret = mInput.skip(1);
                if (ret < 0) {
                    throw new EOFException();
                }
                else if (ret == 0) {
                    continue;
                }

                numOfBits -= 8;
            }

            /**
             * 不足一个字节
             */
            if (numOfBits > 0) {
                mByteValue = mInput.read();
                mBitsLeft = 8 - numOfBits;
            }
        }
    }

    private boolean isByteAlign() {
        return mBitsLeft > 0;
    }
}
