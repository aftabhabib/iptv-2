package com.iptv.core.hls.session;

import android.util.Log;

import com.iptv.core.utils.OkHttp;
import com.iptv.core.webvtt.Webvtt;

import java.io.IOException;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public final class SubtitleSession implements Session {
    private static final String TAG = "SubtitleSession";

    private Webvtt mSubtitle;

    public SubtitleSession(String url, Map<String, String> properties) {
        loadSubtitle(url, properties);
    }

    protected void loadSubtitle(String url, Map<String, String> properties) {
        Request request = OkHttp.createGetRequest(url, properties);
        Response response = null;
        try {
            response = OkHttp.getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                /**
                 * 字幕一定是Webvtt格式
                 */
                mSubtitle = Webvtt.parse(response.body().string());
            }
            else {
                Log.e(TAG, "access " + url + " fail, " + response.message());

                response.close();
            }
        }
        catch (IOException e) {
            Log.e(TAG, "network problems, " + e.getMessage());

            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
