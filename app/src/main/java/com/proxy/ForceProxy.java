package com.proxy;

import android.util.Log;

import com.forcetech.android.ForceTV;

class ForceProxy implements Proxy {
    private static final String TAG = "ForceProxy";

    private ForceTV mClient;

    private boolean mIsServiceRunning = false;
    private boolean mIsWorking = false;

    public ForceProxy() {
        mClient = new ForceTV(9999, 10 * 1024 * 1024);

        int ret = mClient.startP2PService();
        if (ret == 0) {
            mIsServiceRunning = true;
        }
    }

    public String start(String url) {
        if (mClient == null) {
            throw new IllegalStateException("proxy is released");
        }

        if (!mIsServiceRunning) {
            throw new IllegalStateException("proxy service is not running");
        }

        if (mIsWorking) {
            Log.w(TAG, "proxy is working, stop first");

            mClient.stopChannel();
            mIsWorking = false;
        }

        mClient.startChannel(url);
        mIsWorking = true;

        return mClient.getPlayUrl();
    }

    public void stop() {
        if (mClient == null) {
            throw new IllegalStateException("proxy is released");
        }

        if (mIsWorking) {
            mClient.stopChannel();
            mIsWorking = false;
        }
        else {
            Log.w(TAG, "proxy is not working");
        }
    }

    public void release() {
        if (mClient != null) {
            if (mIsWorking) {
                Log.w(TAG, "proxy is working, stop first");

                mClient.stopChannel();
                mIsWorking = false;
            }

            if (mIsServiceRunning) {
                mClient.stopP2PService();
                mIsServiceRunning = false;
            }

            mClient = null;
        }
        else {
            Log.w(TAG, "proxy is released");
        }
    }
}
