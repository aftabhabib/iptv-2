package com.iptv.core.resource;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.iptv.core.channel.ChannelGroup;

import java.io.File;
import java.util.Map;

/**
 * 资源（虚基类）
 */
public abstract class AbstractResource implements Resource {
    private static final int MSG_LOAD_CHANNEL_TABLE = 0;
    private static final int MSG_DECODE_SOURCE = 1;

    private File mFilesDir;
    private SharedPreferences mSharedPreferences;

    private HandlerThread mDriverThread;
    private Handler mHandler;

    private Listener mListener = null;

    /**
     * 构造函数
     */
    public AbstractResource(Context context) {
        mFilesDir = context.getDir(getName(), 0);
        mSharedPreferences = context.getSharedPreferences(getName(), 0);

        mDriverThread = new HandlerThread("resource driver thread");
        mDriverThread.start();

        mHandler = new Handler(mDriverThread.getLooper(), mHandlerCallback);
    }

    /**
     * 获取名称
     */
    protected abstract String getName();

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
     * 获取档案目录
     */
    protected File getFilesDir() {
        return mFilesDir;
    }

    /**
     * 是否包含首选项
     */
    protected boolean containsPreference(String key) {
        return mSharedPreferences.contains(key);
    }

    /**
     * 获取首选项
     */
    protected String getPreference(String key) {
        return mSharedPreferences.getString(key, "");
    }

    /**
     * 新增（或变更）首选项
     */
    protected void putPreference(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

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
