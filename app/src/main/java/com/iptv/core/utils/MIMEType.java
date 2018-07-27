package com.iptv.core.utils;

public class MIMEType {
    private static final String TYPE_M3U8 = "application/x-mpegURL";
    private static final String TYPE_M3U8_STD = "application/vnd.apple.mpegurl";

    public static boolean isM3U8(String type) {
        if (type.equalsIgnoreCase(TYPE_M3U8) || type.equalsIgnoreCase(TYPE_M3U8_STD)) {
            return true;
        }
        else {
            return false;
        }
    }
}
