package com.iptv.core.hls.playlist;

/**
 * 格式
 */
final class Codec {
    /**
     * 是不是音频格式
     */
    public static boolean isAudioFormat(String format) {
        if (format.startsWith("mp4a")) {
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
                || format.startsWith("mp4v")) {
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
