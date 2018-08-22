package com.iptv.core.player.source;

import android.util.Log;

import com.iptv.core.player.DataSource;
import com.iptv.core.utils.OkHttp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Response;

public class HttpSource implements DataSource {
    private static final String TAG = "HttpSource";

    private String mUrl;
    private Map<String, String> mProperties;

    private long mContentSize = -1;
    private InputStream mContentInput = null;

    /**
     * 构造函数
     */
    public HttpSource(String url, Map<String, String> properties) {
        mUrl = url;
        mProperties = properties;
    }

    /**
     * 连接
     */
    public boolean connect() {
        Response response = null;

        try {
            response = OkHttp.get(mUrl, mProperties);

            if (response.isSuccessful()) {
                String contentLength = response.header("Content-Length", "-1");
                mContentSize = Long.parseLong(contentLength);

                mContentInput = response.body().byteStream();
            }
            else {
                Log.e(TAG, "connect fail, " + response.message());
            }
        }
        catch (IOException e) {
            Log.e(TAG, "connect error");
        }
        finally {
            if (mContentInput == null) {
                response.close();
            }
        }

        return mContentInput != null;
    }

    /**
     * 获取资源大小
     */
    public long getSize() {
        return mContentSize;
    }

    @Override
    public int read(byte[] buffer, int offset, int size) throws IOException {
        if (mContentInput == null) {
            throw new IllegalStateException("not connected");
        }

        return mContentInput.read(buffer, offset, size);
    }

    @Override
    public void release() {
        if (mContentInput != null) {
            disconnect();
        }
    }

    /**
     * 断开
     */
    protected void disconnect() {
        try {
            mContentInput.close();
        }
        catch (IOException e) {
            /**
             * ignore
             */
        }
    }
}
