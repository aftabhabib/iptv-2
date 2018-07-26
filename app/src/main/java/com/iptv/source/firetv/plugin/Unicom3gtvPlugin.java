package com.iptv.source.firetv.plugin;

import android.util.Log;

import com.iptv.source.Plugin;
import com.iptv.source.utils.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Unicom3gtvPlugin implements Plugin {
    private static final String TAG = "Unicom3gtvPlugin";

    private static final String SCHEME = "gdlt://";

    private static final String UNICOM_3GTV_URL_FORMULAR =
            "http://u1.3gtv.net:2080/pms-service/broadcast/broadcast_detail?contentType=4&sectionId=-1&portalId=43&id=%s";

    public Unicom3gtvPlugin() {
        //
    }

    @Override
    public String getName() {
        return "联通悦TV";
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
            String[] arrParameter = url.substring(SCHEME.length()).split("#");

            String channelId = arrParameter[0];
            String resIndex = arrParameter[1];

            return getPlayUrl(channelId, Integer.parseInt(resIndex), null);
        }
        else {
            throw new IllegalArgumentException("invalid url");
        }
    }

    private String getPlayUrl(String channelId, int resIndex, Map<String, String> property) {
        String url = "";

        byte[] content = HttpHelper.opGet(String.format(UNICOM_3GTV_URL_FORMULAR, channelId), property);
        if (content == null) {
            Log.e(TAG, "get json fail");
        }
        else {
            try {
                JSONObject rootObj = new JSONObject(new String(content));

                JSONArray resourceArray = rootObj.getJSONArray("resources");
                JSONObject resourceObj = resourceArray.getJSONObject(resIndex);

                url = resourceObj.getString("resUrl").trim();
            }
            catch (JSONException e) {
                Log.e(TAG, "parse json fail, " + e.getMessage());
            }
        }

        return url;
    }
}
