package com.iptv.core.hls.session;

import android.util.Log;

import com.iptv.core.utils.IOUtils;
import com.iptv.core.utils.OkHttp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Response;

/**
 * 解密
 */
final class Decrypter {
    private static final String TAG = "Decrypter";

    private String mUrl;
    private byte[] mKey;
    private byte[] mInitVector;

    /**
     * 构造函数
     */
    public Decrypter(String url, byte[] iv) throws IOException {
        mUrl = url;
        mInitVector = iv;

        mKey = loadKey();
        if (mKey == null) {
            throw new IOException("load key fail");
        }
    }

    /**
     * 加载密钥数据
     */
    private byte[] loadKey() {
        byte[] key = null;

        Response response = null;
        try {
            response = OkHttp.get(mUrl, null);

            if (response.isSuccessful()) {
                InputStream input = response.body().byteStream();
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                IOUtils.save(input, output);

                key = output.toByteArray();
            }
            else {
                Log.e(TAG, "GET " + mUrl + " fail, " + response.message());
            }
        }
        catch (IOException e) {
            Log.e(TAG, "read " + mUrl + " error");
        }
        finally {
            /**
             * 释放连接
             */
            if (response != null) {
                response.close();
            }
        }

        return key;
    }

    /**
     * 更新密钥
     */
    public void updateKey(String url, byte[] iv) throws IOException {
        if (mUrl.equals(url) && isInitVectorChanged(iv)) {
            return;
        }

        if (!mUrl.equals(url)) {
            mKey = loadKey();
            if (mKey == null) {
                throw new IOException("load key fail");
            }
        }

        if (!isInitVectorChanged(iv)) {
            mInitVector = iv;
        }
    }

    /**
     * 密钥的初始向量是否一致
     */
    private boolean isInitVectorChanged(byte[] iv) {
        return Arrays.hashCode(iv) == Arrays.hashCode(mKey);
    }

    /**
     * 解密
     */
    public InputStream decrypt(InputStream input) throws GeneralSecurityException {
        return new CipherInputStream(input, createCipher());
    }

    /**
     * 创建解密密钥
     */
    private Cipher createCipher() throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

        Key cipherKey = new SecretKeySpec(mKey, "AES");
        AlgorithmParameterSpec cipherIV = new IvParameterSpec(mInitVector);
        cipher.init(Cipher.DECRYPT_MODE, cipherKey, cipherIV);

        return cipher;
    }
}
