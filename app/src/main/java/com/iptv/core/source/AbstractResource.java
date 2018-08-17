package com.iptv.core.source;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.iptv.core.channel.ChannelGroup;

import java.util.Map;

/**
 * 资源（虚基类）
 */
public abstract class AbstractResource implements Resource {
    private static final int MSG_LOAD_CHANNEL_TABLE = 0;
    private static final int MSG_DECODE_SOURCE = 1;

    private HandlerThread mDriverThread;
    private Handler mHandler;

    private Listener mListener;

    /**
     * 构造函数
     */
    public AbstractResource(Context context) {
        mDriverThread = new HandlerThread("resource driver thread");
        mDriverThread.start();

        mHandler = new Handler(mDriverThread.getLooper(), mHandlerCallback);
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void loadChannelTable() {
        sendMessage(MSG_LOAD_CHANNEL_TABLE);
    }

    @Override
    public void decodeSource(String source) {
        sendMessage(MSG_DECODE_SOURCE, source);
    }

    @Override
    public void release() {
        mDriverThread.quitSafely();
    }

    /**
     * 发送消息
     */
    private void sendMessage(int what) {
        mHandler.sendEmptyMessage(what);
    }

    /**
     * 发送消息
     */
    private void sendMessage(int what, Object obj) {
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        msg.obj = obj;

        mHandler.sendMessage(msg);
    }

    /**
     * 消息处理
     */
    private Handler.Callback mHandlerCallback = new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_CHANNEL_TABLE: {
                    onLoadChannelTable();

                    break;
                }
                case MSG_DECODE_SOURCE: {
                    String source = (String)msg.obj;
                    onDecodeSource(source);

                    break;
                }
                default: {
                    return false;
                }
            }

            return true;
        }
    };

    /**
     * 响应加载频道表
     */
    protected abstract void onLoadChannelTable();

    /**
     * 响应解码数据源
     */
    protected abstract void onDecodeSource(String source);

    /**
     * 通知出错
     */
    protected void notifyError(String error) {
        if (mListener != null) {
            mListener.onError(error);
        }
    }

    /**
     * 通知频道表
     */
    protected void notifyChannelTable(ChannelGroup[] groups) {
        if (mListener != null) {
            mListener.onLoadChannelTable(groups);
        }
    }

    /**
     * 通知数据源
     */
    protected void notifySource(String url, Map<String, String> properties) {
        if (mListener != null) {
            mListener.onDecodeSource(url, properties);
        }
    }
}
