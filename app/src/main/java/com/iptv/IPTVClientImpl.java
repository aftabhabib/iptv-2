package com.iptv;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;

import com.iptv.channel.ChannelTable;

public class IPTVClientImpl implements IPTVClient {
    private static final String TAG = "IPTVClientImpl";

    private EventHandler mEventHandler;
    private IPTVClientImplInternal mClientInternal;

    private Listener mListener;

    public IPTVClientImpl() {
        Looper looper = Looper.myLooper();
        if (looper == null) {
            looper = Looper.getMainLooper();
        }
        mEventHandler = new EventHandler(looper);

        mClientInternal = new IPTVClientImplInternal(mEventHandler);
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void load(Context context) {
        mClientInternal.load(context);
    }

    @Override
    public void setOutputSurface(Surface surface) {
        mClientInternal.setOutputSurface(surface);
    }

    @Override
    public void setChannelSource(String source) {
        mClientInternal.setChannelSource(source);
    }

    @Override
    public void startPlay() {
        mClientInternal.startPlay();
    }

    @Override
    public void setVolume(float volume) {
        mClientInternal.setVolume(volume);
    }

    @Override
    public void stopPlay() {
        mClientInternal.stopPlay();
    }

    @Override
    public void release() {
        mClientInternal.release();

        mEventHandler.removeCallbacksAndMessages(null);
    }

    private class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IPTVClientImplInternal.MSG_ERROR: {
                    String error = (String)msg.obj;
                    handleError(error);

                    break;
                }
                case IPTVClientImplInternal.MSG_LOAD_COMPLETE: {
                    ChannelTable channelTable = (ChannelTable)msg.obj;
                    handleLoadComplete(channelTable);

                    break;
                }
                case IPTVClientImplInternal.MSG_PREPARE_COMPLETE: {
                    handlePrepareComplete();

                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private void handleError(String desc) {
        mListener.onError(desc);
    }

    private void handleLoadComplete(ChannelTable channelTable) {
        mListener.onLoadComplete(channelTable);
    }

    private void handlePrepareComplete() {
        mListener.onPrepareComplete();
    }
}
