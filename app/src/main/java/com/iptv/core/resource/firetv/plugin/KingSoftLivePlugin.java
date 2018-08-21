package com.iptv.core.resource.firetv.plugin;

import android.util.Log;

import com.iptv.core.resource.Plugin;
import com.iptv.core.resource.firetv.Authorization;
import com.iptv.core.utils.IOUtils;
import com.iptv.core.utils.OkHttp;
import com.iptv.core.utils.StringOutputStream;
import com.iptv.core.utils.UserAgent;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

public final class KingSoftLivePlugin implements Plugin {
    private static final String TAG = "KingSoftLivePlugin";

    private Authorization mAuth;

    /**
     * 构造函数
     */
    public KingSoftLivePlugin(Authorization auth) {
        mAuth = auth;
    }

    @Override
    public String getName() {
        return "KingSoft Live";
    }

    @Override
    public boolean isSupported(String url) {
        return url.startsWith("kslive1://");
    }

    @Override
    public String translate(String url) {
        if (!isSupported(url)) {
            throw new IllegalArgumentException("not supported url");
        }

        url = url.replace("kslive1://", "") + mAuth.generate();

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("User-Agent", UserAgent.UA_WINDOWS);

        return getPlayUrl(url, properties);
    }

    @Override
    public Map<String, String> getProperties() {
        return null;
    }

    /**
     * 获取播放地址
     */
    private String getPlayUrl(String url, Map<String, String> properties) {
        String ret = "";

        Response response = null;
        try {
            response = OkHttp.get(url, properties);

            if (response.isSuccessful()) {
                InputStream input = response.body().byteStream();
                StringOutputStream output = new StringOutputStream();
                IOUtils.save(input, output);

                ret = output.toString();
            }
            else {
                Log.e(TAG, "GET " + url + " fail, " + response.message());
            }
        }
        catch (IOException e) {
            Log.e(TAG, "read " + url + " error");
        }
        finally {
            /**
             * 释放网络连接
             */
            if (response != null) {
                response.close();
            }
        }

        return ret;
    }
}
