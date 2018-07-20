package com.iptv.source;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.iptv.channel.ChannelTable;

public abstract class BaseClient extends Handler {
    private static final String TAG = "BaseClient";

    private static final int MSG_SETUP = 0;
    private static final int MSG_DECODE_SOURCE = 1;

    protected Listener mListener;

    public BaseClient(Looper looper) {
        super(looper);
    }

    public interface Listener {
        void onSetup(ChannelTable table);

        void onDecodeSource(String source);

        void onError(String error);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setup() {
        Message msg = Message.obtain(this);
        msg.what = MSG_SETUP;

        sendMessage(msg);
    }

    public void decodeSource(String source) {
        Message msg = Message.obtain(this);
        msg.what = MSG_DECODE_SOURCE;
        msg.obj = source;

        sendMessage(msg);
    }

    @Override
    public void	handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SETUP: {
                onSetup();
                break;
            }
            case MSG_DECODE_SOURCE: {
                String source = (String)msg.obj;
                onDecodeSource(source);
                break;
            }
            default: {
                Log.w(TAG, "unknown message " + msg.what);
                break;
            }
        }
    }

    protected abstract void onSetup();

    protected abstract void onDecodeSource(String source);
}
