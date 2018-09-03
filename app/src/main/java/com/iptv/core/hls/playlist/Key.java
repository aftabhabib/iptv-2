package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;
import com.iptv.core.utils.MalformedFormatException;
import com.iptv.core.utils.StringUtils;

import java.math.BigInteger;

/**
 * 密钥
 */
public final class Key {
    /**
     * 属性
     */
    private static final String ATTR_METHOD = "METHOD";
    private static final String ATTR_IV = "IV";
    private static final String ATTR_URI = "URI";

    /**
     * 加密方式
     */
    public static final String METHOD_NONE = "NONE";
    public static final String METHOD_AES_128 = "AES-128";
    public static final String METHOD_SAMPLE_AES = "SAMPLE-AES";

    private String mMethod = null;
    private MetaData mMetaData = new MetaData();

    /**
     * 构造函数
     */
    public Key(String[] attributes) throws MalformedFormatException {
        for (String attribute : attributes) {
            String[] result = attribute.split("=");
            parseAttribute(result[0], result[1]);
        }

        if (!StringUtils.isValid(mMethod)) {
            throw new MalformedFormatException("METHOD is required");
        }

        if (!mMethod.equals(METHOD_NONE) && !mMetaData.containsKey(ATTR_URI)) {
            throw new MalformedFormatException("URI is required when METHOD is not NONE");
        }
    }

    /**
     * 解析属性
     */
    private void parseAttribute(String name, String value) {
        if (name.equals(ATTR_METHOD)) {
            mMethod = value;
        }
        else if (name.equals(ATTR_IV)) {
            String iv;
            if (value.startsWith("0x") || value.startsWith("0X")) {
                iv = value.substring(2);
            }
            else {
                iv = value;
            }

            mMetaData.putByteArray(ATTR_IV, new BigInteger(iv, 16).toByteArray());
        }
        else if (name.equals(ATTR_URI)) {
            mMetaData.putString(ATTR_URI, value);
        }
        else {
            /**
             * ignore
             */
        }
    }

    /**
     * 获取加密方式
     */
    public String getMethod() {
        return mMethod;
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mMetaData.containsKey(ATTR_URI);
    }

    /**
     * 获取url
     */
    public String getUri() {
        if (!containsUri()) {
            throw new IllegalStateException("no URI");
        }

        return mMetaData.getString(ATTR_URI);
    }

    /**
     * 是否定义了初始向量
     */
    public boolean containsInitVector() {
        return mMetaData.containsKey(ATTR_IV);
    }

    /**
     * 获取初始向量
     */
    public byte[] getInitVector() {
        if (!containsInitVector()) {
            throw new IllegalStateException("no IV");
        }

        return mMetaData.getByteArray(ATTR_IV);
    }
}
