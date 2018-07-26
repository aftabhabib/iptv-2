package com.iptv.core.player.source.proxy;

import com.iptv.core.player.source.Source;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 纳加、TVBus和原力P2P等第三方协议的共同特征
 */
abstract class ProxySource implements Source {
    protected String mLocalUrl = null;

    private HttpURLConnection mConnection = null;
    private String mContentType = "";
    private BufferedInputStream mContentInput = null;

    @Override
    public void connect(String url) throws IOException {
        startService(url);

        connectProxy();
    }

    @Override
    public String getMime() {
        return mContentType;
    }

    @Override
    public int read(byte[] buffer, int offset, int size) throws IOException {
        return mContentInput.read(buffer, offset, size);
    }

    @Override
    public long skip(long size) throws IOException {
        return mContentInput.skip(size);
    }

    @Override
    public void disconnect() {
        disconnectProxy();

        stopService();
    }

    protected abstract void startService(String url) throws IOException;

    protected abstract void stopService();

    protected void connectProxy() throws IOException {
        URL url = new URL(mLocalUrl);
        mConnection = (HttpURLConnection)url.openConnection();

        int responseCode = mConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            /**
             * 保存媒体文件的类型
             */
            mContentType = mConnection.getContentType();

            mContentInput = new BufferedInputStream(mConnection.getInputStream());
        }
        else {
            throw new IOException("connect proxy fail, " + mConnection.getResponseMessage());
        }
    }

    protected void disconnectProxy() {
        if (mConnection != null) {
            mContentInput = null;

            mConnection.disconnect();
            mConnection = null;
        }
    }
}
