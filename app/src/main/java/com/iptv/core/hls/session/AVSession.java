package com.iptv.core.hls.session;

import android.util.Log;

import com.iptv.core.hls.playlist.Playlist;
import com.iptv.core.utils.OkHttp;

import java.io.IOException;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public final class AVSession implements Session {
    private static final String TAG = "AVSession";

    public static final int STREAM_NONE = 0;
    public static final int STREAM_AUDIO = 1;
    public static final int STREAM_VIDEO = 2;

    private String mUrl;
    private Map<String, String> mProperties;

    private Playlist mPlaylist;

    private int mStreamMask = STREAM_AUDIO | STREAM_VIDEO;

    public AVSession(String url, Map<String, String> properties) {
        this(url, properties, null);
    }

    public AVSession(String url, Map<String, String> properties, Playlist playlist) {
        mUrl = url;
        mProperties = properties;

        if (playlist == null) {
            /**
             * url对应的播放列表还没有加载（MasterPlaylist）
             */
            loadPlaylist();
        }
        else {
            mPlaylist = playlist;
        }
    }

    protected void loadPlaylist() {
        Request request = OkHttp.createGetRequest(mUrl, mProperties);
        Response response = null;
        try {
            response = OkHttp.getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                /**
                 * 肯定是MediaPlaylist
                 */
                mPlaylist = Playlist.parse(response.body().string());
            }
            else {
                Log.e(TAG, "access " + mUrl + " fail, " + response.message());

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

    public void setStreamMask(int streamMask) {
        mStreamMask = streamMask;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
