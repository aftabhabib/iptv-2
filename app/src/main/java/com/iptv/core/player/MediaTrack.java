package com.iptv.core.player;

public interface MediaTrack {
    /**
     * 获取类型
     */
    int getType();

    /**
     * 获取元信息
     */
    MetaData getMetaData();

    /**
     * 读取采样数据
     */
    MediaBuffer read();
}
