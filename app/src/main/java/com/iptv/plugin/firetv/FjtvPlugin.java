package com.iptv.plugin.firetv;

import android.util.Log;

import com.iptv.plugin.Plugin;
import com.iptv.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class FjtvPlugin implements Plugin {
    private static final String TAG = "FjtvPlugin";

    private static final String SCHEME = "fjtv://";

    private static final String FJTV_URL_FORMULAR =
            "http://mobile.fjtv.net/haibo/channel_detail.php?appid=9&appkey=OU4VuJgmGkqFzelCaueFLHll1sZJpOG4&client_id_android=b17049e927554e29a2860236864e6cb6&device_token=347e2ef9eb2eabaeba84cf3d31b18381&_member_id=&version=2.0.5&app_version=2.0.5&package_name=com.hoge.android.app.fujian&system_version=5.1&phone_models=OPPOR9m&channel_id=%s";

    public FjtvPlugin() {
        //
    }

    @Override
    public String getName() {
        return "福建TV";
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

        byte[] content = HttpHelper.opGet(String.format(FJTV_URL_FORMULAR, channelId), property);
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
