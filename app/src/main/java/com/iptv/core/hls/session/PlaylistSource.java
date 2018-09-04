package com.iptv.core.hls.session;

import android.util.Log;

import com.iptv.core.hls.playlist.Playlist;
import com.iptv.core.hls.playlist.Segment;
import com.iptv.core.player.extractor.MPEG2TSExtractor;
import com.iptv.core.player.source.HttpSource;

import javax.crypto.Cipher;

/**
 * 播放列表描述的媒体源
 */
final class PlaylistSource {
    private static final String TAG = "PlaylistSource";

    /**
     * 掩码
     */
    public static final int STREAM_AUDIO = 1;
    public static final int STREAM_VIDEO = 1 << 1;
    public static final int STREAM_SUBTITLE = 1 << 2;

    /**
     * 消息类型
     */
    private static final int MSG_LOAD_PLAYLIST = 0;
    private static final int MSG_LOAD_SEGMENT = 1;
    private static final int MSG_RELOAD_PLAYLIST = 2;

    private int mStreamMask;

    private String mUrl = null;
    private Playlist mPlaylist = null;

    private Segment[] mSegments = null;
    private int mPlayIndex = -1;

    private AESCipher mAESCipher = null;
    private MPEG2TSExtractor mExtractor = null;

    /**
     * 构造函数
     */
    public PlaylistSource(int streamMask) {
        mStreamMask = streamMask;
    }

    /**
     * 开始
     */
    public void start(String url, Playlist playlist) {
        mUrl = url;
        sendMessage(MSG_LOAD_PLAYLIST, playlist);
    }

    /**
     * 停止
     */
    public void stop() {

    }

    /**
     * 发送消息
     */
    private void sendMessage(int what) {
        sendMessage(what, null);
    }

    /**
     * 发送消息
     */
    private void sendMessage(int what, Object obj) {
        sendMessage(what, obj, 0);
    }

    /**
     * 发送消息
     */
    private void sendMessage(int what, Object obj, long delay) {
        /**
         *
         */
    }

    /**
     * 加载播放列表
     */
    private void onLoadPlaylist(Playlist playlist) {
        if (playlist == null) {
            mPlaylist = Utils.loadPlaylist(mUrl, null);
            if (mPlaylist == null) {
                Log.e(TAG, "load playlist fail");
                return;
            }
        }
        else {
            /**
             * 外部传入了播放列表，避免二次加载
             */
            mPlaylist = playlist;
        }

        mSegments = mPlaylist.getSegments();

        if (!mPlaylist.endOfList()) {
            /**
             * starts less than three target durations from the end of the Playlist file
             */
            int duration = 0;
            for (int i = mSegments.length - 1; i >= 0; i--) {
                duration += mSegments[i].getDuration();

                if (duration >= 3 * mPlaylist.getTargetDuration()) {
                    mPlayIndex = i + 1;
                    break;
                }
            }

            sendMessage(MSG_RELOAD_PLAYLIST, mPlaylist.getTargetDuration());
        }
        else {
            mPlayIndex = 0;
        }

        sendMessage(MSG_LOAD_SEGMENT);
    }

    /**
     * 响应加载片段
     */
    private void onLoadSegment() {
        if (mPlayIndex == mSegments.length) {
            Log.w(TAG, "no more segments, retry later");
            sendMessage(MSG_LOAD_SEGMENT, 1000);
            return;
        }

        Segment segment = mSegments[mPlayIndex++];
        extractSegment(segment);

        /**
         *
         */
    }

    /**
     * 解析片段
     */
    private void extractSegment(Segment segment) {
        HttpSource source = new HttpSource(Utils.makeUrl(mUrl, segment.getUri()), null);

        if (segment.isEncrypted()) {
            String url = Utils.makeUrl(mUrl, segment.getKeyUri());
            byte[] iv = segment.getKeyInitVector();

            if (mAESCipher == null) {
                mAESCipher = new AESCipher(url, iv);
            }
            else {
                mAESCipher.update(url, iv);
            }

            Cipher cipher = mAESCipher.getCipher();
            if (cipher == null) {
                Log.w(TAG, "get segment decrypt cipher fail");
                return;
            }

            source.setDecryptCipher(cipher);
        }

        if (!source.connect()) {
            /**
             * skip, try next
             */
            return;
        }

        if (mExtractor == null) {
            mExtractor = new MPEG2TSExtractor();
        }
        mExtractor.setDataSource(source);
    }

    /**
     * 响应重新加载播放列表
     */
    private void onReloadPlaylist() {
        Playlist playlist = Utils.loadPlaylist(mUrl, null);
        if (playlist == null) {
            Log.e(TAG, "reload playlist fail");
            return;
        }

        if (playlist.hashCode() == mPlaylist.hashCode()) {
            /**
             * playlist not update yet, retry
             */
            sendMessage(MSG_RELOAD_PLAYLIST, mPlaylist.getTargetDuration() / 2);
        }
        else {
            /**
             *
             */

            if (!mPlaylist.endOfList()) {
                sendMessage(MSG_RELOAD_PLAYLIST, mPlaylist.getTargetDuration());
            }
        }
    }
}
