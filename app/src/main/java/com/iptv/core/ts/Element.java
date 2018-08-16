package com.iptv.core.ts;

final class Element {
    public static final int STREAM_TYPE_MPEG1_VIDEO = 0x01;
    public static final int STREAM_TYPE_MPEG2_VIDEO = 0x02;
    public static final int STREAM_TYPE_MPEG1_AUDIO = 0x03;
    public static final int STREAM_TYPE_MPEG2_AUDIO = 0x04;
    public static final int STREAM_TYPE_MPEG2_ADTS_AUDIO = 0x0f;
    public static final int STREAM_TYPE_MPEG4_VIDEO = 0x10;
    public static final int STREAM_TYPE_MPEG4_LATM_AUDIO = 0x11;
    public static final int STREAM_TYPE_H264_VIDEO = 0x1b;

    private int mStreamType;
    private AccessUnitBuffer mAccessUnitBuffer = null;

    /**
     * 构造函数
     */
    public Element(int streamType) {
        mStreamType = streamType;
    }

    /**
     * 获取流类型
     */
    public int getStreamType() {
        return mStreamType;
    }

    /**
     * 是不是视频流
     */
    public boolean isVideoStream() {
        switch (mStreamType) {
            case STREAM_TYPE_MPEG1_VIDEO:
            case STREAM_TYPE_MPEG2_VIDEO:
            case STREAM_TYPE_MPEG4_VIDEO:
            case STREAM_TYPE_H264_VIDEO: {
                return true;
            }
            default: {
                return false;
            }
        }
    }


    /**
     * 是不是音频流
     */
    public boolean isAudioStream() {
        switch (mStreamType) {
            case STREAM_TYPE_MPEG1_AUDIO:
            case STREAM_TYPE_MPEG2_AUDIO:
            case STREAM_TYPE_MPEG2_ADTS_AUDIO:
            case STREAM_TYPE_MPEG4_LATM_AUDIO: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    /**
     * 写入PES包
     */
    public void writePESPacket(PESPacket pesPacket) {
        /**
         * we only process video/audio stream
         */
        if (!pesPacket.isVideoStream() && !pesPacket.isAudioStream()) {
            return;
        }

        if (mAccessUnitBuffer == null) {
            /**
             * AccessUnit buffer init
             */
            mAccessUnitBuffer = new AccessUnitBuffer();
        }

        if (pesPacket.containsPayloadData()) {
            mAccessUnitBuffer.writePayload(pesPacket.getPayloadData(),
                    pesPacket.getPresentationTimeStamp(), pesPacket.getDecodingTimeStamp());
        }
    }

    /**
     * 读取AccessUnit
     */
    public AccessUnit readAccessUnit() {
        if (mAccessUnitBuffer == null) {
            return null;
        }

        return mAccessUnitBuffer.read();
    }
}
