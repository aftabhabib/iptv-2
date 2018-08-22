package com.iptv.core;

import android.content.Context;
import android.os.HandlerThread;
import android.view.Surface;

import com.iptv.core.channel.ChannelGroup;
import com.iptv.core.player.Manifest;
import com.iptv.core.player.Player;
import com.iptv.core.player.PlayerImpl;
import com.iptv.core.resource.Resource;
import com.iptv.core.resource.ResourceFactory;

import java.util.Map;

public final class IPTVClientImpl implements IPTVClient {
    private HandlerThread mDriverThread;

    private Resource mResource;
    private Player mPlayer;

    private Listener mListener = null;

    /**
     * 构造函数
     */
    public IPTVClientImpl(Context context) {
        mDriverThread = new HandlerThread("driver thread");
        mDriverThread.start();

        mResource = ResourceFactory.createResource(mDriverThread.getLooper(), context);
        mResource.setListener(mResourceListener);

        mPlayer = new PlayerImpl(mDriverThread.getLooper(), context);
        mPlayer.setListener(mPlayerListener);
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void loadChannelTable() {
        mResource.loadChannelTable();
    }

    @Override
    public void loadMedia(String url) {
        mResource.decodeUrl(url);
    }

    @Override
    public void setOutputSurface(Surface surface) {
        mPlayer.setOutputSurface(surface);
    }

    @Override
    public void startPlay() {
        mPlayer.start();
    }

    @Override
    public void setVolume(float volume) {
        mPlayer.setVolume(volume);
    }

    @Override
    public void stopPlay() {
        mPlayer.stop();
    }

    @Override
    public void selectTrack(int trackIndex) {
        mPlayer.selectTrack(trackIndex);
    }

    @Override
    public void release() {
        mDriverThread.quitSafely();
        mDriverThread = null;

        mPlayer.release();
        mPlayer = null;

        mResource.release();
        mResource = null;
    }

    /**
     * 资源事件回调
     */
    private Resource.Listener mResourceListener = new Resource.Listener() {

        @Override
        public void onLoadChannelTable(ChannelGroup[] groups) {
            notifyChannelTable(groups);
        }

        @Override
        public void onDecodeUrl(String url, Map<String, String> properties) {
            notifyDataSource(url, properties);
        }

        @Override
        public void onError(String error) {
            notifyError(error);
        }
    };

    /**
     * 播放器事件回调
     */
    private Player.Listener mPlayerListener = new Player.Listener() {

        @Override
        public void onLoadMedia(Manifest manifest) {
            notifyMedia(manifest);
        }

        @Override
        public void onError(String error) {
            notifyError(error);
        }
    };

    /**
     * 通知出错
     */
    private void notifyError(String error) {
        if (mListener != null) {
            mListener.onError(error);
        }
    }

    /**
     * 通知频道表
     */
    private void notifyChannelTable(ChannelGroup[] groups) {
        if (mListener != null) {
            mListener.onLoadChannelTable(groups);
        }
    }

    /**
     * 通知数据源
     */
    private void notifyDataSource(String url, Map<String, String> properties) {
        mPlayer.setDataSource(url, properties);
        mPlayer.loadMedia();
    }

    /**
     * 通知媒体
     */
    private void notifyMedia(Manifest manifest) {
        if (mListener != null) {
            mListener.onLoadMedia(manifest);
        }
    }
}
