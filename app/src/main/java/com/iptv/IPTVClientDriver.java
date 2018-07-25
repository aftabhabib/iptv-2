package com.iptv;

import android.content.Context;
import android.os.HandlerThread;
import android.view.Surface;

import com.iptv.channel.ChannelTable;

public class IPTVClientDriver implements IPTVClient {
    private static final String TAG = "IPTVClientDriver";

    private HandlerThread mDriverThread;

    private Listener mListener;
    private IPTVClientImpl mImpl;

    public IPTVClientDriver() {
        mDriverThread = new HandlerThread("iptv client driver thread");
        mDriverThread.start();

        mImpl = new IPTVClientImpl(mDriverThread.getLooper());
        mImpl.setDriver(this);
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void load(Context context) {
        mImpl.load(context);
    }

    @Override
    public void setOutputSurface(Surface surface) {
        mImpl.setOutputSurface(surface);
    }

    @Override
    public void setChannelSource(String source) {
        mImpl.setChannelSource(source);
    }

    @Override
    public void startPlay() {
        mImpl.startPlay();
    }

    @Override
    public void setVolume(float volume) {
        mImpl.setVolume(volume);
    }

    @Override
    public void stopPlay() {
        mImpl.stopPlay();
    }

    @Override
    public void release() {
        mImpl.release();
        mImpl = null;

        mDriverThread.quitSafely();
    }

    public void notifyError(String desc) {
        mListener.onError(desc);
    }

    public void notifyLoadComplete(ChannelTable channelTable) {
        mListener.onLoadComplete(channelTable);
    }

    public void notifyPrepareComplete() {
        mListener.onPrepareComplete();
    }
}
