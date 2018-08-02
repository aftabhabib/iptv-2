package com.iptv.core.hls.session;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.iptv.core.hls.playlist.Playlist;
import com.iptv.core.utils.OkHttp;

import java.io.IOException;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public final class PlaylistSource extends Handler {
    private static final String TAG = "PlaylistSource";

    private static final int MSG_LOAD_PLAYLIST = 0;
    private static final int MSG_LOAD_MEDIASEGMENT = 1;

    private String mUrl;
    private Map<String, String> mProperties;

    private Playlist mPlaylist;

    public PlaylistSource(Looper looper, String url, Map<String, String> properties) {
        super(looper);

        mUrl = url;
        mProperties = properties;
    }

    public void setPlaylist(Playlist playlist) {
        mPlaylist = playlist;
    }

    public void start() {
        if (mPlaylist == null) {
            /**
             * 加载Stream或Media的Playlist
             */
            sendMessage(MSG_LOAD_PLAYLIST);
        }
        else {
            sendMessage(MSG_LOAD_MEDIASEGMENT);
        }
    }

    public void stop() {
        removeCallbacksAndMessages(null);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_LOAD_PLAYLIST: {
                loadPlaylist();
                break;
            }
            case MSG_LOAD_MEDIASEGMENT: {
                break;
            }
            default: {
                break;
            }
        }
    }

    private void sendMessage(int what) {
        sendMessage(what, null);
    }

    private void sendMessage(int what, Object obj) {
        Message msg = obtainMessage();
        msg.what = what;
        msg.obj = obj;

        msg.sendToTarget();
    }

    private void loadPlaylist() {
        Request request = OkHttp.createGetRequest(mUrl, mProperties);
        Response response = null;
        try {
            response = OkHttp.getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                /**
                 * 肯定是MediaPlaylist
                 */
                mPlaylist = Playlist.parse(response.body().string());

                loadMediaSegment();
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

    private void loadMediaSegment() {

    }
}
