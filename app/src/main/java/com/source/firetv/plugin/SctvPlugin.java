package com.source.firetv.plugin;

import android.util.Log;

import com.source.firetv.Plugin;
import com.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SctvPlugin implements Plugin {
    private static final String TAG = "SctvPlugin";

    private static final String SCHEME = "sctv://";

    private static final String SCTV_URL = "http://tvdi.sctv.com:18091/xmsjt/client/getChannelById?channelid=%s";

    public SctvPlugin() {
        //ignore
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
    public String process(String url, Map<String, String> property) {
        if (url.startsWith(SCHEME)) {
            String channelId = url.substring(SCHEME.length());

            return getPlayUrl(channelId, property);
        }
        else {
            throw new IllegalArgumentException("url is not sctv source");
        }
    }

    private String getPlayUrl(String channelId, Map<String, String> property) {
        String url = "";

        byte[] content = HttpHelper.opGet(String.format(SCTV_URL, channelId), property);
        if (content == null) {
            Log.e(TAG, "get channel's json fail");
        }
        else {
            try {
                JSONObject rootObj = new JSONObject(new String(content));

                /**
                 * TODO: 验证失败
                 */
            }
            catch (JSONException e) {
                Log.e(TAG, "parse channel's json fail, " + e.getMessage());
            }
        }

        return url;
    }
}
