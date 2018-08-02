package com.iptv.core.hls.session;

import android.os.HandlerThread;

import com.iptv.core.hls.playlist.Playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Session {
    private static final String TAG = "Session";

    private String mMasterUrl;
    private Map<String, String> mProperties;

    private List<PlaylistSource> mSourceList;

    private HandlerThread mDriverThread;

    public Session(String masterUrl, Map<String, String> properties) {
        mMasterUrl = masterUrl;
        mProperties = properties;

        mSourceList = new ArrayList<PlaylistSource>(3);

        mDriverThread = new HandlerThread("session driver thread");
        mDriverThread.start();
    }

    /**
     * 添加源（MasterUrl是MediaPlaylist）
     */
    public void addSource(Playlist playlist) {
        PlaylistSource source = new PlaylistSource(mDriverThread.getLooper(),
                mMasterUrl, mProperties);
        source.setPlaylist(playlist); //避免重复加载

        mSourceList.add(source);
    }

    /**
     * 添加源（MasterUrl是MasterPlaylist）
     */
    public void addSource(String uri) {
        PlaylistSource source = new PlaylistSource(mDriverThread.getLooper(),
                Utils.makeUrl(mMasterUrl, uri), mProperties);

        mSourceList.add(source);
    }

    /**
     * 开始
     */
    public void start() {
        for (PlaylistSource source : mSourceList) {
            source.start();
        }
    }

    /**
     * 读取媒体数据
     */

    /**
     * 停止
     */
    public void stop() {
        for (PlaylistSource source : mSourceList) {
            source.stop();
        }
    }

    /**
     * 释放
     */
    public void release() {
        mDriverThread.quit();

        mSourceList.clear();
    }
}
