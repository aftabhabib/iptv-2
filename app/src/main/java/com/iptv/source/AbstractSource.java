package com.iptv.source;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public abstract class AbstractSource extends Handler implements Source {
    private static final String TAG = "AbstractSource";

    private static final int MSG_SETUP = 0;

    public AbstractSource(Looper looper) {
        super(looper);
    }

    public void setup(OnSetupListener listener) {
        Message msg = Message.obtain(this);
        msg.what = MSG_SETUP;
        msg.obj = listener;

        sendMessage(msg);
    }

    @Override
    public void	handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SETUP: {
                OnSetupListener listener = (OnSetupListener)msg.obj;
                onSetup(listener);

                break;
            }
            default: {
                Log.w(TAG, "unknown message " + msg.what);
                break;
            }
        }
    }

    protected abstract void onSetup(OnSetupListener listener);
}
