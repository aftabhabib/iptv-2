package com.iptv.core.hls.playlist;

/**
 * 格式
 */
final class Codec {
    /**
     * 是不是音频格式
     */
    public static boolean isAudioFormat(String format) {
        if (format.startsWith("mp4a")
                || format.startsWith("ac-3")
                || format.startsWith("ec-3")) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 是不是视频格式
     */
    public static boolean isVideoFormat(String format) {
        if (format.startsWith("avc1")
                || format.startsWith("mp4v")
                || format.startsWith("vp09")
                || format.startsWith("vp08")) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 构造函数（私有属性，不允许创建实例）
     */
    private Codec() {
        /**
         * nothing
         */
    }
}
