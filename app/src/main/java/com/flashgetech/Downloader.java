package com.flashgetech;

public class Downloader {
    public static final int DE_SUCCESS = 0x0;
    public static final int DE_ALREADY_INITED = -0x1;
    public static final int DE_UNINITED = -0x2;
    public static final int DE_URL_INVALID = -0x3;
    public static final int DE_FOLDER_INVALID = -0x4;
    public static final int DE_DOWNLOADID_INVALID = -0x5;
    public static final int DE_TASKINFO_INVALID = -0x6;
    public static final int DE_FILENAME_INVALID = -0x7;
    public static final int DE_AUTHENTICATE_FAILED = -0x8;
    public static final int DE_PARAM_INVALID = -0x9;
    public static final int DE_LIVE_GETCHANNEL_ERROR = -0xa;

    public static final int TE_CREATEFILE = 0x1;
    public static final int TE_WRITEFILE = 0x2;
    public static final int TE_READFILE = 0x3;
    public static final int TE_NOTENOUGHSPACE = 0x4;
    public static final int TE_NORESOURCE = 0x5;

    public static final int TS_INIT = 0x0;
    public static final int TS_RUNNING = 0x1;
    public static final int TS_WAIT = 0x2;
    public static final int TS_STOP = 0x3;
    public static final int TS_COMPLETE = 0x4;
    public static final int TS_FAILED = 0x5;

    static {
        System.loadLibrary("fgdownloader");
    }

    public static native int Uninit();

    public native String LiveGetLocalProxyUrl(String url);
}
