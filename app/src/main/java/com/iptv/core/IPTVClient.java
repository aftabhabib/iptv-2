package com.iptv.core;

import android.content.Context;
import android.view.Surface;

import com.iptv.core.channel.ChannelTable;

public interface IPTVClient {
    /**
     * 事件回调
     */
    interface Listener {
        /**
         * 频道表载入完毕
         */
        void onLoadComplete(ChannelTable channelTable);

        /**
         * 频道源准备好了
         */
        void onPrepareComplete();

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
     * 载入频道信息
     */
    void load(Context context);

    /**
     * 设置输出画面
     */
    void setOutputSurface(Surface surface);

    /**
     * 设置频道源
     */
    void setChannelSource(String source);

    /**
     * 开始播放
     */
    void startPlay();

    /**
     * 设置音量
     */
    void setVolume(float volume);

    /**
     * 停止播放
     */
    void stopPlay();

    /**
     * 释放
     */
    void release();
}
