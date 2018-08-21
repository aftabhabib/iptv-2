package com.iptv.core.resource.firetv.plugin;

import com.iptv.core.resource.Plugin;
import com.iptv.core.resource.firetv.Authorization;

import java.util.Map;

public final class ChengboPlugin implements Plugin {
    private Authorization mAuth;

    /**
     * 构造函数
     */
    public ChengboPlugin(Authorization auth) {
        mAuth = auth;
    }

    @Override
    public String getName() {
        return "chengbo";
    }

    @Override
    public boolean isSupported(String url) {
        return url.startsWith("www.chengbo.org")
                || url.startsWith("wmv.chengbo.org");
    }

    @Override
    public String translate(String url) {
        if (!isSupported(url)) {
            throw new IllegalArgumentException("not supported url");
        }

        return "http://" + url + mAuth.generate();
    }

    @Override
    public Map<String, String> getProperties() {
        return null;
    }
}
