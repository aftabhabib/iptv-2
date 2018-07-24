package com.iptv.player;

import android.view.Surface;

import java.util.Map;

public interface Player {
    interface Listener {
        /**
         * 媒体数据准备好了
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
     * 准备媒体数据
     */
    void prepare(String url, Map<String, String> properties);

    /**
     * 设置输出画面
     */
    void setOutputSurface(Surface surface);

    /**
     * 开始
     */
    void start();

    /**
     * 停止
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
