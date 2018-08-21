package com.iptv.core.resource.firetv;

import android.util.Log;

import com.iptv.core.utils.OkHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Response;

public final class Authorization {
    private static final String TAG = "Authorization";

    private String mYzKey;

    /**
     * 构造函数
     */
    public Authorization(String yzKey) {
        mYzKey = yzKey;
    }

    /**
     * 生成身份认证参数
     */
    public String generate() {
        String ret = "";

        Key key = new Key();
        if (fetchKey(key)) {
            try {
                JSONObject rootObj = key.parse();
                String ip = rootObj.getString("ip");
                String tm = rootObj.getString("tm");

                ret = makeQuery(tm, md5(makeKey(ip, tm)));
            }
            catch (JSONException e) {
                Log.e(TAG, "get ip and tm fail");
            }
            catch (NoSuchAlgorithmException e) {
                Log.e(TAG, "md5 fail");
            }
        }

        return ret;
    }

    /**
     * 获取Key
     */
    private boolean fetchKey(Key key) {
        boolean ret = false;

        Response response = null;
        try {
            response = OkHttp.get(ServerConfig.getKeyUrl(), null);

            if (response.isSuccessful()) {
                ret = key.write(response.body().byteStream());
            } else {
                Log.e(TAG, "GET key fail, " + response.message());
            }
        }
        catch (IOException e) {
            Log.e(TAG, "read key error");
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

    /**
     * 生成key
     */
    private String makeKey(String ip, String tm) {
        StringBuffer strBuffer = new StringBuffer();

        strBuffer.append("91jssbadaojibae888fe89bcda3d44596c7076f9188nnsb");
        strBuffer.append(ip);
        strBuffer.append("c4d890e1c48142305f8b5b5c3df97be5");
        strBuffer.append(tm);
        strBuffer.append("0420e7a5a4e838ea051adcbf97ec9e7");
        strBuffer.append(mYzKey);

        return strBuffer.toString();
    }

    /**
     * key的MD5摘要
     */
    private static String md5(String key) throws NoSuchAlgorithmException {
        StringBuffer strBuffer = new StringBuffer();

        byte[] digestData = MessageDigest.getInstance("MD5").digest(key.getBytes());
        for (int i = 0; i < digestData.length; i++) {
            int value = digestData[i] & 0xff;

            if (value < 0x10) {
                strBuffer.append("0");
            }

            strBuffer.append(Integer.toHexString(value));
        }

        return strBuffer.toString();
    }

    /**
     * 生成url的query部分
     */
    private static String makeQuery(String tm, String keyDigest) {
        StringBuffer strBuffer = new StringBuffer();

        strBuffer.append("&tm=");
        strBuffer.append(tm);
        strBuffer.append("&key=");
        strBuffer.append(keyDigest);

        return strBuffer.toString();
    }
}
