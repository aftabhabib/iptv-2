package com.iptv.core.hls.playlist;

/**
 * 播放列表
 */
public abstract class Playlist {
    private String mPlaylistUrl;

    /**
     * 构造函数
     */
    public Playlist() {
        /**
         * nothing
         */
    }

    /**
     * 设置播放列表url
     */
    public void setPlaylistUrl(String playlistUrl) {
        mPlaylistUrl = playlistUrl;
    }
}
