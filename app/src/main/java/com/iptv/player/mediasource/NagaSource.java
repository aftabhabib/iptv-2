package com.iptv.player.mediasource;

import android.annotation.TargetApi;
import android.media.MediaDataSource;
import android.os.Build;
import android.util.Log;

import com.nagasoft.player.VJListener;
import com.nagasoft.player.VJPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@TargetApi(Build.VERSION_CODES.M)
public class NagaSource extends MediaDataSource implements VJListener {
    private static final String TAG = "NagaSource";

    private VJPlayer mPlayer;
    private boolean mIsServiceRunning = false;
    private URL mLocalUrl = null;

    private HttpURLConnection mConnection = null;
    private long mContentLength = -1;
    private long mCurrPosition = 0;
    private InputStream mContentInput = null;

    public NagaSource() {
        mPlayer = new VJPlayer();
        mPlayer.setVJListener(this);
        /**
         * FIXME: 不清楚参数的单位，暂时认为是秒
         */
        mPlayer.setVJMSBufferTimeout(10);
    }

    public boolean connect(String url) throws IOException {
        mPlayer.setURL(url);

        mIsServiceRunning = mPlayer.start();
        if (!mIsServiceRunning) {
            Log.e(TAG, "VJPlayer start fail");
            return false;
        }

        /**
         * wait VJPlayer notify play url
         */
        boolean isConnected = false;
        do {
            try {
                wait();
                isConnected = true;
            }
            catch (InterruptedException e) {
                //ignore
            }
        }
        while (!isConnected);

        /**
         * connect to service
         */
        return connectService();
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
    public long getSize() throws IOException {
        return mContentLength;
    }

    @Override
    public void close() throws IOException {
        disconnect();
        mContentLength = -1;
        mCurrPosition = 0;

        if (mIsServiceRunning) {
            mPlayer.stop();
            mIsServiceRunning = false;
        }
    }

    @Override
    public void onPlayURL(String url) {
        Log.d(TAG, "VJPlayer notify url " + url);

        try {
            mLocalUrl = new URL(url);
        }
        catch (MalformedURLException e) {
            //ignore
        }

        synchronized (this) {
            notify();
        }
    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "VJPlayer notify error " + error);
    }

    private boolean connectService() throws IOException {
        mConnection = (HttpURLConnection)mLocalUrl.openConnection();

        mConnection.connect();

        int responseCode = mConnection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            Log.e(TAG, "connect fail, response code = " + responseCode);
            return false;
        }

        mContentLength = mConnection.getContentLength();
        mContentInput = mConnection.getInputStream();

        return true;
    }

    private void seekTo(long position) throws IOException {
        disconnect();

        mConnection = (HttpURLConnection)mLocalUrl.openConnection();
        mConnection.setRequestProperty("Range", "bytes=" + position + "-");

        mConnection.connect();

        int responseCode = mConnection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_PARTIAL) {
            throw new IOException("range request fail, response code = " + responseCode);
        }

        mContentInput = mConnection.getInputStream();
    }

    private void disconnect() {
        if (mConnection != null) {
            mContentInput = null;

            mConnection.disconnect();
            mConnection = null;
        }
    }
}
