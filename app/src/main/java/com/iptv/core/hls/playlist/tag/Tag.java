package com.iptv.core.hls.playlist.tag;

/**
 * 标签
 */
public abstract class Tag {
    protected String mName;

    /**
     * 构造函数
     */
    public Tag(String name) {
        mName = name;
    }

    /**
     * 获取名称
     */
    public String getName() {
        return mName;
    }

    /**
     * 标签名
     */
    public static class Name {
        public static final String M3U = "#EXTM3U";
        public static final String VERSION = "#EXT-X-VERSION";

        public static final String TARGET_DURATION = "#EXT-X-TARGETDURATION";
        public static final String MEDIA_SEQUENCE = "#EXT-X-MEDIA-SEQUENCE";
        public static final String DISCONTINUITY_SEQUENCE = "#EXT-X-DISCONTINUITY-SEQUENCE";
        public static final String END_LIST = "#EXT-X-ENDLIST";
        public static final String PLAYLIST_TYPE = "#EXT-X-PLAYLIST-TYPE";
        public static final String I_FRAMES_ONLY = "#EXT-X-I-FRAMES-ONLY";

        public static final String INF = "#EXTINF";
        public static final String KEY = "#EXT-X-KEY";
        public static final String DISCONTINUITY = "#EXT-X-DISCONTINUITY";
        public static final String BYTE_RANGE = "#EXT-X-BYTERANGE";
        public static final String MAP = "#EXT-X-MAP";

        public static final String MEDIA = "#EXT-X-MEDIA";
        public static final String STREAM_INF = "#EXT-X-STREAM-INF";
        public static final String I_FRAME_STREAM_INF = "EXT-X-I-FRAME-STREAM-INF";

        /**
         * 构造函数（私有属性，不允许创建实例）
         */
        private Name() {
            /**
             * nothing
             */
        }
    }
}
