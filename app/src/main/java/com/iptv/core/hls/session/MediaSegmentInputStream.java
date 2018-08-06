package com.iptv.core.hls.session;

import com.iptv.core.utils.OkHttp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

final class MediaSegmentInputStream extends InputStream {
    private InputStream mInput;

    public MediaSegmentInputStream(String url, Map<String, String> properties) throws IOException {
        connect(url, properties);
    }

    private void connect(String url, Map<String, String> properties) throws IOException {
        Request request = OkHttp.createGetRequest(url, properties);

        Response response = OkHttp.getClient().newCall(request).execute();
        if (response.isSuccessful()) {
            mInput = response.body().byteStream();
        }
        else {
            /**
             * 访问MediaSegment失败
             */
            response.close();

            throw new IOException("access " + url + " fail, " + response.message());
        }
    }

    @Override
    public int read() throws IOException {
        return mInput.read();
    }

    @Override
    public void close() throws IOException {
        mInput.close();
    }
}
