package com.iptv.core.hls.playlist;

import java.util.ArrayList;
import java.util.List;

public final class Codec {
    private String mType;
    private String mProfile;

    private Codec(String type, String profile) {
        mType = type;
        mProfile = profile;
    }

    /**
     * 是不是Video类型
     */
    public boolean isVideoType() {
        return false;
    }

    /**
     * 是不是Audio类型
     */
    public boolean isAudioType() {
        return false;
    }

    public static List<Codec> parseList(String codecs) {
        List<Codec> codecList = new ArrayList<Codec>(2);

        String[] codecArray = codecs.split(",");
        for (int i = 0; i < codecArray.length; i++) {
            String[] result = codecArray[i].split(".");

            codecList.add(new Codec(result[0], result[1]));
        }

        return codecList;
    }
}
