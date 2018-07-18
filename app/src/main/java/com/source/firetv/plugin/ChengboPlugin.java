package com.source.firetv.plugin;

import android.util.Log;

import com.source.firetv.Plugin;
import com.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class ChengboPlugin implements Plugin {
    private static final String TAG = "ChengboPlugin";

    private static final String SCHEME = "kslive1://";
    private static final String HOST1 = "www.chengbo.org";
    private static final String HOST2 = "wmv.chengbo.org";
    private static final String DOUBLE_AT = "@@";

    private static final String CHENGBO_KEY_URL = "http://www.chengbo.org/soft/key.php";

    private String mYzKey;

    public ChengboPlugin(String yzKey) {
        mYzKey = yzKey;
    }

    @Override
    public String getName() {
        return "chengbo";
    }

    @Override
    public boolean isSupported(String url) {
        if (url.startsWith(SCHEME)
                || url.startsWith(HOST1)
                || url.startsWith(HOST2)
                || url.startsWith(DOUBLE_AT)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String process(String url, Map<String, String> property) {
        if (url.startsWith(SCHEME)) {
            String queryParameters = getQueryParameters();
            if (queryParameters.isEmpty()) {
                return "";
            }

            return url.substring(SCHEME.length()) + queryParameters;
        }
        else if (url.startsWith(HOST1) || url.startsWith(HOST2)) {
            String queryParameters = getQueryParameters();
            if (queryParameters.isEmpty()) {
                return "";
            }

            return "http://" + url + queryParameters;
        }
        else if (url.startsWith(DOUBLE_AT)) {
            String queryParameters = getQueryParameters();
            if (queryParameters.isEmpty()) {
                return "";
            }

            String tmpUrl = "http://" + url.substring(DOUBLE_AT.length()) + queryParameters;
            return getPlayUrl(tmpUrl, property) + DOUBLE_AT;
        }
        else {
            throw new IllegalArgumentException("url is not chengbo item_source");
        }
    }

    private String getQueryParameters() {
        String parameters = "";

        byte[] content = HttpHelper.opGet(CHENGBO_KEY_URL, null);
        if (content == null) {
            Log.e(TAG, "get json fail");
        }
        else {
            try {
                JSONObject rootObj = new JSONObject(new String(content));

                String ip = rootObj.getString("ip");
                String tm = rootObj.getString("tm");

                StringBuffer keyBuffer = new StringBuffer();
                keyBuffer.append("91jssbadaojibae888fe89bcda3d44596c7076f9188nnsb");
                keyBuffer.append(ip);
                keyBuffer.append("c4d890e1c48142305f8b5b5c3df97be5");
                keyBuffer.append(tm);
                keyBuffer.append("0420e7a5a4e838ea051adcbf97ec9e7");
                keyBuffer.append(mYzKey);

                byte[] arrDigest = MessageDigest.getInstance("MD5")
                        .digest(keyBuffer.toString().getBytes());
                /**
                 * FIXME：转换逻辑有问题，但逆向出来的原始代码就是这样
                 */
                StringBuffer hexBuffer = new StringBuffer();
                for (int i = 0; i < arrDigest.length; i++) {
                    int value = arrDigest[i] & 0xff;
                    if (value >= 0x10) {
                        hexBuffer.append(Integer.toHexString(value));
                    }
                    else {
                        hexBuffer.append("0");
                    }
                }

                StringBuffer parameterBuffer = new StringBuffer();
                parameterBuffer.append("&tm=");
                parameterBuffer.append(tm);
                parameterBuffer.append("&key=");
                parameterBuffer.append(hexBuffer.toString());

                parameters = parameterBuffer.toString();
            }
            catch (JSONException e) {
                Log.e(TAG, "parse json fail, " + e.getMessage());
            }
            catch (NoSuchAlgorithmException e) {
                //ignore
            }
        }

        return parameters;
    }

    private String getPlayUrl(String url, Map<String, String> property) {
        String playUrl = "";

        byte[] content = HttpHelper.opGet(url, property);
        if (content == null) {
            Log.e(TAG, "get play url fail");
        }
        else {
            playUrl = new String(content).trim();
        }

        return playUrl;
    }
}
