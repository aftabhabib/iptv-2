package com.iptv.core.player;

import android.view.Surface;

import java.util.Map;

public interface Player {
    /**
     * 事件回调
     */
    interface Listener {
        /**
         * 加载媒体
         */
        void onLoadMedia();

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
     * 设置媒体源
     */
    void setDataSource(String url, Map<String, String> properties);

    /**
     * 加载媒体
     */
    void loadMedia();

    /**
     * 设置输出画面
     */
    void setOutputSurface(Surface surface);

    /**
     * 开始播放
     */
    void start();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 设置音量
     */
    void setVolume(float volume);

    /**
     * 释放
     */
    void release();
}
