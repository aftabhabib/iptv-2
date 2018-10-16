package com.iptv.core.hls.playlist.datatype;

/**
 * 音频声道参数
 */
public final class AudioChannelParameter {
    private int mChannels;

    /**
     * 构造函数
     */
    public AudioChannelParameter(int channels) {
        mChannels = channels;
    }

    /**
     * 获取声道数
     */
    public int getChannels() {
        return mChannels;
    }

    @Override
    public String toString() {
        return String.valueOf(mChannels);
    }

    /**
     * 来自字符串
     */
    public static AudioChannelParameter valueOf(String str) {
        String[] results = str.split("/");

        /**
         * The first parameter is a count of audio channels expressed as a decimal-integer,
         * no other parameters are currently defined
         */
        return new AudioChannelParameter(Integer.parseInt(results[0]));
    }
}
