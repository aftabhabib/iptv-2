package com.iptv.core.ts;

import com.iptv.core.utils.BitReader;

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
        if (data == null || data.length != PACKET_SIZE) {
            throw new IllegalArgumentException("invalid data");
        }

        BitReader reader = new BitReader(data);

        int syncByte = reader.readInt(8);
        if (syncByte != 0x47) {
            return null;
        }

        reader.skip(1);
        int payloadUnitStart = reader.readInt(1);
        reader.skip(1);
        int packetId = reader.readInt(13);

        reader.skip(2);
        int adaptionFieldControl = reader.readInt(2);
        int continuityCounter = reader.readInt(4);

        if ((adaptionFieldControl & 0x02) > 0) {
            int adaptionFieldLength = reader.readInt(8);

            if (adaptionFieldLength > 0) {
                /**
                 * check adaption field length
                 */
                if (adaptionFieldLength * 8 > reader.available()) {
                    return null;
                }

                /**
                 * we do not care adaptionField
                 */
                reader.skip(adaptionFieldLength * 8);
            }
        }

        byte[] payload = null;

        if ((adaptionFieldControl & 0x01) > 0) {
            /**
             * check payload length
             */
            if (reader.available() / 8 == 0) {
                return null;
            }

            payload = reader.readAvailableData();
        }

        return new TransportPacket(packetId, payload,
                payloadUnitStart > 0, continuityCounter);
    }
}
