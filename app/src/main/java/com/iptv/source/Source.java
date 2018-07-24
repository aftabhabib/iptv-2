package com.iptv.source;

import com.iptv.channel.ChannelTable;

import java.util.Map;

public interface Source {
    /**
     * 获取名称
     */
    String getName();

    /**
     * 启动
     */
    boolean setup();
    /**
     * 获取频道表
     */
    ChannelTable getChannelTable();

    /**
     * 数据源解码
     */
    Map<String, String> decodeSource(String source);

    /**
     * 释放
     */
    void release();
}
