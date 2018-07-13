package com.forcetech.android;

import android.net.Uri;

class ForceChannel {
    public static ForceChannel parse(String url) {
        Uri uri = Uri.parse(url);

        if (!uri.getScheme().equalsIgnoreCase("p2p")) {
            throw new AssertionError("invalid forcetv url");
        }

        return new ForceChannel(uri.getHost() + ":" + uri.getPort(),
                parseId(uri.getLastPathSegment()), uri.getPath());
    }

    private String mServer;
    private String mId;
    private String mPath;

    private ForceChannel(String server, String id, String path) {
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

    private static String parseId(String segment) {
        int index = segment.lastIndexOf('.');
        if (index > 0) {
            return segment.substring(0, index);
        }

        return segment;
    }
}
