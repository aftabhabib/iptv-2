package com.iptv.core;

import android.content.Context;
import android.os.HandlerThread;
import android.view.Surface;

import com.iptv.core.channel.ChannelTable;

public class IPTVClientDriver implements IPTVClient {
    private static final String TAG = "IPTVClientDriver";

    private HandlerThread mDriverThread;

    private Listener mListener;
    private IPTVClientInternal mInternal;

    public IPTVClientDriver() {
        mDriverThread = new HandlerThread("iptv client driver thread");
        mDriverThread.start();

        mInternal = new IPTVClientInternal(mDriverThread.getLooper());
        mInternal.setDriver(this);
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void load(Context context) {
        mInternal.load(context);
    }

    @Override
    public void setOutputSurface(Surface surface) {
        mInternal.setOutputSurface(surface);
    }

    @Override
    public void setChannelSource(String source) {
        mInternal.setChannelSource(source);
    }

    @Override
    public void startPlay() {
        mInternal.startPlay();
    }

    @Override
    public void setVolume(float volume) {
        mInternal.setVolume(volume);
    }

    @Override
    public void stopPlay() {
        mInternal.stopPlay();
    }

    @Override
    public void release() {
        mInternal.release();
        mInternal = null;

        mDriverThread.quitSafely();
    }

    public void notifyError(String desc) {
        if (mListener != null) {
            mListener.onError(desc);
        }
    }

    public void notifyLoadComplete(ChannelTable channelTable) {
        if (mListener != null) {
            mListener.onLoadComplete(channelTable);
        }
    }

    public void notifyPrepareComplete() {
        if (mListener != null) {
            mListener.onPrepareComplete();
        }
    }
}
