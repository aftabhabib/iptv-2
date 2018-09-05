package com.iptv.core.hls.session;

import com.iptv.core.player.DataSource;
import com.iptv.core.utils.OkHttp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

import okhttp3.Response;

/**
 * 片段数据源
 */
public class SegmentSource implements DataSource {
    private String mUrl;
    private Map<String, String> mProperties;

    private Cipher mCipher = null;

    private Response mResponse = null;
    private InputStream mContentStream = null;

    /**
     * 构造函数
     */
    public SegmentSource(String url, Map<String, String> properties) {
        mUrl = url;
        mProperties = properties;
    }

    /**
     * 设置解密密钥
     */
    public void setDecryptCipher(Cipher cipher) {
        mCipher = cipher;
    }

    /**
     * 连接
     */
    public boolean connect() throws IOException {
        if (mResponse != null) {
            throw new IllegalStateException("already connected");
        }

        mResponse = OkHttp.get(mUrl, mProperties);

        return mResponse.isSuccessful();
    }

    /**
     * 获取MIME类型
     */
    public String getMIMEType() {
        if (mResponse == null) {
            throw new IllegalStateException("not connected");
        }

        return mResponse.header("Content-Type");
    }

    @Override
    public int read(byte[] buffer, int offset, int size) throws IOException {
        if (mResponse == null) {
            throw new IllegalStateException("not connected");
        }

        if (mContentStream == null) {
            mContentStream = mResponse.body().byteStream();

            if (mCipher != null) {
                mContentStream = new CipherInputStream(mContentStream, mCipher);
            }
        }

        return mContentStream.read(buffer, offset, size);
    }

    /**
     * 断开
     */
    public void disconnect() {
        if (mResponse != null) {
            mResponse.close();
            mResponse = null;

            if (mContentStream != null) {
                mContentStream = null;
            }
        }
    }
}
