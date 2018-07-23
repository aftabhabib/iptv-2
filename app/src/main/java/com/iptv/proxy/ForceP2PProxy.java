package com.iptv.proxy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.forcetech.android.ForceTV;
import com.iptv.source.ProtocolType;

public class ForceP2PProxy extends Service {
    private static final String TAG = "ForceP2PProxy";

    private ForceTV mClient;
    private boolean mIsRunning = false;

    @Override
    public void onCreate() {
        mClient = new ForceTV(9999, 10 * 1024 * 1024);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        if (url == null || !ProtocolType.isForceP2P(url)) {
            Log.e(TAG, "not force p2p protocol");
        }
        else {
            int ret = mClient.startP2PService();
            if (ret < 0) {
                Log.e(TAG, "ForceTV start p2p service fail");
            }
            else {
                mClient.startChannel(url);
                mIsRunning = true;

                notifyPlayUrl();
            }
        }

        return START_NOT_STICKY;
    }

    private void notifyPlayUrl() {
        Intent intent = new Intent("com.iptv.demo.action.PLAY_URL");
        intent.putExtra("local_proxy_url", mClient.getPlayUrl());

        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mClient != null) {
            if (mIsRunning) {
                mClient.stopChannel();
                mClient.stopP2PService();
                mIsRunning = false;
            }

            mClient = null;
        }
    }
}
