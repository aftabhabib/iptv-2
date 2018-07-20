package com.iptv.source;

import com.iptv.channel.ChannelTable;
import com.iptv.plugin.Plugin;

import java.util.List;

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
     * 获取插件
     */
    List<Plugin> getPluginList();
}
