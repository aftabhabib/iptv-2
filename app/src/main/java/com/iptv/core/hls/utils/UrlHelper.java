package com.iptv.core.hls.utils;

/**
 * URL辅助
 */
public final class UrlHelper {
    /**
     * 生成url
     */
    public static String makeUrl(String baseUri, String uri) {
        String url;

        if (uri.startsWith("http://") || uri.startsWith("https://")) {
            /**
             * not relative uri
             */
            url = uri;
        }
        else {
            url = baseUri + "/" + uri;
        }

        return url;
    }

    /**
     * 获取播放列表url的基础uri
     */
    public static String getBaseUri(String playlistUrl) {
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
    private UrlHelper() {
        /**
         * nothing
         */
    }
}
