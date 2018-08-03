package com.iptv.core.hls.session;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.iptv.core.hls.playlist.Media;
import com.iptv.core.hls.playlist.Playlist;
import com.iptv.core.hls.playlist.VariantStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Session extends Handler implements PlaylistSource.Listener {
    private static final String TAG = "Session";

    private static final int MSG_START = 0;
    private static final int MSG_NOTIFY_MASTER_PLAYLIST = 10;
    private static final int MSG_NOTIFY_MEDIA_PLAYLIST = 11;
    private static final int MSG_NOTIFY_MEDIA_SEGMENT = 12;

    private String mMasterUrl;
    private Map<String, String> mProperties;

    private HandlerThread mSourceDriver;
    private List<PlaylistSource> mSourceList;

    private Playlist mMasterPlaylist;

    public Session(Looper looper) {
        super(looper);

        /**
         * 最多四个：一个MasterPlaylist，加上音频、视频、字幕（相互独立）的三个MediaPlaylist
         */
        mSourceList = new ArrayList<PlaylistSource>(4);
    }

    public void setDataSource(String url, Map<String, String> properties) {
        mMasterUrl = url;
        mProperties = properties;
    }

    public void start() {
        Message msg = obtainMessage();
        msg.what = MSG_START;

        msg.sendToTarget();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_START: {
                onStart();

                break;
            }
            case MSG_NOTIFY_MASTER_PLAYLIST: {
                Playlist playlist = (Playlist)msg.obj;
                onNotifyMasterPlaylist(playlist);

                break;
            }
            case MSG_NOTIFY_MEDIA_PLAYLIST: {
                PlaylistSource source = (PlaylistSource)msg.obj;
                onNotifyMediaPlaylist(source);

                break;
            }
            case MSG_NOTIFY_MEDIA_SEGMENT: {
                onNotifyMediaSegment(msg.arg1, msg.arg2);

                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onLoadMasterPlaylist(Playlist playlist) {
        Message msg = obtainMessage();
        msg.what = MSG_NOTIFY_MASTER_PLAYLIST;
        msg.obj = playlist;

        msg.sendToTarget();
    }

    @Override
    public void onLoadMediaPlaylist(PlaylistSource source) {
        Message msg = obtainMessage();
        msg.what = MSG_NOTIFY_MEDIA_PLAYLIST;
        msg.obj = source;

        msg.sendToTarget();
    }

    @Override
    public void onLoadMediaSegment(int size, int elapsedTime) {
        Message msg = obtainMessage();
        msg.what = MSG_NOTIFY_MEDIA_SEGMENT;
        msg.arg1 = size;
        msg.arg2 = elapsedTime;

        msg.sendToTarget();
    }

    private void onStart() {
        mSourceDriver = new HandlerThread("source driver thread");
        mSourceDriver.start();

        PlaylistSource source = new PlaylistSource(mSourceDriver.getLooper(), this,
                mMasterUrl, mProperties);
        source.loadPlaylist();

        mSourceList.add(source);
    }

    private void onNotifyMasterPlaylist(Playlist playlist) {
        mMasterPlaylist = playlist;

        /**
         * 初始状态，带宽未知
         */
        selectStream(0);
    }

    private void selectStream(int bandwidth) {
        VariantStream stream = mMasterPlaylist.findStreamByBandwidth(bandwidth);

        if (mMasterPlaylist.containsRenditionGroup()) {
            /**
             * 播放列表中定义有RenditionGroup
             */
            selectRenditionOfStream(stream);
        }
        else {
            PlaylistSource source = new PlaylistSource(mSourceDriver.getLooper(), this,
                    Utils.makeUrl(mMasterUrl, stream.getUri()), mProperties);
            source.loadPlaylist();

            mSourceList.add(source);
        }
    }

    private void selectRenditionOfStream(VariantStream stream) {
        int streamMask = 0;

        if (stream.containsVideoRendition()) {
            /**
             * 流中定义有VideoRendition
             */
            Media rendition = mMasterPlaylist.getDefaultRenditionInGroup(stream.getVideoGroupId());
            if (rendition.containsUri()) {
                /**
                 * Rendition的播放列表中定义了媒体片段（纯视频流）
                 */
                PlaylistSource source = new PlaylistSource(mSourceDriver.getLooper(), this,
                        Utils.makeUrl(mMasterUrl, rendition.getUri()), mProperties);
                source.loadPlaylist();

                mSourceList.add(source);
            }
            else {
                Log.d(TAG, "media data for video rendition is included in the stream");
                streamMask |= STREAM_VIDEO;
            }
        }
        else {
            if (stream.containsVideoCodec()) {
                /**
                 * 流中定义了VideoCodec
                 */
                streamMask |= STREAM_VIDEO;
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
                PlaylistSource source = new PlaylistSource(mSourceDriver.getLooper(), this,
                        Utils.makeUrl(mMasterUrl, rendition.getUri()), mProperties);
                source.loadPlaylist();

                mSourceList.add(source);
            }
            else {
                Log.d(TAG, "media data for audio rendition is included in the stream");
                streamMask |= STREAM_AUDIO;
            }
        }
        else {
            if (stream.containsAudioCodec()) {
                /**
                 * 流中定义了AudioCodec
                 */
                streamMask |= STREAM_AUDIO;
            }
        }

        if (stream.containsSubtitleRendition()) {
            /**
             * 流中定义有SubtitleRendition
             */
            Media rendition = mMasterPlaylist.getDefaultRenditionInGroup(stream.getSubtitleGroupId());
            if (rendition.containsUri()) {
                PlaylistSource source = new PlaylistSource(mSourceDriver.getLooper(), this,
                        Utils.makeUrl(mMasterUrl, rendition.getUri()), mProperties);
                source.loadPlaylist();

                mSourceList.add(source);
            }
            else {
                Log.e(TAG,"uri is required if the type is subtitle");
            }
        }
        else {
            /**
             * 字幕一定是独立的，不会在流中
             */
        }

        if (streamMask > 0) {
            PlaylistSource source = new PlaylistSource(mSourceDriver.getLooper(), this,
                    Utils.makeUrl(mMasterUrl, stream.getUri()), mProperties);
            source.loadPlaylist();

            mSourceList.add(source);
        }
    }

    private void onNotifyMediaPlaylist(PlaylistSource source) {
        source.loadMediaSegment();
    }

    private void onNotifyMediaSegment(int size, int elapsedTime) {
        /**
         * TODO：根据下载媒体片段的时间估算带宽
         */
    }
}
