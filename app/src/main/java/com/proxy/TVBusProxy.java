package com.proxy;

import android.content.Context;
import android.util.Log;

import com.tvbus.engine.TVCore;
import com.tvbus.engine.TVListener;

import org.json.JSONException;
import org.json.JSONObject;

class TVBusProxy implements Proxy, TVListener {
    private static final String TAG = "TVBusProxy";

    private TVCore mCore;

    private boolean mIsServiceRunning = false;
    private boolean mIsWorking= false;

    private String mLocalUrl = "";
    private Object mLock = new Object();

    public TVBusProxy(Context ctx) {
        mCore = new TVCore();
        mCore.setTVListener(this);
        mCore.setServPort(8902);
        mCore.setPlayPort(4010);

        int ret = mCore.init(ctx);
        if (ret == 0) {
            ret = mCore.run();
            if (ret == 0) {
                mIsServiceRunning = true;
            }
            else {
                Log.e(TAG, "run fail");
            }
        }
        else {
            Log.e(TAG, "init fail");
        }
    }

    public String start(String url) {
        if (mCore == null) {
            throw new IllegalStateException("proxy is released");
        }

        if (!mIsServiceRunning) {
            throw new IllegalStateException("proxy service is not running");
        }

        if (mIsWorking) {
            Log.w(TAG, "proxy is working, stop first");

            mCore.stop();
            mIsWorking = false;
        }

        /**
         * FIXME: 这里的accessCode用处不明
         */
        mCore.start(url, "1111");
        mIsWorking = true;

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
        if (mCore == null) {
            throw new IllegalStateException("proxy is released");
        }

        if (mIsWorking) {
            mCore.stop();
            mIsWorking = false;
        }
        else {
            Log.w(TAG, "proxy is not working");
        }
    }

    public void release() {
        if (mCore != null) {
            if (mIsWorking) {
                Log.w(TAG, "proxy is working, stop first");

                mCore.stop();
                mIsWorking = false;
            }

            if (mIsServiceRunning) {
                mCore.quit();
                mIsServiceRunning = false;
            }

            mCore = null;
        }
        else {
            Log.w(TAG, "proxy is released");
        }
    }

    @Override
    public void onInited(String result) {
        Log.d(TAG, "onInited callback " + result);
    }

    @Override
    public void onPrepared(String result) {
        Log.d(TAG, "onPrepared callback " + result);

        synchronized (mLock) {
            try {
                JSONObject rootObj = new JSONObject(result);

                mLocalUrl = rootObj.getString("hls");
            }
            catch (JSONException e) {
                Log.w(TAG, "parse json error, " + e.getMessage());
            }

            mLock.notify();
        }
    }

    @Override
    public void onInfo(String result) {
        Log.d(TAG, "onInfo callback " + result);
    }

    @Override
    public void onStart(String result) {
        Log.d(TAG, "onStart callback " + result);
    }

    @Override
    public void onStop(String result) {
        Log.d(TAG, "onStop callback " + result);
    }

    @Override
    public void onQuit(String result) {
        Log.d(TAG, "onQuit callback " + result);
    }
}
