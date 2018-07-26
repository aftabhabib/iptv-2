package com.iptv.core.source.firetv.plugin;

import android.util.Log;

import com.iptv.core.source.Plugin;
import com.iptv.core.source.utils.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SzyxPlugin implements Plugin {
    private static final String TAG = "SzyxPlugin";

    private static final String SCHEME = "szyx://";

    private static final String SZYX_URL_FORMULAR =
            "http://122.193.8.99:8090/cms/thirdPartyPortalInterface/play.service?clientId=26941&channelId=%s&busiType=live&definition=sd&clientip=192.168.0.104&resFormat=json&terminalType=mobile&assetId=%s";

    public SzyxPlugin() {
        //
    }

    @Override
    public String getName() {
        return "";
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

        byte[] content = HttpHelper.opGet(String.format(SZYX_URL_FORMULAR, channelId, channelId), property);
        if (content == null) {
            Log.e(TAG, "get json fail");
        }
        else {
            try {
                JSONObject rootObj = new JSONObject(new String(content));

                JSONObject responseObj = rootObj.getJSONObject("playResponse");
                JSONObject contentsObj = responseObj.getJSONObject("contentList");

                JSONArray contentArray = contentsObj.getJSONArray("content");
                JSONObject contentObj = contentArray.getJSONObject(0);

                url = contentObj.getString("url").trim();
            }
            catch (JSONException e) {
                Log.e(TAG, "parse json fail, " + e.getMessage());
            }
        }

        return url;
    }
}
