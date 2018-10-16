package com.iptv.core.hls.playlist.datatype;

/**
 * 枚举字符串
 */
public final class EnumeratedString {
    /**
     * 加密方式
     */
    public static final String AES_128 = "AES-128";
    public static final String SAMPLE_AES = "SAMPLE-AES";

    /**
     * 媒体类型
     */
    public static final String VIDEO = "VIDEO";
    public static final String AUDIO = "AUDIO";
    public static final String SUBTITLES = "SUBTITLES";
    public static final String CLOSED_CAPTIONS = "CLOSED-CAPTIONS";

    /**
     * HDCP层次
     */
    public static final String TYPE_0 = "TYPE-0";

    /**
     * 媒体播放列表类型
     */
    public static final String EVENT = "EVENT";
    public static final String VOD = "VOD";

    /**
     * 没有
     */
    public static final String NONE = "NONE";

    /**
     * 是、否
     */
    public static final String YES = "YES";
    public static final String NO = "NO";
}
