package com.iptv.plugin.firetv;

import android.util.Log;

import com.iptv.plugin.Plugin;
import com.utils.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ZhcsAppPlugin implements Plugin {
    private static final String TAG = "ZhcsAppPlugin";

    private static final String SCHEME = "xhhncs://";

    private static final String ZHCS_APP_URL = "http://www.zhcsapp.cn:8686/zhcsserver/SearchPanelList.action?ad0101=%s";

    public ZhcsAppPlugin() {
        //
    }

    @Override
    public String getName() {
        return "智慧长沙";
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
            String ad0101 = url.substring(SCHEME.length());

            return getPlayUrl(ad0101, property);
        }
        else {
            throw new IllegalArgumentException("url is not zhcs app item_source");
        }
    }

    private String getPlayUrl(String ad0101, Map<String, String> property) {
        String url = "";

        byte[] content = HttpHelper.opGet(String.format(ZHCS_APP_URL, ad0101), property);
        if (content == null) {
            Log.e(TAG, "get channel's json fail");
        }
        else {
            try {
                JSONObject tmpObj = new JSONObject(new String(content));

                JSONArray rootArray = tmpObj.getJSONArray("root");
                JSONObject rootObj = rootArray.getJSONObject(0);

                url = rootObj.getString("ad0107").trim();
            }
            catch (JSONException e) {
                Log.e(TAG, "parse channel's json fail, " + e.getMessage());
            }
        }

        return url;
    }
}
