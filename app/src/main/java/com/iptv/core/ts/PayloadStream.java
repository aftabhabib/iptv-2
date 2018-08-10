package com.iptv.core.ts;

import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

class PayloadStream {
    private static final String TAG = "PayloadStream";

    private int mId;
    private int mLastContinuityCounter;

    private int mCapacity = 128 * 1024;
    private byte[] mBuffer = null;
    private int mOffset = 0;

    private Queue<byte[]> mPayloadUnitQueue = new LinkedList<byte[]>();

    public PayloadStream(TransportPacket packet) {
        mId = packet.getPacketId();

        if (packet.isPayloadUnitStart()) {
            writePayloadData(packet.getPayloadData());
        }
        else {
            /**
             * 等待新的负载单元开始
             */
            Log.w(TAG, "payload data is not the start of unit, discard");
        }

        mLastContinuityCounter = packet.getContinuityCounter();
    }

    /**
     * 写入负载数据
     */
    private void writePayloadData(byte[] payloadData) {
        if (mBuffer == null) {
            mBuffer = new byte[mCapacity];
        }
        else {
            if (mOffset + payloadData.length > mBuffer.length) {
                /**
                 * 溢出，扩大缓冲
                 */
                autoIncreaseBuffer();
            }
        }

        System.arraycopy(payloadData, 0, mBuffer, mOffset, payloadData.length);
        mOffset += payloadData.length;
    }

    /**
     * 自动扩大（倍增）缓冲
     */
    private void autoIncreaseBuffer() {
        mCapacity *= 2;

        byte[] buffer = new byte[mCapacity];
        System.arraycopy(mBuffer, 0, buffer, 0, mOffset);
        mBuffer = buffer;
    }

    /**
     * 写入一个包
     */
    public void writePacket(TransportPacket packet) {
        /**
         * check id
         */
        if (mId != packet.getPacketId()) {
            throw new IllegalArgumentException("packet not belongs to this payload stream");
        }

        /**
         * if continuity_counter continuous
         */
        if (isDiscontinuous(packet.getContinuityCounter())) {
            /**
             * 计数不连续。如果缓冲中有未完成的负载单元，清除！
             */
            if (!isBufferEmpty()) {
                clearBuffer();
            }

            mLastContinuityCounter = packet.getContinuityCounter();
        }

        /**
         * if packet is start of payload unit
         */
        if (packet.isPayloadUnitStart()) {
            /**
             * 上一个负载单元结束，将其移入队列（如果有的话）
             */
            if (!isBufferEmpty()) {
                queuePayloadUnit();
            }

            writePayloadData(packet.getPayloadData());
        }
        else {
            if (isBufferEmpty()) {
                /**
                 * 等待新的负载单元开始
                 */
                Log.w(TAG, "payload data is not the start of unit, discard");
            }
            else {
                writePayloadData(packet.getPayloadData());
            }
        }
    }

    /**
     * 是否连续
     */
    private boolean isDiscontinuous(int continuityCounter) {
        /**
         * counter is 4 bits field. it wraps around to 0 after its maximum value
         */
        return ((++mLastContinuityCounter & 0x0f) != continuityCounter);
    }

    private void clearBuffer() {
        mOffset = 0;
    }

    /**
     * 是否为空
     */
    private boolean isBufferEmpty() {
        return mOffset == 0;
    }

    private void queuePayloadUnit() {
        byte[] payloadUnit = Arrays.copyOfRange(mBuffer, 0, mOffset);
        mOffset = 0;

        mPayloadUnitQueue.add(payloadUnit);
    }

    /**
     * 获取一个负载单元的数据
     */
    public byte[] read() {
        return mPayloadUnitQueue.remove();
    }
}
