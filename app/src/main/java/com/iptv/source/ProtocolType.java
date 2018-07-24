package com.iptv.source;

public class ProtocolType {
    private static final String TYPE_HTTP = "http://";
    private static final String TYPE_HTTPS = "https://";
    private static final String TYPE_RTSP = "rtsp://";
    private static final String TYPE_RTMP = "rtmp://";
    private static final String TYPE_NAGA = "vjms://";
    private static final String TYPE_TVBUS = "tvbus://";
    private static final String TYPE_FORCE_P2P = "p2p://";
    private static final String TYPE_FLASHGET_X = "flashgetx://";

    public static boolean isHttpOrHttps(String url) {
        if (url.startsWith(TYPE_HTTP) || url.startsWith(TYPE_HTTPS)) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean isRtsp(String url) {
        return url.startsWith(TYPE_RTSP);
    }

    public static boolean isRtmp(String url) {
        return url.startsWith(TYPE_RTMP);
    }

    public static boolean isNaga(String url) {
        return url.startsWith(TYPE_NAGA);
    }

    public static boolean isTVBus(String url) {
        return url.startsWith(TYPE_TVBUS);
    }

    public static boolean isForceP2P(String url) {
        return url.startsWith(TYPE_FORCE_P2P);
    }

    public static boolean isFlashgetX(String url) {
        return url.startsWith(TYPE_FLASHGET_X);
    }
}
