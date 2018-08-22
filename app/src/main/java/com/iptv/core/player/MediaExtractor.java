package com.iptv.core.player;

public interface MediaExtractor {
    /**
     * 设置源
     */
    void setDataSource(DataSource source);

    /**
     * 获取元信息
     */
    MetaData getMetaData();

    /**
     * 获取所有轨道
     */
    MediaTrack[] getTracks();

    /**
     * 选择轨道
     */
    void selectTrack(int trackIndex);
}
