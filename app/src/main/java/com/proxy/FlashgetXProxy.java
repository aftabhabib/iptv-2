package com.proxy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.flashgetech.Downloader;
import com.iptv.source.ProtocolType;

public class FlashgetXProxy extends Service {
    private static final String TAG = "FlashgetXProxy";

    private Downloader mDownloader;

    @Override
    public void onCreate() {
        mDownloader = new Downloader();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        if (url == null || !ProtocolType.isFlashgetX(url)) {
            Log.e(TAG, "not flashgetx protocol");
        }
        else {
            String localProxyUrl = mDownloader.LiveGetLocalProxyUrl(url);
            notifyPlayUrl(localProxyUrl);
        }

        return START_NOT_STICKY;
    }

    private void notifyPlayUrl(String url) {
        Intent intent = new Intent("com.iptv.demo.action.PLAY_URL");
        intent.putExtra("local_proxy_url", url);

        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mDownloader != null) {
            Downloader.Uninit();
            mDownloader = null;
        }
    }
}
