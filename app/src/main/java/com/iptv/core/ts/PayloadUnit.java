package com.iptv.core.ts;

import java.nio.ByteBuffer;

class PayloadUnit {
    private ByteBuffer mBuffer;

    private int mCounter = -1;

    public PayloadUnit(int capacity) {
        mBuffer = ByteBuffer.allocateDirect(capacity);
    }

    /**
     * （包序）计数是否连续
     */
    public boolean isDiscontinuous(int counter) {
        if (mCounter == -1) {
            return false;
        }
        else {
            /**
             * counter is 4 bits field
             */
            return ((mCounter + 1) & 0x0f) != counter;
        }
    }

    /**
     * 写入数据，同时更新（包序）计数
     */
    public void write(byte[] data, int counter) {
        mBuffer.put(data);

        mCounter = counter;
    }

    /**
     * 重置
     */
    public void reset() {
        mBuffer.clear();

        mCounter = -1;
    }

    /**
     * 获取负载单元的数据
     */
    public byte[] data() {
        return mBuffer.array();
    }

    /**
     * 清空数据
     */
    public void clear() {
        mBuffer.clear();
    }
}
