package com.iptv;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;

import com.iptv.player.Player;
import com.iptv.source.Source;
import com.iptv.source.firetv.FireTVSource;

import java.util.Map;

class IPTVClientImpl extends Handler implements Player.Listener {
    private static final String TAG = "IPTVClientImpl";

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
    private static final int MSG_PLAYER_ERROR = 100;
    private static final int MSG_PLAYER_PREPARE_COMPLETE = 101;

    private int mState;
    private IPTVClientDriver mDriver;

    private Source mSource;
    private Player mPlayer;

    public IPTVClientImpl(Looper looper) {
        super(looper);

        mState = ST_INIT;
    }

    public void setDriver(IPTVClientDriver driver) {
        mDriver = driver;
    }

    public void load(Context context) {
        if (!checkState(ST_INIT)) {
            throw new IllegalStateException("should be in INIT state");
        }

        sendMessage(MSG_LOAD, context);
    }

    public void setChannelSource(String source) {
        if (!checkState(ST_IDLE)
                && !checkState(ST_READY)) {
            throw new IllegalStateException("should be in IDLE or READY state");
        }

        sendMessage(MSG_SET_CHANNEL_SOURCE, source);
    }

    public void setOutputSurface(Surface surface) {
        if (!checkState(ST_BUFFERING)
                && !checkState(ST_READY)) {
            throw new IllegalStateException("should be in BUFFERING or READY state");
        }

        sendMessage(MSG_SET_OUTPUT_SURFACE, surface);
    }

    public void startPlay() {
        if (!checkState(ST_READY)) {
            throw new IllegalStateException("should be in READY state");
        }

        sendMessage(MSG_START_PLAY);
    }

    public void stopPlay() {
        if (!checkState(ST_PLAY)) {
            throw new IllegalStateException("should be in PLAY state");
        }

        sendMessage(MSG_STOP_PLAY);
    }

    public void setVolume(float volume) {
        if (!checkState(ST_PLAY)) {
            throw new IllegalStateException("should be in PLAY state");
        }

        sendMessage(MSG_SET_VOLUME, volume);
    }

    public void release() {
        if (!checkState(ST_INIT)
                && !checkState(ST_IDLE)
                && !checkState(ST_BUFFERING)
                && !checkState(ST_READY)) {
            throw new IllegalStateException("should be in INIT, IDLE, BUFFERING or READY state");
        }

        sendMessage(MSG_RELEASE);
    }

    @Override
    public void onPrepareComplete() {
        if (!checkState(ST_BUFFERING)) {
            throw new IllegalStateException("should be in BUFFERING state");
        }

        sendMessage(MSG_PLAYER_PREPARE_COMPLETE);
    }

    @Override
    public void onError(String desc) {
        sendMessage(MSG_PLAYER_ERROR, desc);
    }

    private void sendMessage(int what) {
        sendMessage(what, null);
    }

    private void sendMessage(int what, Object obj) {
        Message msg = obtainMessage();
        msg.what = what;
        msg.obj = obj;

        msg.sendToTarget();
    }

    @Override
    public void handleMessage(Message msg) {
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
            case MSG_PLAYER_PREPARE_COMPLETE: {
                handlePlayerPrepareComplete();

                break;
            }
            case MSG_PLAYER_ERROR: {
                String desc = (String)msg.obj;
                handlePlayerError(desc);

                break;
            }
            default: {
                break;
            }
        }
    }

    private void handleLoad(Context context) {
        mSource = new FireTVSource(context);

        if (!mSource.setup()) {
            mDriver.notifyError(mSource.getName() + " setup fail");
        }
        else {
            setState(ST_IDLE);

            mDriver.notifyLoadComplete(mSource.getChannelTable());
        }
    }

    private void handleSetChannelSource(String source) {
        Map<String, String> parameters = mSource.decodeSource(source);
        if (parameters == null || !parameters.containsKey("url")) {
            mDriver.notifyError(mSource.getName() + " decode " + source + " fail");
        }
        else {
            String url = parameters.remove("url");
            if (url.isEmpty()) {
                mDriver.notifyError(mSource.getName() + " decode " + source + " fail");
            }
            else {
                /**
                 * FIXME：根据url的类型创建对应的Player
                 */
                mPlayer = null;
                mPlayer.setListener(this);
                mPlayer.prepare(url, parameters);

                setState(ST_BUFFERING);
            }
        }
    }

    private void handleSetOutputSurface(Surface surface) {
        mPlayer.setOutputSurface(surface);
    }

    private void handleStartPlay() {
        mPlayer.start();

        setState(ST_PLAY);
    }

    private void handleStopPlay() {
        mPlayer.stop();

        setState(ST_READY);
    }

    private void handleSetVolume(float volume) {
        mPlayer.setVolume(volume);
    }

    private void handleRelease() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

        if (mSource != null) {
            mSource.release();
            mSource = null;
        }
    }

    private void handlePlayerPrepareComplete() {
        setState(ST_READY);

        mDriver.notifyPrepareComplete();
    }

    private void handlePlayerError(String desc) {
        mDriver.notifyError(desc);
    }

    private void setState(int state) {
        synchronized (this) {
            mState = state;
        }
    }

    private boolean checkState(int state) {
        boolean ret;

        synchronized (this) {
            ret = (mState == state);
        }

        return ret;
    }
}
