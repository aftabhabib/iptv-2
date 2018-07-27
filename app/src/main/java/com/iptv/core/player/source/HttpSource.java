package com.iptv.core.player.source;

import android.util.Log;

import com.iptv.core.utils.OkHttp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class HttpSource implements Source {
    private static final String TAG = "HttpSource";

    private String mContentType = "";
    private InputStream mContentInput = null;

    public HttpSource() {
        //ignore
    }

    @Override
    public boolean connect(String url, Map<String, String> property) throws IOException {
        boolean ret = false;

        Request request = OkHttp.createGetRequest(url, property);
        Response response = OkHttp.getClient().newCall(request).execute();
        if (response.isSuccessful()) {
            mContentType = response.header("Content-Type");
            mContentInput = response.body().byteStream();

            ret = true;
        }
        else {
            Log.e(TAG, "connect fail, " + response.message());

            response.close();
        }

        return ret;
    }

    @Override
    public String getMIMEType() {
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
        try {
            mContentInput.close();
        }
        catch (IOException e) {
            //ignore
        }
    }
}
