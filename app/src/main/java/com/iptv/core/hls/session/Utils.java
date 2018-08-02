package com.iptv.core.hls.session;

public class Utils {
    /**
     * 如果uri是相对的，则基于masterUrl的主要部分组成一个url
     */
    public static String makeUrl(String masterUrl, String uri) {
        if (uri.startsWith("http://")
                || uri.startsWith("https://")) {
            /**
             * uri不是相对路径
             */
            return uri;
        }
        else {
            if (uri.startsWith("/")) {
                /**
                 * 相对路径的起始不应该是路径分隔符，删除
                 */
                uri = uri.substring(1);
            }

            /**
             * masterUrl只保留其中的基础部分（scheme://authority）
             */
            return getBasePart(masterUrl) + "/" + uri;
        }
    }

    /**
     * scheme://authority/path?query#fragment
     */
    private static String getBasePart(String url) {
        int schemeEnd = url.indexOf("://");

        int pathStart = url.indexOf("/", schemeEnd + 3);
        if (pathStart < 0) {
            /**
             * 没有path部分
             */
            int queryStart = url.indexOf("?", schemeEnd + 3);
            if (queryStart < 0) {
                /**
                 * 也没有query部分
                 */
                int fragmentStart = url.indexOf("#", schemeEnd + 3);
                if (fragmentStart < 0) {
                    /**
                     * 还没有fragment部分
                     */
                    return url;
                }
                else {
                    return url.substring(0, fragmentStart);
                }
            }
            else {
                return url.substring(0, queryStart);
            }
        }
        else {
            return url.substring(0, pathStart);
        }
    }
}
