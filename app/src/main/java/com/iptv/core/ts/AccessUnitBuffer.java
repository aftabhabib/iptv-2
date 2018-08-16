package com.iptv.core.ts;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

final class AccessUnitBuffer {
    private static final int CAPACITY = 256 * 1024;

    private byte[] mBuffer;
    private int mOffset;

    private long mPrevPresentationTimeStamp = -1;
    private long mPrevDecodingTimeStamp = -1;

    private Queue<AccessUnit> mAccessUnitQueue;

    /**
     * 构造函数
     */
    public AccessUnitBuffer() {
        mBuffer = new byte[CAPACITY];
        mOffset = 0;

        mAccessUnitQueue = new LinkedList<AccessUnit>();
    }

    /**
     * 写入负载（数据和时间戳）
     */
    public void writePayload(byte[] data, long pts, long dts) {
        /**
         * PresentationTimeStamp的变化，意味着新一个AccessUnit的开始
         */
        if (isPresentationTimeStampChanged(pts) && isEmpty()) {
            mAccessUnitQueue.add(new AccessUnit(
                    readData(), mPrevPresentationTimeStamp, mPrevDecodingTimeStamp));
        }

        writeData(data);

        if (pts != -1) {
            mPrevPresentationTimeStamp = pts;
        }

        if (dts != -1) {
            mPrevDecodingTimeStamp = dts;
        }
    }

    /**
     * 检查PresentationTimeStamp是否变化
     */
    private boolean isPresentationTimeStampChanged(long pts) {
        if (((mPrevPresentationTimeStamp != -1) && (mPrevPresentationTimeStamp == pts))
                || (pts == -1)) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * 缓冲是否为空
     */
    public boolean isEmpty() {
        return mOffset == 0;
    }

    /**
     * 写入数据
     */
    private void writeData(byte[] data) {
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
     * 清空缓冲
     */
    public void clear() {
        mOffset = 0;
    }

    /**
     * 读出数据
     */
    private byte[] readData() {
        byte[] data = Arrays.copyOfRange(mBuffer, 0, mOffset);
        mOffset = 0;

        return data;
    }

    /**
     * 读取AccessUnit
     */
    public AccessUnit read() {
        return mAccessUnitQueue.remove();
    }
}
