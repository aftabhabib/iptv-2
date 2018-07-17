package com.forcetech.android;

import android.net.Uri;

class ChannelInfo {
    private String mServer;
    private String mId;
    private String mPath;

    private ChannelInfo(String server, String id, String path) {
        mServer = server;
        mId = id;
        mPath = path;
    }

    public String getServer() {
        return mServer;
    }

    public String getId() {
        return mId;
    }

    public String getPath() {
        return mPath;
    }

    public static ChannelInfo parse(String url) {
        Uri uri = Uri.parse(url);

        return new ChannelInfo(uri.getHost() + ":" + uri.getPort(),
                parseId(uri.getLastPathSegment()), uri.getPath());
    }

    private static String parseId(String segment) {
        int index = segment.lastIndexOf('.');
        if (index > 0) {
            return segment.substring(0, index);
        }

        return segment;
    }
}
