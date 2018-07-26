package com.iptv.player.mediasource;

import android.annotation.TargetApi;
import android.media.MediaDataSource;
import android.os.Build;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@TargetApi(Build.VERSION_CODES.M)
public abstract class LocalServiceSource extends MediaDataSource {
    protected String mLocalUrl = null;

    private HttpURLConnection mConnection = null;
    private long mContentLength = -1;
    private String mContentType = "";

    private long mCurrPosition = 0;
    private InputStream mContentInput = null;

    public void connect(String url) throws IOException {
        startService(url);

        connectService();
    }

    public String getMime() {
        return mContentType;
    }

    @Override
    public long getSize() throws IOException {
        return mContentLength;
    }

    @Override
    public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
        if (position != mCurrPosition) {
            seekTo(position);
            mCurrPosition = position;
        }

        int ret = mContentInput.read(buffer, offset, size);
        if (ret > 0) {
            mCurrPosition += ret;
        }

        return ret;
    }

    @Override
    public void close() throws IOException {
        disconnectService();

        mContentLength = -1;
        mCurrPosition = 0;

        stopService();
    }

    protected abstract void startService(String url) throws IOException;

    protected abstract void stopService();

    protected void connectService() throws IOException {
        connectService(0);
    }

    protected void connectService(long position) throws IOException {
        URL url = new URL(mLocalUrl);
        mConnection = (HttpURLConnection)url.openConnection();

        if (position > 0) {
            mConnection.setRequestProperty("Range", "bytes=" + position + "-");
        }

        int responseCode = mConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            /**
             * 保存媒体文件的大小和类型
             */
            mContentType = mConnection.getContentType();
            mContentLength = mConnection.getContentLength();

            mContentInput = mConnection.getInputStream();
        }
        else if (responseCode == HttpURLConnection.HTTP_PARTIAL) {
            mContentInput = mConnection.getInputStream();
        }
        else {
            throw new IOException("connect service fail, " + mConnection.getResponseMessage());
        }
    }

    protected void seekTo(long position) throws IOException {
        disconnectService();

        connectService(position);
    }

    protected void disconnectService() {
        if (mConnection != null) {
            mContentInput = null;

            mConnection.disconnect();
            mConnection = null;
        }
    }
}
