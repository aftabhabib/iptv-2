package com.iptv.core.player.proxy;

import android.util.Log;

import com.nagasoft.player.VJListener;
import com.nagasoft.player.VJPlayer;

import java.io.IOException;

public class NagaProxy extends AbstractProxy {
    private static final String TAG = "NagaProxy";

    private VJPlayer mPlayer;

    public NagaProxy() {
        super();

        mPlayer = new VJPlayer();
        mPlayer.setVJListener(new VJPlayerListener());
        /**
         * FIXME：暂不清楚参数的单位，猜测是秒
         */
        mPlayer.setVJMSBufferTimeout(10);
    }

    @Override
    public void startService(String url) throws IOException {
        mPlayer.setURL(url);

        if (!mPlayer.start()) {
            throw new IOException("VJPlayer start fail");
        }

        /**
         * 阻塞，等待VJPlayer的异步通知
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
        mPlayer.stop();
    }

    private class VJPlayerListener implements VJListener {

        @Override
        public void onPlayURL(String url) {
            Log.d(TAG, "VJPlayer notify url " + url);

            onGetLocalUrl(url);
        }

        @Override
        public void onError(int error) {
            Log.e(TAG, "VJPlayer notify error " + error);
        }
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
