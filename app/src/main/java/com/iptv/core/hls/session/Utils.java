package com.iptv.core.hls.session;

import android.util.Log;

import com.iptv.core.hls.playlist.Playlist;
import com.iptv.core.utils.IOUtils;
import com.iptv.core.utils.OkHttp;
import com.iptv.core.utils.StringOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Response;

final class Utils {
    private static final String TAG = "Utils";

    /**
     * 加载播放列表
     */
    public static Playlist loadPlaylist(String url, Map<String, String> properties) {
        Playlist playlist = null;

        Response response = null;
        try {
            response = OkHttp.get(url, properties);

            if (response.isSuccessful()) {
                InputStream input = response.body().byteStream();
                StringOutputStream output = new StringOutputStream();
                IOUtils.save(input, output);

                playlist = new Playlist(output.toString());
            }
            else {
                Log.e(TAG, "GET " + url + " fail, " + response.message());
            }
        }
        catch (IOException e) {
            Log.e(TAG, "read " + url + " error");
        }
        finally {
            /**
             * 释放网络连接
             */
            if (response != null) {
                response.close();
            }
        }

        return playlist;
    }

    /**
     * 生成url
     */
    public static String makeUrl(String playlistUrl, String uri) {
        String url;

        if (uri.startsWith("http://") || uri.startsWith("https://")) {
            /**
             * not relative uri
             */
            url = uri;
        }
        else {
            String relativePath = uri;

            if (relativePath.startsWith("/")) {
                /**
                 * 容错：相对路径的起始不应该是路径分隔符，删除
                 */
                relativePath = relativePath.substring(1);
            }

            url = getDomain(playlistUrl) + "/" + relativePath;
        }

        return url;
    }

    /**
     * 获取播放列表url的域
     */
    private static String getDomain(String playlistUrl) {
        /**
         * scheme://authority/path?query#fragment
         */
        int schemeEnd = playlistUrl.indexOf("://");
        if (schemeEnd < 0) {
            throw new IllegalStateException("must be a absolute uri");
        }

        int pathStart = playlistUrl.indexOf("/", schemeEnd + 3);
        if (pathStart < 0) {
            /**
             * 没有path部分
             */
            int queryStart = playlistUrl.indexOf("?", schemeEnd + 3);
            if (queryStart < 0) {
                /**
                 * 也没有query部分
                 */
                int fragmentStart = playlistUrl.indexOf("#", schemeEnd + 3);
                if (fragmentStart < 0) {
                    /**
                     * 还没有fragment部分
                     */
                    return playlistUrl;
                }
                else {
                    return playlistUrl.substring(0, fragmentStart);
                }
            }
            else {
                return playlistUrl.substring(0, queryStart);
            }
        }
        else {
            return playlistUrl.substring(0, pathStart);
        }
    }

    /**
     * 构造函数（私有属性，不允许创建实例）
     */
    private Utils() {
        /**
         * nothing
         */
    }
}
