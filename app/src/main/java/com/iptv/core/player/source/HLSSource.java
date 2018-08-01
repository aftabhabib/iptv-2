package com.iptv.core.player.source;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.iptv.core.hls.playlist.Media;
import com.iptv.core.hls.playlist.Playlist;
import com.iptv.core.hls.playlist.VariantStream;
import com.iptv.core.hls.session.AVSession;
import com.iptv.core.hls.session.Session;
import com.iptv.core.hls.session.SubtitleSession;
import com.iptv.core.utils.OkHttp;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class HLSSource extends Handler {
    private static final String TAG = "HLSSource";

    private static final int MSG_LOAD_PLAYLIST = 0;

    private String mUrl;
    private Map<String, String> mProperties;

    private Playlist mMasterPlaylist;
    private int mBandwidth;

    private List<Session> mSessionList;

    public HLSSource(Looper looper, String url, Map<String, String> properties) {
        super(looper);

        mUrl = url;
        mProperties = properties;
    }

    public void load() {
        sendMessage(MSG_LOAD_PLAYLIST);
    }

    protected void sendMessage(int what) {
        sendMessage(what, null);
    }

    protected void sendMessage(int what, Object obj) {
        Message msg = obtainMessage();
        msg.what = what;
        msg.obj = obj;

        msg.sendToTarget();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_LOAD_PLAYLIST: {
                loadPlaylist();
                break;
            }
            default: {
                break;
            }
        }
    }

    protected void loadPlaylist() {
        Request request = OkHttp.createGetRequest(mUrl, mProperties);
        Response response = null;
        try {
            response = OkHttp.getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                Playlist playlist = Playlist.parse(response.body().string());
                if (playlist.containsVariantStream()) {
                    /**
                     * 播放列表中定义有一个或多个流，选择
                     */
                    mMasterPlaylist = playlist;
                    selectStream(0); //初始状态，带宽未知
                }
                else if (playlist.containsMediaSegment()) {
                    /**
                     * 播放列表中定义有媒体片段
                     */
                    AVSession session = new AVSession(mUrl, mProperties, playlist);
                    mSessionList.add(session);
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

    protected void selectStream(int bandwidth) {
        VariantStream stream = mMasterPlaylist.findStreamByBandwidth(bandwidth);

        if (mMasterPlaylist.containsRenditionGroup()) {
            /**
             * 播放列表中定义有RenditionGroup
             */
            selectRenditionOfStream(stream);
        }
        else {
            /**
             * 流的播放列表中定义了媒体片段
             */
            AVSession session = new AVSession(makeUrl(mUrl, stream.getUri()), mProperties);
            mSessionList.add(session);
        }
    }

    protected void selectRenditionOfStream(VariantStream stream) {
        int streamMask = AVSession.STREAM_NONE;

        if (stream.containsVideoRendition()) {
            /**
             * 流中定义有VideoRendition
             */
            Media rendition = mMasterPlaylist.getDefaultRenditionInGroup(stream.getVideoGroupId());
            if (rendition.containsUri()) {
                /**
                 * Rendition的播放列表中定义了媒体片段（纯视频流）
                 */
                AVSession session = new AVSession(makeUrl(mUrl, rendition.getUri()), mProperties);
                mSessionList.add(session);
            }
            else {
                Log.d(TAG, "media data for video rendition is included in the stream");
                streamMask |= AVSession.STREAM_VIDEO;
            }
        }
        else {
            if (stream.containsVideoCodec()) {
                /**
                 * 流中定义了VideoCodec
                 */
                streamMask |= AVSession.STREAM_VIDEO;
            }
        }

        if (stream.containsAudioRendition()) {
            /**
             * 流中定义有AudioRendition
             */
            Media rendition = mMasterPlaylist.getDefaultRenditionInGroup(stream.getAudioGroupId());
            if (rendition.containsUri()) {
                /**
                 * Rendition的播放列表中定义了媒体片段（纯音频流）
                 */
                AVSession session = new AVSession(makeUrl(mUrl, rendition.getUri()), mProperties);
                mSessionList.add(session);
            }
            else {
                Log.d(TAG, "media data for audio rendition is included in the stream");
                streamMask |= AVSession.STREAM_AUDIO;
            }
        }
        else {
            if (stream.containsAudioCodec()) {
                /**
                 * 流中定义了AudioCodec
                 */
                streamMask |= AVSession.STREAM_AUDIO;
            }
        }

        if (streamMask != AVSession.STREAM_NONE) {
            AVSession session = new AVSession(makeUrl(mUrl, stream.getUri()), mProperties);
            session.setStreamMask(streamMask);

            mSessionList.add(session);
        }

        /**
         * 字幕轨道一定是独立的，数据不会包含在流中
         */
        if (stream.containsSubtitleRendition()) {
            /**
             * 流中定义有SubtitleRendition
             */
            Media rendition = mMasterPlaylist.getDefaultRenditionInGroup(stream.getSubtitleGroupId());
            if (rendition.containsUri()) {
                SubtitleSession session = new SubtitleSession(makeUrl(mUrl, rendition.getUri()), mProperties);
                mSessionList.add(session);
            }
            else {
                Log.e(TAG,"uri is required if the type is subtitle");
            }
        }
    }

    private static String makeUrl(String baseUrl, String relativeUrl) {
        /**
         * TODO：待实现
         */

        return "";
    }
}
