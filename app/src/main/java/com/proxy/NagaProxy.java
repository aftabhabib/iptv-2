package com.proxy;

import android.util.Log;

import com.nagasoft.player.VJListener;
import com.nagasoft.player.VJPlayer;

class NagaProxy implements Proxy, VJListener {
    private static final String TAG = "NagaProxy";

    private VJPlayer mPlayer;

    private boolean mIsWorking = false;

    private String mLocalUrl = "";
    private Object mLock = new Object();

    public NagaProxy() {
        mPlayer = new VJPlayer();
        mPlayer.setVJListener(this);
    }

    public String start(String url) {
        if (mPlayer == null) {
            throw new IllegalStateException("proxy is released");
        }

        if (mIsWorking) {
            Log.w(TAG, "proxy is working, stop first");

            mPlayer.stop();
            mIsWorking = false;
        }

        mPlayer.setURL(url);
        mPlayer.setVJMSBufferTimeout(10);

        mIsWorking = mPlayer.start();
        if (!mIsWorking) {
            Log.e(TAG, "proxy start fail");
            return "";
        }

        synchronized (mLock) {
            boolean isWaiting = true;

            do {
                try {
                    mLock.wait();
                    isWaiting = false;
                }
                catch (InterruptedException e) {
                    //ignore
                }
            }
            while (isWaiting);

            return mLocalUrl;
        }
    }

    public void stop() {
        if (mPlayer == null) {
            throw new IllegalStateException("proxy is released");
        }

        if (mIsWorking) {
            mPlayer.stop();
            mIsWorking = false;
        }
        else {
            Log.w(TAG, "proxy is not working");
        }
    }

    public void release() {
        if (mPlayer != null) {
            if (mIsWorking) {
                Log.w(TAG, "proxy is working, stop first");

                mPlayer.stop();
                mIsWorking = false;
            }

            mPlayer._release();
            mPlayer = null;
        }
        else {
            Log.w(TAG, "proxy is released");
        }
    }

    @Override
    public void onPlayURL(String url) {
        synchronized (mLock) {
            mLocalUrl = url;

            mLock.notify();
        }
    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "naga proxy notify error " + error);
    }
}
