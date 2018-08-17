package com.iptv.core.source;

import com.iptv.core.channel.ChannelGroup;

import java.util.Map;

/**
 * 资源
 */
public interface Resource {
    /**
     * 事件回调
     */
    interface Listener {
        /**
         * 加载频道表响应
         */
        void onLoadChannelTable(ChannelGroup[] groups);

        /**
         * 解码数据源响应
         */
        void onDecodeSource(String url, Map<String, String> properties);

        /**
         * 出错
         */
        void onError(String error);
    }

    /**
     * 注册事件回调
     */
    void setListener(Listener listener);

    /**
     * 获取名称
     */
    String getName();

    /**
     * 加载频道表
     */
    void loadChannelTable();

    /**
     * 解码数据源
     */
    void decodeSource(String source);

    /**
     * 释放
     */
    void release();
}
