package com.iptv.player.source;

import android.util.Log;

import com.nagasoft.player.VJListener;
import com.nagasoft.player.VJPlayer;

import java.io.IOException;

public class NagaSource extends LocalServiceSource implements VJListener {
    private static final String TAG = "NagaSource";

    private VJPlayer mPlayer;
    private boolean mIsServiceRunning = false;

    public NagaSource() {
        super();

        mPlayer = new VJPlayer();
        mPlayer.setVJListener(this);
        /**
         * FIXME: 不清楚参数的单位，暂时认为是秒
         */
        mPlayer.setVJMSBufferTimeout(10);
    }

    @Override
    public void onPlayURL(String url) {
        Log.d(TAG, "VJPlayer notify url " + url);

        mLocalUrl = url;
        /**
         * 得到mLocalUrl，解除startService的阻塞
         */
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "VJPlayer notify error " + error);
    }

    @Override
    protected void startService(String url) throws IOException {
        mPlayer.setURL(url);

        if (!mPlayer.start()) {
            throw new IOException("VJPlayer start fail");
        }

        /**
         * 等待VJPlayer的异步通知
         */
        boolean isInterrupted = false;
        do {
            try {
                wait();
            }
            catch (InterruptedException e) {
                isInterrupted = true;
            }
        }
        while (isInterrupted);

        mIsServiceRunning = true;
    }

    @Override
    protected void stopService() {
        if (mIsServiceRunning) {
            mPlayer.stop();
            mIsServiceRunning = false;
        }
    }
}
