package com.iptv.core.player.proxy;

import android.content.Context;
import android.util.Log;

import com.tvbus.engine.TVCore;
import com.tvbus.engine.TVListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

class TVBusProxy extends Proxy {
    private static final String TAG = "TVBusProxy";

    private TVCore mCore;

    private int mInitResult;
    private DriverThread mDriverThread;

    public TVBusProxy(Context context) {
        super();

        mInitResult = initTVCore(context);
    }

    private int initTVCore(Context context) {
        mCore = new TVCore();
        mCore.setTVListener(new TVCoreListener());
        /**
         * 端口号，参考GitHub上的代码
         */
        mCore.setServPort(8902);
        mCore.setPlayPort(4010);

        return mCore.init(context);
    }

    @Override
    public void startService(String url) throws IOException {
        if (mInitResult < 0) {
            throw new IOException("TVCore init fail");
        }

        mDriverThread = new DriverThread();
        mDriverThread.start();

        /**
         * 稍等，确保DriverThread已运行
         */
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            //ignore
        }

        /**
         * FIXME: 参数accessCode，从GitHub上的代码看应该是指访问资源时的“认证码”
         */
        mCore.start(url, "1111");

        /**
         * 阻塞，等待TVCore的异步通知
         */
        synchronized (this) {
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
        }
    }

    @Override
    public void stopService() {
        mCore.stop();

        mDriverThread.quitSafely();
    }

    private class DriverThread extends Thread {
        public DriverThread() {
            super("TVCore driver thread");
        }

        @Override
        public void	run() {
            /**
             * 从GitHub上的代码逻辑看，TVCore的run操作应该是阻塞的
             */
            mCore.run();
        }

        public void quitSafely() {
            /**
             * quit操作会解除run操作的阻塞，线程体结束
             */
            mCore.quit();

            boolean isTerminated = false;
            do {
                try {
                    join();
                    isTerminated = true;
                }
                catch (InterruptedException e) {
                    //ignore
                }
            }
            while (!isTerminated);
        }
    }

    private class TVCoreListener implements TVListener {

        @Override
        public void onInited(String result) {
            Log.d(TAG, "onInited callback " + result);
        }

        @Override
        public void onPrepared(String result) {
            Log.d(TAG, "onPrepared callback " + result);

            onGetLocalUrl(getPlayUrl(result));
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

    private static String getPlayUrl(String preparedResult) {
        String url = "";

        try {
            JSONObject rootObj = new JSONObject(preparedResult);
            url = rootObj.getString("hls");
        }
        catch (JSONException e) {
            Log.w(TAG, "parse prepared json error, " + e.getMessage());
        }

        return url;
    }

    private void onGetLocalUrl(String url) {
        mLocalUrl = url;

        /**
         * 得到本地代理地址，解除startService的阻塞
         */
        synchronized (this) {
            notify();
        }
    }
}
