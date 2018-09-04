package com.iptv.core.hls.session;

import android.util.Log;

import com.iptv.core.utils.IOUtils;
import com.iptv.core.utils.OkHttp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Response;

/**
 * 解密
 */
final class AESCipher {
    private static final String TAG = "AESCipher";

    private String mUrl;
    private byte[] mInitVector;

    private byte[] mKey = null;
    private Cipher mCipher = null;

    /**
     * 构造函数
     */
    public AESCipher(String url, byte[] iv) {
        mUrl = url;
        mInitVector = iv;
    }

    /**
     * 更新密钥
     */
    public void updateKey(String url) {
        if (!isKeyChanged(url)) {
            return;
        }

        mUrl = url;

        /**
         * 重新获取key
         */
        mKey = null;
        mCipher = null;
    }

    /**
     * 密钥是否一致
     */
    private boolean isKeyChanged(String url) {
        return !mUrl.equals(url);
    }

    /**
     * 更新初始向量
     */
    public void updateInitVector(byte[] iv) {
        if (!isInitVectorChanged(iv)) {
            return;
        }

        mInitVector = iv;

        /**
         * 复用之前的key
         */
        mCipher = null;
    }

    /**
     * 初始向量是否一致
     */
    private boolean isInitVectorChanged(byte[] iv) {
        return !Arrays.equals(mInitVector, iv);
    }

    /**
     * 获取cipher
     */
    public Cipher getCipher() {
        if (mCipher == null) {
            if (mKey == null) {
                mKey = fetch(mUrl, null);
                if (mKey == null) {
                    Log.e(TAG,"fetch AES key fail");
                    return null;
                }
            }

            try {
                mCipher = createCipher();
            }
            catch (GeneralSecurityException e) {
                Log.e(TAG, "create cipher fail, " + e.getMessage());
            }
        }

        return mCipher;
    }

    /**
     * 创建cipher
     */
    private Cipher createCipher() throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(mKey, "AES"),
                new IvParameterSpec(mInitVector));

        return cipher;
    }

    /**
     * 获取数据
     */
    private static byte[] fetch(String url, Map<String, String> properties) {
        byte[] data = null;

        Response response = null;
        try {
            response = OkHttp.get(url, properties);

            if (response.isSuccessful()) {
                InputStream input = response.body().byteStream();
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                IOUtils.save(input, output);

                data = output.toByteArray();
            }
            else {
                Log.e(TAG, "GET " + url + " fail, " + response.message());
            }
        }
        catch (IOException e) {
            Log.e(TAG, "read " + url + " error");
        }
        finally {
            /**
             * 释放连接
             */
            if (response != null) {
                response.close();
            }
        }

        return data;
    }
}
