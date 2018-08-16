package com.iptv.core;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Surface;

import com.iptv.core.player.Player;
import com.iptv.core.player.PlayerImpl;
import com.iptv.core.source.Resource;
import com.iptv.core.source.ResourceFactory;

public class IPTVClientImpl implements IPTVClient {
    private static final String TAG = "IPTVClientImpl";

    private static final int MSG_LOAD_CHANNEL_TABLE = 0;
    private static final int MSG_LOAD_MEDIA = 1;
    private static final int MSG_SET_OUTPUT_SURFACE = 2;
    private static final int MSG_START_PLAY = 3;
    private static final int MSG_STOP_PLAY = 4;
    private static final int MSG_SET_VOLUME = 5;

    private HandlerThread mDriverThread;
    private Handler mHandler;

    private Resource mSource;
    private Player mPlayer;

    private Listener mListener = null;

    /**
     * 构造函数
     */
    public IPTVClientImpl(Context context) {
        mDriverThread = new HandlerThread("iptv client driver thread");
        mDriverThread.start();

        mHandler = new Handler(mDriverThread.getLooper(), mHandlerCallback);

        mSource = ResourceFactory.createResource(ResourceFactory.RESOURCE_TYPE_FIRETV, context);
        mPlayer = new PlayerImpl(context);
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void loadChannelTable() {
        sendMessage(MSG_LOAD_CHANNEL_TABLE);
    }

    @Override
    public void loadMedia(String url) {
        sendMessage(MSG_LOAD_MEDIA, url);
    }

    @Override
    public void setOutputSurface(Surface surface) {
        sendMessage(MSG_SET_OUTPUT_SURFACE, surface);
    }

    @Override
    public void startPlay() {
        sendMessage(MSG_START_PLAY);
    }

    @Override
    public void setVolume(float volume) {
        sendMessage(MSG_SET_VOLUME, volume);
    }

    @Override
    public void stopPlay() {
        sendMessage(MSG_STOP_PLAY);
    }

    @Override
    public void release() {
        mDriverThread.quitSafely();

        mPlayer.release();
        mSource.release();
    }

    /**
     * 发送消息
     */
    private void sendMessage(int what) {
        mHandler.sendEmptyMessage(what);
    }

    /**
     * 发送消息
     */
    private void sendMessage(int what, Object obj) {
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        msg.obj = obj;

        mHandler.sendMessage(msg);
    }

    /**
     * 消息处理
     */
    private Handler.Callback mHandlerCallback = new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_CHANNEL_TABLE: {
                    onLoadChannelTable();

                    break;
                }
                case MSG_LOAD_MEDIA: {
                    String url = (String) msg.obj;
                    onLoadMedia(url);

                    break;
                }
                case MSG_SET_OUTPUT_SURFACE: {
                    Surface surface = (Surface) msg.obj;
                    onSetOutputSurface(surface);

                    break;
                }
                case MSG_START_PLAY: {
                    onStartPlay();

                    break;
                }
                case MSG_STOP_PLAY: {
                    onStopPlay();

                    break;
                }
                case MSG_SET_VOLUME: {
                    float volume = (float) msg.obj;
                    onSetVolume(volume);

                    break;
                }
                default: {
                    return false;
                }
            }

            return true;
        }
    };

    /**
     * 响应加载频道表
     */
    private void onLoadChannelTable() {
        //
    }

    /**
     * 响应加载频道源
     */
    private void onLoadMedia(String url) {
        //
    }

    /**
     * 响应设置播放窗口
     */
    private void onSetOutputSurface(Surface surface) {
        mPlayer.setOutputSurface(surface);
    }

    /**
     * 响应开始播放
     */
    private void onStartPlay() {
        mPlayer.start();
    }

    /**
     * 响应设置音量
     */
    private void onSetVolume(float volume) {
        mPlayer.setVolume(volume);
    }

    /**
     * 响应停止播放
     */
    private void onStopPlay() {
        mPlayer.stop();
    }

    /**
     * 通知出错
     */
    private void notifyError(String error) {
        if (mListener != null) {
            mListener.onError(error);
        }
    }
}
