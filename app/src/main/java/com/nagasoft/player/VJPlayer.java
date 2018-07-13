package com.nagasoft.player;

import java.lang.ref.WeakReference;

/**
 * 纳加SDK
 */
public class VJPlayer {
    private static final String TAG = "VJPlayer";

    static {
        System.loadLibrary("p2pcore");
        System.loadLibrary("vjplayer_jni");

        native_init();
    }

    private int mNativeListener = 0;
    private int mNativePlayer = 0;

    private VJListener mListener = null;

    /**
     * 构造函数
     */
    public VJPlayer() {
        native_setup(new WeakReference(this));
    }

    private static final native void native_init();

    /**
     * 释放
     */
    public native void _release();

    public native long getPlayBackDuration();

    public native boolean isLiveStream();

    public native boolean isPlayBackStream();

    public native boolean isVodFile();

    public final native void native_finalize();

    private final native void native_setup(Object wRef);

    public native boolean seekPlayBack(long pos);

    public native void setURL(String url);

    public native void setVJMSBufferTimeout(int size);

    public native boolean start();

    public native void stop();

    public void setVJListener(VJListener listener) {
        mListener = listener;
    }

    private void notifyPlayURL(String url) {
        if (mListener != null) {
            mListener.onPlayURL(url);
        }
    }

    private void notifyError(int error) {
        if (mListener != null) {
            mListener.onError(error);
        }
    }
}
