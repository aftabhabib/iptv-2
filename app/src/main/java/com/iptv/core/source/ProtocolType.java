package com.iptv.core.source;

public class ProtocolType {
    private static final String TYPE_HTTP = "http://";
    private static final String TYPE_HTTPS = "https://";
    private static final String TYPE_RTSP = "rtsp://";
    private static final String TYPE_RTMP = "rtmp://";
    private static final String TYPE_NAGA = "vjms://";
    private static final String TYPE_TVBUS = "tvbus://";
    private static final String TYPE_FORCE_P2P = "p2p://";

    /**
     * 检查url是否是http或https协议
     */
    public static boolean isHttpOrHttps(String url) {
        if (url.startsWith(TYPE_HTTP) || url.startsWith(TYPE_HTTPS)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 检查url是否是rtsp协议
     */
    public static boolean isRtsp(String url) {
        return url.startsWith(TYPE_RTSP);
    }

    /**
     * 检查url是否是rtmp协议
     */
    public static boolean isRtmp(String url) {
        return url.startsWith(TYPE_RTMP);
    }

    /**
     * 检查url是否是naga协议
     */
    public static boolean isNaga(String url) {
        return url.startsWith(TYPE_NAGA);
    }

    /**
     * 检查url是否是tvbus协议
     */
    public static boolean isTVBus(String url) {
        return url.startsWith(TYPE_TVBUS);
    }

    /**
     * 检查url是否是force p2p协议
     */
    public static boolean isForceP2P(String url) {
        return url.startsWith(TYPE_FORCE_P2P);
    }
}
