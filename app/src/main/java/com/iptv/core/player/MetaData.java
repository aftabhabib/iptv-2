package com.iptv.core.player;

import java.util.HashMap;
import java.util.Map;

/**
 * 元信息
 */
public class MetaData {
    /**
     * Media
     */
    public static final String KEY_MIME = "mime";
    public static final String KEY_DECODING_TIMESTAMP = "decoding_timestamp";
    public static final String KEY_PRESENTATION_TIMESTAMP = "presentation_timestamp";

    /**
     * Video
     */
    public static final String KEY_VIDEO_COLOR_SPACE = "color_space";
    public static final String KEY_VIDEO_WIDTH = "width";
    public static final String KEY_VIDEO_HEIGHT = "height";
    public static final String KEY_VIDEO_FRAME_RATE = "frame_rate";
    public static final String KEY_VIDEO_ROTATE = "rotate";

    /**
     * Audio
     */
    public static final String KEY_AUDIO_BITS_PER_SAMPLE = "bits_per_sample";
    public static final String KEY_AUDIO_CHANNELS = "channels";
    public static final String KEY_AUDIO_SAMPLE_RATE = "sample_rate";

    /**
     * Codec
     */
    public static final String KEY_PROFILE = "profile";
    public static final String KEY_LEVEL = "level";
    public static final String KEY_BITRATE = "bitrate";
    public static final String KEY_ES_DESCRIPTORS = "es_descriptors";
    public static final String KEY_AVC_CONFIGURATION = "avc_configuration";

    /**
     * M3U8
     */
    public static final String KEY_VERSION = "version";
    public static final String KEY_TARGET_DURATION = "target_duration";
    public static final String KEY_MEDIA_SEQUENCE = "media_sequence";
    public static final String KEY_END_LIST = "end_list";

    public static final String KEY_SEGMENT_DURATION = "segment_duration";
    public static final String KEY_SEGMENT_TITLE = "segment_title";

    public static final String KEY_RANGE_OFFSET = "range_offset";
    public static final String KEY_RANGE_LENGTH = "range_length";

    public static final String KEY_CIPHER_METHOD = "cipher_method";
    public static final String KEY_CIPHER_IV = "cipher_iv";

    public static final String KEY_DISCONTINUITY = "discontinuity";

    public static final String KEY_MEDIA_TYPE = "media_type";
    public static final String KEY_GROUP_ID = "group_id";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_DEFAULT_SELECT = "default_select";

    public static final String KEY_BANDWIDTH = "bandwidth";
    public static final String KEY_AUDIO_GROUP_ID = "audio_group_id";
    public static final String KEY_VIDEO_GROUP_ID = "video_group_id";
    public static final String KEY_SUBTITLE_GROUP_ID = "subtitle_group_id";

    private Map<String, Object> mTable;

    /**
     * 构造函数
     */
    public MetaData() {
        mTable = new HashMap<String, Object>();
    }

    /**
     * 是否有数据
     */
    public boolean isEmpty() {
        return mTable.isEmpty();
    }

    /**
     * 是否包含
     */
    public boolean containsKey(String key) {
        return mTable.containsKey(key);
    }

    /**
     * 以字符串类型读值
     */
    public String getString(String key) {
        return (String)mTable.get(key);
    }

    /**
     * 以布尔类型读值
     */
    public boolean getBoolean(String key) {
        return (boolean)mTable.get(key);
    }

    /**
     * 以long类型读值
     */
    public long getLong(String key) {
        return (long)mTable.get(key);
    }

    /**
     * 以int类型读值
     */
    public int getInteger(String key) {
        return (int)mTable.get(key);
    }

    /**
     * 以float类型读值
     */
    public float getFloat(String key) {
        return (float)mTable.get(key);
    }

    /**
     * 以byte数组类型读值
     */
    public byte[] getByteArray(String key) {
        return (byte[])mTable.get(key);
    }

    /**
     * 放入字符串类型的值
     */
    public void putString(String key, String value) {
        mTable.put(key, value);
    }

    /**
     * 放入布尔类型的值
     */
    public void putBoolean(String key, boolean value) {
        mTable.put(key, value);
    }

    /**
     * 放入long类型的值
     */
    public void putLong(String key, long value) {
        mTable.put(key, value);
    }

    /**
     * 放入int类型的值
     */
    public void putInteger(String key, int value) {
        mTable.put(key, value);
    }

    /**
     * 放入float类型的值
     */
    public void putFloat(String key, float value) {
        mTable.put(key, value);
    }

    /**
     * 放入byte数组类型的值
     */
    public void putByteArray(String key, byte[] value) {
        mTable.put(key, value);
    }

    /**
     * 清除
     */
    public void clear() {
        mTable.clear();
    }
}
