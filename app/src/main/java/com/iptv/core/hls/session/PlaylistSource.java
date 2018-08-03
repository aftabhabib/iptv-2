package com.iptv.core.hls.session;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.iptv.core.hls.playlist.MediaSegment;
import com.iptv.core.hls.playlist.Playlist;
import com.iptv.core.utils.OkHttp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

final class PlaylistSource extends Handler {
    private static final String TAG = "PlaylistSource";

    private static final int MSG_LOAD_PLAYLIST = 0;
    private static final int MSG_LOAD_MEDIA_SEGMENT = 1;

    private Listener mListener;

    private String mUrl;
    private Map<String, String> mProperties;

    private Playlist mPlaylist;
    private Map<String, byte[]> mAESKeyCache;

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

    public void loadMediaSegment() {
        Message msg = obtainMessage();
        msg.what = MSG_LOAD_MEDIA_SEGMENT;

        msg.sendToTarget();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_LOAD_PLAYLIST: {
                onLoadPlaylist();

                break;
            }
            case MSG_LOAD_MEDIA_SEGMENT: {
                onLoadMediaSegment();

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

    private void onLoadMediaSegment() {
        MediaSegment segment = mPlaylist.removeMediaSegment();

        Map<String, String> properties;
        if (segment.containsRange()) {
            properties = new HashMap<String, String>(mProperties);
            properties.put("Range", segment.getRangeValue());
        }
        else {
            properties = mProperties;
        }

        byte[] data = download(Utils.makeUrl(mUrl, segment.getUri()), properties);
        if (data == null) {
            /**
             * FIXME：下载失败
             */
        }
        else {
            if (segment.isEncrypted()) {
                /**
                 * MediaSegment是加密的
                 */
                if (!segment.isMediaSampleEncrypted()) {
                    /**
                     * 全加密
                     */
                }
                else {
                    /**
                     * TODO：MediaSample加密，暂不支持
                     */
                }
            }

            /**
             * Extractor
             */
        }
    }

    private byte[] decryptSegment(byte[] encryptedData, MediaSegment segment) {
        if (mAESKeyCache == null) {
            mAESKeyCache = new HashMap<String, byte[]>();
        }

        byte[] key;
        if (!mAESKeyCache.containsKey(segment.getKeyUri())) {
            /**
             * download key and cache
             */
            key = download(Utils.makeUrl(mUrl, segment.getKeyUri()), mProperties);
            if (key == null) {
                /**
                 * FIXME：下载失败
                 */
            }
            else {
                mAESKeyCache.put(segment.getKeyUri(), key);
            }
        }
        else {
            key = mAESKeyCache.get(segment.getKeyUri());
        }

        if (!segment.isMediaSampleEncrypted()) {
            /**
             * MediaSegment是AES-128加密
             */
            ByteArrayInputStream input = new ByteArrayInputStream(encryptedData);

            InputStream decryptInput = new AESDecryptInputStream(input, key, segment.getKeyInitVector());
        }
        else {
            /**
             * TODO：MediaSample是AES-128加密
             */
        }
    }

    private static byte[] download(String url, Map<String, String> properties) {
        byte[] data = null;

        Request request = OkHttp.createGetRequest(url, properties);
        Response response = null;
        try {
            response = OkHttp.getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();

                InputStream input = response.body().byteStream();
                save(input, output);
                input.close();

                data = output.toByteArray();
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

        return data;
    }

    private static void save(InputStream input, OutputStream output) throws IOException {
        byte[] buf = new byte[1024];

        while (true) {
            int bytesRead = input.read(buf);
            if (bytesRead == -1) {
                break;
            }

            output.write(buf, 0, bytesRead);
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
