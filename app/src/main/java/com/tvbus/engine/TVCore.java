package com.tvbus.engine;

import android.content.Context;

/**
 * TVBus SDK
 */
public class TVCore {
    private static final String TAG = "TVCore";

    static {
        System.loadLibrary("tvcore");
    }

    private long mNativeHandle;

    public TVCore() {
        mNativeHandle = initialise();
    }

    public int init(Context ctx) {
        return init(mNativeHandle, ctx);
    }

    public void quit() {
        quit(mNativeHandle);
    }

    public int run() {
        return run(mNativeHandle);
    }

    public void setPlayPort(int port) {
        setPlayPort(mNativeHandle, port);
    }

    public void setServPort(int port) {
        setServPort(mNativeHandle, port);
    }

    public void setTVListener(TVListener listener) {
        setListener(mNativeHandle, listener);
    }

    public void start(String url) {
        start(mNativeHandle, url);
    }

    public void start(String url, String accessCode) {
        start2(mNativeHandle, url, accessCode);
    }

    public void stop() {
        stop(mNativeHandle);
    }

    private native long initialise();

    private native int init(long handle, Context ctx);

    private native void quit(long handle);

    private native int run(long handle);

    private native void setListener(long handle, TVListener listener);

    private native void setPlayPort(long handle, int port);

    private native void setServPort(long handle, int port);

    private native void start(long handle, String url);

    private native void start2(long handle, String url, String accessCode);

    private native void stop(long handle);
}
