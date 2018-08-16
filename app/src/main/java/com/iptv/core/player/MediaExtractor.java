package com.iptv.core.player;

public interface MediaExtractor {
    /**
     * 设置源
     */
    void setMediaSource(MediaSource source);

    /**
     * 读取Sample
     */
    MediaSample readSample();
}
