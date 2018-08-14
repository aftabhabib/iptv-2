package com.iptv.core.ts;

import java.util.Arrays;

class PayloadBuffer {
    private static final int CAPACITY = 128 * 1024;

    private byte[] mBuffer;
    private int mOffset;

    private int mLastContinuityCounter;

    public PayloadBuffer() {
        mBuffer = new byte[CAPACITY];
        mOffset = 0;

        mLastContinuityCounter = -1;
    }

    /**
     * 检查连续性
     */
    public boolean checkContinuity(int continuityCounter) {
        boolean ret;

        if (mLastContinuityCounter < 0) {
            /**
             * counter init
             */
            ret = true;
        }
        else {
            /**
             * counter is 4 bits field.
             * it wraps around to 0 after its maximum value
             */
            ret = ((mLastContinuityCounter + 1) & 0x0F) == continuityCounter;
        }

        mLastContinuityCounter = continuityCounter;

        return ret;
    }

    /**
     * 写入数据
     */
    public void write(byte[] data) {
        if (mOffset + data.length > mBuffer.length) {
            /**
             * 溢出，扩大缓冲
             */
            byte[] buffer = new byte[mBuffer.length * 2];
            System.arraycopy(mBuffer, 0, buffer, 0, mOffset);

            mBuffer = buffer;
        }

        System.arraycopy(data, 0, mBuffer, mOffset, data.length);
        mOffset += data.length;
    }

    /**
     * 清空缓存
     */
    public void clear() {
        mOffset = 0;
    }

    /**
     * 是否为空
     */
    public boolean isEmpty() {
        return mOffset == 0;
    }

    /**
     * 获取一个负载单元的数据
     */
    public byte[] read() {
        byte[] data = Arrays.copyOfRange(mBuffer, 0, mOffset);

        clear();

        return data;
    }
}
