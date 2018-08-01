package com.iptv.core.player.hls;

import android.util.Log;

import com.iptv.core.player.source.Source;
import com.iptv.core.player.hls.playlist.Media;
import com.iptv.core.player.hls.playlist.Playlist;
import com.iptv.core.player.hls.playlist.VariantStream;
import com.iptv.core.utils.OkHttp;

import java.io.IOException;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class HLSSource implements Source {
    private static final String TAG = "HLSSource";

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

            /**
             * FIXME：使用Handler发送播放列表加载完毕消息
             */
            Playlist playlist = Playlist.parse(content);

            ret = true;
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

    protected void handlePlaylistLoaded(Playlist playlist) {
        /**
         * 根据播放列表的情况分别处理
         */
        if (playlist.containsVariantStream()) {
            /**
             * 播放列表中定义有多个流，选择一个流（带宽未知）
             */
            VariantStream stream = playlist.findStreamByBandwidth(0);

            if (playlist.containsRenditionGroup()) {
                /**
                 * 播放列表中定义了RenditionGroup
                 */
                Media videoRendition = null;
                Media audioRendition = null;
                Media subtitleRendition = null;

                if (stream.containsVideoRendition()) {
                    /**
                     * 流中定义了VideoRendition（例如：不同视角的camera）
                     */
                    videoRendition = playlist.getDefaultRenditionInGroup(stream.getVideoGroupId());
                }

                if (stream.containsAudioRendition()) {
                    /**
                     * 流中定义了AudioRendition（例如：不同语言的配音）
                     */
                    audioRendition = playlist.getDefaultRenditionInGroup(stream.getAudioGroupId());
                }

                if (stream.containsSubtitleRendition()) {
                    /**
                     * 流中定义了SubtitleRendition（例如：不同语言的字幕）
                     */
                    subtitleRendition = playlist.getDefaultRenditionInGroup(stream.getSubtitleGroupId());
                }

                /**
                 * TODO: 复杂，还没完全理解
                 */
            }
            else {
                /**
                 * TODO: 获取流的播放列表
                 */
            }
        }
        else if (playlist.containsMediaSegment()) {
            /**
             * 播放列表中定义有媒体片段（最普通，最简单的情况），直接取媒体数据即可
             */

            /**
             * TODO: 直接取流
             */
        }
        else {
            Log.e(TAG, "playlist not supported yet");
        }
    }
}
