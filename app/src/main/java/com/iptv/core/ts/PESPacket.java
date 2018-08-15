package com.iptv.core.ts;

import com.iptv.core.utils.BitReader;

final class PESPacket {
    private int mStreamId;

    private long mPresentationTimeStamp;
    private long mDecodingTimeStamp;

    private byte[] mPayloadData;

    /**
     * 构造函数
     */
    private PESPacket(int streamId, byte[] payloadData, long pts, long dts) {
        mStreamId = streamId;
        mPayloadData = payloadData;

        mPresentationTimeStamp = pts;
        mDecodingTimeStamp = dts;
    }

    /**
     * 是不是音频流
     */
    public boolean isAudioStream() {
        if ((mStreamId & 0xc0) == 0xc0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 是不是视频流
     */
    public boolean isVideoStream() {
        if ((mStreamId & 0xe0) == 0xe0) {
            return true;
        }
        else {
            return false;
        }
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
     * 获取pts
     */
    public long getPresentationTimeStamp() {
        return mPresentationTimeStamp;
    }

    /**
     * 获取dts
     */
    public long getDecodingTimeStamp() {
        return mDecodingTimeStamp;
    }

    /**
     * 解析数据，创建PESPacket
     */
    public static PESPacket parse(byte[] data) {
        BitReader reader = new BitReader(data);
        if (reader.available() < 6 * 8) {
            return null;
        }

        int startCodePrefix = reader.readInt(24);
        if (startCodePrefix != 0x000001) {
            return null;
        }

        int streamId = reader.readInt(8);
        int packetLength = reader.readInt(16);
        /**
         * check packet length
         */
        if (((packetLength == 0) && ((streamId & 0xe0) != 0xe0))
                || (packetLength * 8 > reader.available())) {
            return null;
        }

        byte[] payload = null;
        long pts = -1;
        long dts = -1;

        if ((streamId == 0xbc)
                || (streamId == 0xbf)
                || (streamId == 0xf0)
                || (streamId == 0xf1)
                || (streamId == 0xff)
                || (streamId == 0xf2)
                || (streamId == 0xf8)) {
            payload = reader.readBytes(packetLength);
        }
        else if (streamId == 0xbe) {
            /**
             * padding, ignore
             */
        }
        else {
            int availableLength =
                    ((packetLength == 0) ? (reader.available() / 8) : packetLength);

            reader.skip(2);
            reader.skip(2);
            reader.skip(1);
            reader.skip(1);
            reader.skip(1);
            reader.skip(1);
            int timeStampFlag = reader.readInt(2);
            reader.skip(1);
            reader.skip(1);
            reader.skip(1);
            reader.skip(1);
            reader.skip(1);
            reader.skip(1);

            int headerLength = reader.readInt(8);
            /**
             * check header length
             */
            if (3 + headerLength > availableLength) {
                return null;
            }

            int availableHeaderLength = headerLength;

            if ((timeStampFlag & 0x02) > 0) {
                reader.skip(4);
                pts |= reader.readLong(3) << 30;
                reader.skip(1);
                pts |= reader.readLong(15) << 15;
                reader.skip(1);
                pts |= reader.readLong(15);
                reader.skip(1);

                availableHeaderLength -= 5;

                if ((timeStampFlag & 0x01) > 0) {
                    reader.skip(4);
                    dts |= reader.readLong(3) << 30;
                    reader.skip(1);
                    dts |= reader.readLong(15) << 15;
                    reader.skip(1);
                    dts |= reader.readLong(15);
                    reader.skip(1);

                    availableHeaderLength -= 5;
                }
            }

            if (availableHeaderLength > 0) {
                /**
                 * we do not care other optional field
                 */
                reader.skip(availableHeaderLength * 8);
            }

            availableLength -= (3 + headerLength);
            /**
             * check payload length
             */
            if (availableLength == 0) {
                return null;
            }

            payload = reader.readBytes(availableLength);
        }

        return new PESPacket(streamId, payload, pts, dts);
    }
}
