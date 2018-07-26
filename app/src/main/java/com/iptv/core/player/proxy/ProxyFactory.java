package com.iptv.core.player.proxy;

import android.content.Context;

import com.iptv.core.utils.ProtocolType;

public class ProxyFactory {
    private Context mContext;

    public ProxyFactory(Context context) {
        mContext = context;
    }

    public Proxy create(String url) {
        if (ProtocolType.isNaga(url)) {
            return new NagaProxy();
        }
        else if (ProtocolType.isForceP2P(url)) {
            return new ForceP2PProxy();
        }
        else if (ProtocolType.isTVBus(url)) {
            return new TVBusProxy(mContext);
        }
        else {
            return null;
        }
    }
}
