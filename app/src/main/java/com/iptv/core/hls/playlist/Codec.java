package com.iptv.core.hls.playlist;

import java.util.ArrayList;
import java.util.List;

final class Codec {
    /**
     * 仅列举了个别常见的，更多见http://mp4ra.org/#/codecs
     */
    private static final String VIDEO_CODEC_AVC1 = "avc1";
    private static final String VIDEO_CODEC_MPEG4 = "mp4v";
    private static final String VIDEO_CODEC_VP9 = "vp09";
    private static final String AUDIO_CODEC_MPEG4 = "mp4a";
    private static final String AUDIO_CODEC_OPUS = "Opus";
    private static final String AUDIO_CODEC_AC3 = "ac-3";
    private static final String AUDIO_CODEC_EC3 = "ec-3";

    private String mType;

    private Codec(String type) {
        mType = type;
    }

    /**
     * 是不是Video类型
     */
    public boolean isVideoType() {
        if (mType.equals(VIDEO_CODEC_AVC1)
                || mType.equals(VIDEO_CODEC_MPEG4)
                || mType.equals(VIDEO_CODEC_VP9)) {
            return true;
        }

        return false;
    }

    /**
     * 是不是Audio类型
     */
    public boolean isAudioType() {
        if (mType.equals(AUDIO_CODEC_MPEG4)
                || mType.equals(AUDIO_CODEC_OPUS)
                || mType.equals(AUDIO_CODEC_AC3)
                || mType.equals(AUDIO_CODEC_EC3)) {
            return true;
        }

        return false;
    }

    public static List<Codec> parseList(String codecs) {
        List<Codec> codecList = new ArrayList<Codec>(2);

        String[] codecArray = codecs.split(",");
        for (int i = 0; i < codecArray.length; i++) {
            String[] result = codecArray[i].split(".");

            /**
             * the first element is determined by the MIME type
             */
            codecList.add(new Codec(result[0]));
        }

        return codecList;
    }
}
