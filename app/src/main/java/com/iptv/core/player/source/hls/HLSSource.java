package com.iptv.core.player.source.hls;

import android.net.Uri;
import android.util.Log;

import com.iptv.core.player.source.Source;
import com.iptv.core.player.source.hls.playlist.Playlist;
import com.iptv.core.player.source.hls.playlist.VariantStream;
import com.iptv.core.utils.OkHttp;

import java.io.IOException;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class HLSSource implements Source {
    private static final String TAG = "HLSSource";

    private Playlist mMediaPlaylist;

    public HLSSource() {
        //ignore
    }

    @Override
    public boolean connect(String url, Map<String, String> property) throws IOException {
        boolean ret = false;

        Request request = OkHttp.createGetRequest(url, property);
        Response response = OkHttp.getClient().newCall(request).execute();
        if (response.isSuccessful()) {
            String content = response.body().string();

            Playlist playlist = Playlist.parse(content);
            if (playlist.isVariantPlaylist()) {
                /**
                 * Playlist是MasterPlaylist，选流（此时带宽未知）
                 */
                VariantStream stream = playlist.findStreamByBandwidth(0);

                /**
                 * 获取指定流的Playlist
                 */
                Uri playlistUri = stream.getUri();
                if (playlistUri.isRelative()) {
                    playlistUri = Uri.withAppendedPath(Uri.parse(url), playlistUri.getPath());
                }

                request = OkHttp.createGetRequest(playlistUri.toString(), null);
                response = OkHttp.getClient().newCall(request).execute();
                if (response.isSuccessful()) {
                    content = response.body().string();

                    /**
                     * Playlist肯定是MediaPlaylist
                     */
                    mMediaPlaylist = Playlist.parse(content);
                }
                else {
                    Log.e(TAG, "connect fail, " + response.message());

                    response.close();
                }
            }
            else {
                /**
                 * Playlist是MediaPlaylist
                 */
                mMediaPlaylist = playlist;
            }
        }
        else {
            Log.e(TAG, "connect fail, " + response.message());

            response.close();
        }

        return ret;
    }

    @Override
    public String getMIMEType() {
        return null;
    }

    @Override
    public int read(byte[] buffer, int offset, int size) throws IOException {
        return 0;
    }

    @Override
    public long skip(long size) throws IOException {
        return 0;
    }

    @Override
    public void disconnect() {

    }
}
