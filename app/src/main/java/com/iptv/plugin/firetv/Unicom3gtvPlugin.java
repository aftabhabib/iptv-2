package com.iptv.plugin.firetv;

import android.util.Log;

import com.iptv.plugin.Plugin;
import com.utils.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Unicom3gtvPlugin implements Plugin {
    private static final String TAG = "Unicom3gtvPlugin";

    private static final String SCHEME = "gdlt://";

    private static final String UNICOM_3GTV_URL =
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
    public String process(String url, Map<String, String> property) {
        if (url.startsWith(SCHEME)) {
            String[] arrParameter = url.substring(SCHEME.length()).split("#");

            String channelId = arrParameter[0];
            String resIndex = arrParameter[1];

            return getPlayUrl(channelId, Integer.parseInt(resIndex), property);
        }
        else {
            throw new IllegalArgumentException("url is not unicom 3gtv item_source");
        }
    }

    private String getPlayUrl(String channelId, int resIndex, Map<String, String> property) {
        String url = "";

        byte[] content = HttpHelper.opGet(String.format(UNICOM_3GTV_URL, channelId), property);
        if (content == null) {
            Log.e(TAG, "get channel's json fail");
        }
        else {
            try {
                JSONObject rootObj = new JSONObject(new String(content));

                JSONArray resourceArray = rootObj.getJSONArray("resources");
                JSONObject resourceObj = resourceArray.getJSONObject(resIndex);

                url = resourceObj.getString("resUrl").trim();
            }
            catch (JSONException e) {
                Log.e(TAG, "parse channel's json fail, " + e.getMessage());
            }
        }

        return url;
    }
}
