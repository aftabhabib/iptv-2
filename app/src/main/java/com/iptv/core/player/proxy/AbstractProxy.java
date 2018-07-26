package com.iptv.core.player.proxy;

import java.io.IOException;

/**
 * 纳加、TVBus和原力P2P等第三方协议根据其功能视为一种代理
 */
abstract class AbstractProxy {
    protected String mLocalUrl = null;

    public abstract void startService(String url) throws IOException;

    public String getLocalUrl() {
        return mLocalUrl;
    }

    public abstract void stopService();
}
