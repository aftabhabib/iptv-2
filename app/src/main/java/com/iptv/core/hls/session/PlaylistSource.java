package com.iptv.core.hls.session;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.iptv.core.hls.playlist.MediaSegment;
import com.iptv.core.hls.playlist.Playlist;
import com.iptv.core.utils.OkHttp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

final class PlaylistSource extends Handler {
    private static final String TAG = "PlaylistSource";

    private static final int MSG_LOAD_PLAYLIST = 0;
    private static final int MSG_DOWNLOAD_MEDIASEGMENT = 1;

    private Listener mListener;

    private String mUrl;
    private Map<String, String> mProperties;

    private Playlist mPlaylist;

    public PlaylistSource(Looper looper, Listener listener,
                          String url, Map<String, String> properties) {
        super(looper);

        mListener = listener;

        mUrl = url;
        mProperties = properties;
    }

    public void loadPlaylist() {
        Message msg = obtainMessage();
        msg.what = MSG_LOAD_PLAYLIST;

        msg.sendToTarget();
    }

    public void downloadMediaSegment() {
        Message msg = obtainMessage();
        msg.what = MSG_DOWNLOAD_MEDIASEGMENT;

        msg.sendToTarget();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_LOAD_PLAYLIST: {
                onLoadPlaylist();

                break;
            }
            case MSG_DOWNLOAD_MEDIASEGMENT: {
                onDownloadMediaSegment();

                break;
            }
            default: {
                break;
            }
        }
    }

    private void onLoadPlaylist() {
        Request request = OkHttp.createGetRequest(mUrl, mProperties);
        Response response = null;
        try {
            response = OkHttp.getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Playlist playlist = Playlist.parse(response.body().string());
                if (playlist.containsVariantStream()) {
                    /**
                     * MasterPlaylist
                     */
                    mListener.onLoadMasterPlaylist(playlist);
                }
                else if (playlist.containsMediaSegment()) {
                    /**
                     * MediaPlaylist
                     */
                    mPlaylist = playlist;
                    mListener.onLoadMediaPlaylist(this);
                }
                else {
                    Log.e(TAG, "playlist is not supported");
                }
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

    private void onDownloadMediaSegment() {
        MediaSegment segment = mPlaylist.removeMediaSegment();

        Request request = OkHttp.createGetRequest(mUrl, mProperties);
        Response response = null;
        try {
            response = OkHttp.getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream input = response.body().byteStream();

                /**
                 *
                 */


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

    public interface Listener {
        /**
         * 加载了MasterPlaylist
         */
        void onLoadMasterPlaylist(Playlist playlist);

        /**
         * 加载了MediaPlaylist
         */
        void onLoadMediaPlaylist(PlaylistSource source);

        /**
         * 加载了MediaSegment
         */
        void onLoadMediaSegment(int size, int elapsedTime);
    }
}
