package com.iptv.core.ts;

/**
 * transport_packet
 */
public class TransportPacket {
    private static final int PACKET_SIZE = 188;

    private int mPacketId;

    private boolean mPayloadUnitStart;
    private int mCounter;

    private byte[] mPayload;

    private TransportPacket(int packetId, boolean payloadUnitStart,
                            int counter, byte[] payload) {
        mPacketId = packetId;

        mPayloadUnitStart = payloadUnitStart;
        mCounter = counter;

        mPayload = payload;
    }

    /**
     * 获取PID
     */
    public int getPacketId() {
        return mPacketId;
    }

    /**
     * 负载是否是新的单元
     */
    public boolean isPayloadUnitStart() {
        return mPayloadUnitStart;
    }

    /**
     * 获取（包序）计数
     */
    public int getCounter() {
        return mCounter;
    }

    /**
     * 获取负载
     */
    public byte[] getPayload() {
        return mPayload;
    }

    public static TransportPacket parse(byte[] data) {
        return null;
    }
}
