package com.proxy;

import android.content.Context;

public class ProxyFactory {
    public static Proxy createProxy(Context ctx, String url) {
        if (url.startsWith("vjms://")) {
            return new NagaProxy();
        }
        else if (url.startsWith("tvbus://")) {
            return new TVBusProxy(ctx);
        }
        else if (url.startsWith("p2p://")) {
            return new ForceProxy();
        }
        else {
            return null;
        }
    }
}
