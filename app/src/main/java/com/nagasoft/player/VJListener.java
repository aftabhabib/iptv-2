package com.nagasoft.player;

public interface VJListener {
    /**
     * 播放地址
     * @param url
     */
    void onPlayURL(String url);

    /**
     * 出错
     * @param error
     */
    void onError(int error);
}
