package com.iptv.core.hls.session;

import java.io.IOException;
import java.io.InputStream;

public final class BitStream {
    private InputStream mInput;

    private int mByteValue = 0;
    private int mBitsLeft = 0;

    public BitStream(InputStream input) {
        mInput = input;
    }

    public void skip(int numOfBits) throws IOException {
        if (numOfBits <= mBitsLeft) {
            /**
             * 在当前字节内
             */
            mBitsLeft -= numOfBits;
        }
        else {
            /**
             * 超出当前字节
             */
            numOfBits -= mBitsLeft;

            /**
             * 若干字节
             */
            while (numOfBits > 8) {
                mInput.skip(1);

                numOfBits -= 8;
            }

            /**
             * 剩下的字节内
             */
            if (numOfBits > 0) {
                mByteValue = mInput.read();
                mBitsLeft = 8 - numOfBits;
            }
        }
    }

    private int readBit() throws IOException {
        if (mBitsLeft == 0) {
            mByteValue = mInput.read();
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
}
