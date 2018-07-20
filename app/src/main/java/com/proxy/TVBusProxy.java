package com.proxy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.iptv.source.ProtocolType;
import com.tvbus.engine.TVCore;
import com.tvbus.engine.TVListener;

import org.json.JSONException;
import org.json.JSONObject;

public class TVBusProxy extends Service implements TVListener {
    private static final String TAG = "TVBusProxy";

    private TVCore mCore;
    private boolean mIsRunning = false;

    @Override
    public void onCreate() {
        mCore = new TVCore();
        mCore.setTVListener(this);
        mCore.setServPort(8902);
        mCore.setPlayPort(4010);

        int ret = mCore.init(this);
        if (ret < 0) {
            Log.e(TAG, "TVCore init fail");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        if (url == null || !ProtocolType.isTVBus(url)) {
            Log.e(TAG, "not tvbus protocol");
        }
        else {
            int ret = mCore.run();
            if (ret < 0) {
                Log.e(TAG, "TVCore run fail");
            }
            else {
                /**
                 * FIXME: 这里的accessCode用处不明
                 */
                mCore.start(url, "1111");
                mIsRunning = true;
            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mCore != null) {
            if (mIsRunning) {
                mCore.stop();
                mCore.quit();
                mIsRunning = false;
            }

            mCore = null;
        }
    }

    @Override
    public void onInited(String result) {
        Log.d(TAG, "onInited callback " + result);
    }

    @Override
    public void onPrepared(String result) {
        Log.d(TAG, "onPrepared callback " + result);

        String url = getPlayUrl(result);
        if (!url.isEmpty()) {
            Intent intent = new Intent("com.iptv.demo.action.PLAY_URL");
            intent.putExtra("local_proxy_url", url);

            sendBroadcast(intent);
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
