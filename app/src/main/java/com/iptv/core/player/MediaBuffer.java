package com.iptv.core.player;

import java.util.Arrays;

/**
 * 媒体缓冲
 */
public class MediaBuffer {
    private static final int DEFAULT_CAPACITY = 64 * 1024;

    private MetaData mMetaData;

    private byte[] mBuffer;
    private int mOffset;

    /**
     * 构造函数
     */
    public MediaBuffer() {
        mMetaData = new MetaData();

        /**
         * 初始大小64K
         */
        mBuffer = new byte[DEFAULT_CAPACITY];
        mOffset = 0;
    }

    /**
     * 获取元信息
     */
    public MetaData getMetaData() {
        return mMetaData;
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
    public void write(byte[] buf) {
        write(buf, 0, buf.length);
    }

    /**
     * 写入数据
     */
    public void write(byte[] buf, int size) {
        write(buf, 0, size);
    }

    /**
     * 写入数据
     */
    public void write(byte[] buf, int offset, int size) {
        if (mOffset + size > mBuffer.length) {
            /**
             * 缓冲溢出，扩大一倍
             */
            byte[] buffer = new byte[mBuffer.length * 2];
            System.arraycopy(mBuffer, 0, buffer, 0, mOffset);

            mBuffer = buffer;
        }

        System.arraycopy(buf, offset, mBuffer, mOffset, size);
        mOffset += size;
    }

    /**
     * 读出数据
     */
    public byte[] read() {
        byte[] data = Arrays.copyOfRange(mBuffer, 0, mOffset);
        mOffset = 0;

        return data;
    }

    /**
     * 清空缓冲
     */
    public void clear() {
        mOffset = 0;
    }
}
