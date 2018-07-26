package com.iptv.source.firetv.plugin;

import android.util.Log;

import com.iptv.source.Plugin;
import com.iptv.source.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SctvPlugin implements Plugin {
    private static final String TAG = "SctvPlugin";

    private static final String SCHEME = "sctv://";

    private static final String SCTV_URL_FORMULAR = "http://tvdi.sctv.com:18091/xmsjt/client/getChannelById?channelid=%s";

    public SctvPlugin() {
        //
    }

    @Override
    public String getName() {
        return "四川TV";
    }

    @Override
    public boolean isSupported(String url) {
        if (url.startsWith(SCHEME)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String decode(String url) {
        if (url.startsWith(SCHEME)) {
            String channelId = url.substring(SCHEME.length());

            return getPlayUrl(channelId, null);
        }
        else {
            throw new IllegalArgumentException("invalid url");
        }
    }

    private String getPlayUrl(String channelId, Map<String, String> property) {
        String url = "";

        byte[] content = HttpHelper.opGet(String.format(SCTV_URL_FORMULAR, channelId), property);
        if (content == null) {
            Log.e(TAG, "get json fail");
        }
        else {
            try {
                JSONObject rootObj = new JSONObject(new String(content));

                /**
                 * TODO: 验证失败
                 */
            }
            catch (JSONException e) {
                Log.e(TAG, "parse json fail, " + e.getMessage());
            }
        }

        return url;
    }
}
