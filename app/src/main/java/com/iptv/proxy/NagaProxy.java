package com.iptv.proxy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.nagasoft.player.VJListener;
import com.nagasoft.player.VJPlayer;
import com.iptv.source.ProtocolType;

public class NagaProxy extends Service implements VJListener {
    private static final String TAG = "NagaProxy";

    private VJPlayer mPlayer;
    private boolean mIsRunning = false;

    @Override
    public void onCreate() {
        mPlayer = new VJPlayer();
        mPlayer.setVJListener(this);
        mPlayer.setVJMSBufferTimeout(10);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        if (url == null || !ProtocolType.isNaga(url)) {
            Log.e(TAG, "not naga protocol");
        }
        else {
            mPlayer.setURL(url);

            mIsRunning = mPlayer.start();
            if (!mIsRunning) {
                Log.e(TAG, "VJPlayer start fail");
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
        if (mPlayer != null) {
            if (mIsRunning) {
                mPlayer.stop();
                mIsRunning = false;
            }

            mPlayer._release();
            mPlayer = null;
        }
    }

    @Override
    public void onPlayURL(String url) {
        Log.d(TAG, "VJPlayer notify url " + url);

        Intent intent = new Intent("com.iptv.demo.action.PLAY_URL");
        intent.putExtra("local_proxy_url", url);

        sendBroadcast(intent);
    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "VJPlayer notify error " + error);

        /**
         * FIXME: notify player
         */
    }
}
