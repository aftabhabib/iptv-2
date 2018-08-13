package com.iptv.core.ts;

/**
 * transport_packet
 */
public class TransportPacket {
    public static final int PACKET_SIZE = 188;

    private int mPacketId;
    private byte[] mPayloadData;

    private boolean mIsPayloadUnitStart;
    private int mContinuityCounter;

    /**
     * 构造函数
     */
    private TransportPacket(int packetId, byte[] payloadData,
                            boolean isPayloadUnitStart, int continuityCounter) {
        mPacketId = packetId;
        mPayloadData = payloadData;

        mIsPayloadUnitStart = isPayloadUnitStart;
        mContinuityCounter = continuityCounter;
    }

    /**
     * 获取PID
     */
    public int getPacketId() {
        return mPacketId;
    }

    /**
     * 是否包含负载数据
     */
    public boolean containsPayloadData() {
        return mPayloadData != null;
    }

    /**
     * 获取负载数据
     */
    public byte[] getPayloadData() {
        return mPayloadData;
    }

    /**
     * 获取连续性计数
     */
    public int getContinuityCounter() {
        return mContinuityCounter;
    }

    /**
     * 负载数据是否是负载单元的起始
     */
    public boolean isPayloadUnitStart() {
        return mIsPayloadUnitStart;
    }

    /**
     * 解析数据，创建TransportPacket
     */
    public static TransportPacket parse(byte[] data) {
        return null;
    }
}
