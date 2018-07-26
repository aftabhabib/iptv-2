package com.iptv.player.source;

import com.forcetech.android.ForceTV;

import java.io.IOException;

public class ForceP2PSource extends LocalServiceSource {
    private static final String TAG = "ForceP2PSource";

    private ForceTV mClient;
    private boolean mIsServiceRunning = false;

    public ForceP2PSource() {
        super();

        /**
         * FIXME：端口号、缓冲大小，参考GitHub上的代码
         */
        mClient = new ForceTV(9906, 20 * 1024 * 1024);
    }

    @Override
    protected void startService(String url) throws IOException {
        int ret = mClient.startP2PService();
        if (ret < 0) {
            throw new IOException("ForceP2P start service fail");
        }

        mClient.startChannel(url);
        mIsServiceRunning = true;

        mLocalUrl = mClient.getPlayUrl();
    }

    @Override
    protected void stopService() {
        if (mIsServiceRunning) {
            mClient.stopChannel();
            mClient.stopP2PService();

            mIsServiceRunning = false;
        }
    }
}
