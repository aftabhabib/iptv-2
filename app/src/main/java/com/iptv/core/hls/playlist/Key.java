package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;

public final class Key {
    /**
     * 加密方式
     */
    public static final String METHOD_NONE = "NONE";
    public static final String METHOD_AES_128 = "AES-128";
    public static final String METHOD_SAMPLE_AES = "SAMPLE-AES";

    private MetaData mMetaData;
    private String mUrl;

    /**
     * 构造函数
     */
    public Key() {
        mMetaData = new MetaData();
        mUrl = "";
    }

    /**
     * 获取元信息
     */
    public MetaData getMetaData() {
        return mMetaData;
    }

    /**
     * 设置url
     */
    public void setUrl(String url) {
        mUrl = url;
    }

    /**
     * 获取url
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * 获取加密方式
     */
    public String getMethod() {
        if (!mMetaData.containsKey(MetaData.KEY_CIPHER_METHOD)) {
            throw new IllegalStateException("method is required");
        }

        return mMetaData.getString(MetaData.KEY_CIPHER_METHOD);
    }

    /**
     * 获取密钥的初始向量
     */
    public byte[] getInitVector() {
        if (!mMetaData.containsKey(MetaData.KEY_CIPHER_IV)) {
            return null;
        }
        else {
            return mMetaData.getByteArray(MetaData.KEY_CIPHER_IV);
        }
    }
}
