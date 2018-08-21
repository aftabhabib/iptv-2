package com.iptv.core.resource;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.iptv.core.channel.ChannelGroup;

import java.io.File;
import java.util.Map;

/**
 * 资源（虚基类）
 */
public abstract class AbstractResource extends Handler implements Resource {
    private File mFilesDir;
    private SharedPreferences mSharedPreferences;

    private Listener mListener = null;

    /**
     * 构造函数
     */
    public AbstractResource(Looper looper, Context context) {
        super(looper);

        mFilesDir = context.getDir(getName(), 0);
        mSharedPreferences = context.getSharedPreferences(getName(), 0);
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    /**
     * 获取名称
     */
    protected abstract String getName();

    /**
     * 发送消息
     */
    protected void sendMessage(int what) {
        sendEmptyMessage(what);
    }

    /**
     * 发送消息
     */
    protected void sendMessage(int what, Object obj) {
        Message msg = obtainMessage();
        msg.what = what;
        msg.obj = obj;

        sendMessage(msg);
    }

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
    protected void notifyDataSource(String url, Map<String, String> properties) {
        if (mListener != null) {
            mListener.onDecodeUrl(url, properties);
        }
    }
}
