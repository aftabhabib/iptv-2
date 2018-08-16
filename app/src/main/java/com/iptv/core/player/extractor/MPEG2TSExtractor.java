package com.iptv.core.player.extractor;

import android.util.Log;

import com.iptv.core.player.MediaExtractor;
import com.iptv.core.player.MediaSample;
import com.iptv.core.player.MediaSource;
import com.iptv.core.ts.TransportPacket;
import com.iptv.core.ts.TransportStream;

import java.io.IOException;

public final class MPEG2TSExtractor implements MediaExtractor {
    private static final String TAG = "MPEG2TSExtractor";

    private MediaSource mSource;

    private byte[] mPacketBuffer;
    private TransportStream mTransportStream;

    public MPEG2TSExtractor() {
        mPacketBuffer = new byte[TransportPacket.PACKET_SIZE];
        mTransportStream = new TransportStream();
    }

    @Override
    public void setMediaSource(MediaSource source) {
        mSource = source;
    }

    @Override
    public MediaSample readSample() {
        MediaSample sample = null;

        while (true) {
            TransportPacket packet = readTransportPacket();
            if (packet == null) {
                break;
            }

            mTransportStream.putPacket(packet);

            /**
             * dequeue media sample from transport stream
             */
        }

        return sample;
    }

    /**
     * 读一个TS包
     */
    private TransportPacket readTransportPacket() {
        TransportPacket packet = null;

        try {
            int offset = 0;

            do {
                int ret = mSource.read(mPacketBuffer, offset,
                        TransportPacket.PACKET_SIZE - offset);
                if (ret < 0) {
                    break;
                }

                offset += ret;
            }
            while (offset < TransportPacket.PACKET_SIZE);

            if (offset == TransportPacket.PACKET_SIZE) {
                packet = TransportPacket.parse(mPacketBuffer);
            }
            else {
                Log.w(TAG, "data is not enough, discard");
            }
        }
        catch (IOException e) {
            Log.e(TAG, "read data error, " + e.getMessage());
        }

        return packet;
    }
}
