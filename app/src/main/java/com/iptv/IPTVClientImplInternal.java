package com.iptv;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Surface;

import com.iptv.channel.ChannelTable;
import com.iptv.player.Player;
import com.iptv.source.Source;
import com.iptv.source.firetv.FireTVSource;

import java.util.Map;

class IPTVClientImplInternal implements Handler.Callback, Player.Listener {
    private static final String TAG = "IPTVClientImplInternal";

    private static final int ST_INIT = 0;
    private static final int ST_IDLE = 1;
    private static final int ST_BUFFERING = 2;
    private static final int ST_READY = 3;
    private static final int ST_PLAY = 4;

    private static final int MSG_LOAD = 0;
    private static final int MSG_SET_CHANNEL_SOURCE = 1;
    private static final int MSG_SET_OUTPUT_SURFACE = 2;
    private static final int MSG_START_PLAY = 3;
    private static final int MSG_STOP_PLAY = 4;
    private static final int MSG_SET_VOLUME = 5;
    private static final int MSG_RELEASE = 6;

    public static final int MSG_ERROR = 100;
    public static final int MSG_LOAD_COMPLETE = 101;
    public static final int MSG_PREPARE_COMPLETE = 102;

    private Handler mEventHandler;

    private HandlerThread mHandlerThread;
    private Handler mHandler;

    private Source mSource;
    private Player mPlayer;

    private int mState;

    public IPTVClientImplInternal(Handler eventHandler) {
        mEventHandler = eventHandler;

        mHandlerThread = new HandlerThread("iptv client internal thread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper(), this);

        mState = ST_INIT;
    }

    public void load(Context context) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_LOAD;
        msg.obj = context;
        msg.sendToTarget();
    }

    public void setChannelSource(String source) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_CHANNEL_SOURCE;
        msg.obj = source;
        msg.sendToTarget();
    }

    public void setOutputSurface(Surface surface) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_OUTPUT_SURFACE;
        msg.obj = surface;
        msg.sendToTarget();
    }

    public void startPlay() {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_START_PLAY;
        msg.sendToTarget();
    }

    public void stopPlay() {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_STOP_PLAY;
        msg.sendToTarget();
    }

    public void setVolume(float volume) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_VOLUME;
        msg.obj = volume;
        msg.sendToTarget();
    }

    public void release() {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_RELEASE;
        msg.sendToTarget();

        /**
         * 等待消息处理完毕
         */
        boolean isReleased = false;
        do {
            try {
                wait();
                isReleased = true;
            }
            catch (InterruptedException e) {
                //ignore
            }
        }
        while (!isReleased);
    }

    @Override
    public void onPrepareComplete() {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_PREPARE_COMPLETE;
        msg.sendToTarget();
    }

    @Override
    public void onError(String error) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_ERROR;
        msg.sendToTarget();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_LOAD: {
                Context context = (Context)msg.obj;
                handleLoad(context);

                break;
            }
            case MSG_SET_CHANNEL_SOURCE: {
                String source = (String)msg.obj;
                handleSetChannelSource(source);

                break;
            }
            case MSG_SET_OUTPUT_SURFACE: {
                Surface surface = (Surface)msg.obj;
                handleSetOutputSurface(surface);

                break;
            }
            case MSG_START_PLAY: {
                handleStartPlay();

                break;
            }
            case MSG_STOP_PLAY: {
                handleStopPlay();

                break;
            }
            case MSG_SET_VOLUME: {
                float volume = (float)msg.obj;
                handleSetVolume(volume);

                break;
            }
            case MSG_RELEASE: {
                handleRelease();

                break;
            }
            case MSG_PREPARE_COMPLETE: {
                handlePrepareComplete();

                break;
            }
            default: {
                return false;
            }
        }

        return true;
    }

    private void handleLoad(Context context) {
        if (!checkState(ST_INIT)) {
            postErrorEvent("should be in INIT state");
        }
        else {
            mSource = new FireTVSource(context);

            if (!mSource.setup()) {
                postErrorEvent(mSource.getName() + " setup fail");
            }
            else {
                setState(ST_IDLE);

                postLoadCompleteEvent(mSource.getChannelTable());
            }
        }
    }

    private void handleSetChannelSource(String source) {
        if (!checkState(ST_IDLE)) {
            postErrorEvent("should be in IDLE state");
        }
        else {
            Map<String, String> parameters = mSource.decodeSource(source);
            if (parameters == null || !parameters.containsKey("url")) {
                postErrorEvent(mSource.getName() + " decode " + source + " fail");
            }
            else {
                String url = parameters.remove("url");
                if (url.isEmpty()) {
                    postErrorEvent(mSource.getName() + " decode " + source + " fail");
                }
                else {
                    mPlayer = null;
                    mPlayer.setListener(this);
                    mPlayer.prepare(url, parameters);

                    setState(ST_BUFFERING);
                }
            }
        }
    }

    private void handleSetOutputSurface(Surface surface) {
        if (!checkState(ST_BUFFERING) && !checkState(ST_READY)) {
            postErrorEvent("should be in BUFFERING or READY state");
        }
        else {
            mPlayer.setOutputSurface(surface);
        }
    }

    private void handleStartPlay() {
        if (!checkState(ST_READY)) {
            postErrorEvent("should be in READY state");
        }
        else {
            mPlayer.start();

            setState(ST_PLAY);
        }
    }

    private void handleStopPlay() {
        if (!checkState(ST_PLAY)) {
            postErrorEvent("should be in PLAY state");
        }
        else {
            mPlayer.stop();

            setState(ST_READY);
        }
    }

    private void handleSetVolume(float volume) {
        if (!checkState(ST_PLAY)) {
            postErrorEvent("should be in PLAY state");
        }
        else {
            mPlayer.setVolume(volume);
        }
    }

    private void handleRelease() {
        if (checkState(ST_PLAY)) {
            postErrorEvent("is playing, stop first");
        }
        else {
            if (mPlayer != null) {
                mPlayer.release();
                mPlayer = null;
            }

            if (mSource != null) {
                mSource.release();
                mSource = null;
            }

            /**
             * 忽略后续的消息
             */
            mHandlerThread.quit();
        }

        synchronized (this) {
            notifyAll();
        }
    }

    private void handlePrepareComplete() {
        if (!checkState(ST_BUFFERING)) {
            postErrorEvent("should be in BUFFERING state");
        }
        else {

        }
    }

    private void setState(int state) {
        mState = state;
    }

    private boolean checkState(int state) {
        return (mState == state);
    }

    private void postErrorEvent(String desc) {
        Message msg = mEventHandler.obtainMessage();
        msg.what = MSG_ERROR;
        msg.obj = desc;
        msg.sendToTarget();
    }

    private void postLoadCompleteEvent(ChannelTable channelTable) {
        Message msg = mEventHandler.obtainMessage();
        msg.what = MSG_LOAD_COMPLETE;
        msg.obj = channelTable;
        msg.sendToTarget();
    }

    private void postPrepareCompleteEvent() {
        Message msg = mEventHandler.obtainMessage();
        msg.what = MSG_PREPARE_COMPLETE;
        msg.sendToTarget();
    }
}
