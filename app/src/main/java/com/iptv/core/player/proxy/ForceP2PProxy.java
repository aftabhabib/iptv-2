package com.iptv.core.player.proxy;

import com.forcetech.android.ForceTV;

import java.io.IOException;

class ForceP2PProxy extends Proxy {
    private static final String TAG = "ForceP2PProxy";

    private ForceTV mClient;

    public ForceP2PProxy() {
        super();

        /**
         * FIXME：端口号、缓冲大小，参考GitHub上的代码
         */
        mClient = new ForceTV(9906, 20 * 1024 * 1024);
    }

    @Override
    public void startService(String url) throws IOException {
        int ret = mClient.startP2PService();
        if (ret < 0) {
            throw new IOException("ForceP2P start service fail");
        }

        /**
         * TODO：从GitHub上的代码看，似乎要确保服务已运行
         */

        mClient.startChannel(url);

        mLocalUrl = mClient.getPlayUrl();
    }

    @Override
    public void stopService() {
        mClient.stopChannel();
        mClient.stopP2PService();
    }
}
