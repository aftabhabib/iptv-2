package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;
import com.iptv.core.utils.MalformedFormatException;

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
    private static final String ATTR_KEY_FORMAT = "KEYFORMAT";
    private static final String ATTR_KEY_FORMAT_VERSION = "KEYFORMATVERSIONS";

    /**
     * 加密方式
     */
    public static final String METHOD_NONE = "NONE";
    public static final String METHOD_AES_128 = "AES-128";
    public static final String METHOD_SAMPLE_AES = "SAMPLE-AES";

    /**
     * 密钥格式
     */
    public static final String FORMAT_IDENTITY = "identity";

    private MetaData mMetaData = new MetaData();

    /**
     * 构造函数
     */
    public Key(String[] attributes) throws MalformedFormatException {
        for (String attribute : attributes) {
            String[] result = attribute.split("=");
            parseAttribute(result[0], result[1]);
        }

        if (!mMetaData.containsKey(ATTR_METHOD)) {
            throw new MalformedFormatException("METHOD is required");
        }

        if (getMethod().equals(METHOD_NONE)) {
            if (mMetaData.size() > 1) {
                throw new MalformedFormatException("If METHOD is NONE, no other attributes");
            }
        }
        else {
            if (!mMetaData.containsKey(ATTR_URI)) {
                throw new MalformedFormatException("if METHOD is not NONE, URI is required");
            }
        }
    }

    /**
     * 解析属性
     */
    private void parseAttribute(String name, String value) throws MalformedFormatException {
        if (name.equals(ATTR_METHOD)) {
            mMetaData.putString(ATTR_METHOD, value);
        }
        else if (name.equals(ATTR_IV)) {
            String hexValue;
            if (value.startsWith("0x") || value.startsWith("0X")) {
                hexValue = value.substring(2);
            }
            else {
                hexValue = value;
            }

            byte[] iv = new BigInteger(hexValue, 16).toByteArray();
            if (iv.length != 16) {
                throw new MalformedFormatException("iv should be 128-bit");
            }

            mMetaData.putByteArray(ATTR_IV, iv);
        }
        else if (name.equals(ATTR_URI)) {
            mMetaData.putString(ATTR_URI, value);
        }
        else if (name.equals(ATTR_KEY_FORMAT)) {
            mMetaData.putString(ATTR_KEY_FORMAT, value);
        }
        else if (name.equals(ATTR_KEY_FORMAT_VERSION)) {
            int[] versions;
            if (value.contains("/")) {
                String[] results = value.split("/");

                versions = new int[results.length];
                for (int i = 0; i < results.length; i++) {
                    versions[i] = Integer.parseInt(results[i]);
                }
            }
            else {
                versions = new int[] { Integer.parseInt(value) };
            }

            mMetaData.putIntegerArray(ATTR_KEY_FORMAT_VERSION, versions);
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
        return mMetaData.getString(ATTR_METHOD);
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mMetaData.containsKey(ATTR_URI);
    }

    /**
     * 获取uri
     */
    public String getUri() {
        if (!containsUri()) {
            throw new IllegalStateException("no URI attribute");
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
            throw new IllegalStateException("no IV attribute");
        }

        return mMetaData.getByteArray(ATTR_IV);
    }

    /**
     * 获取密钥格式
     */
    public String getKeyFormat() {
        if (!mMetaData.containsKey(ATTR_KEY_FORMAT)) {
            /**
             * implicit value
             */
            return FORMAT_IDENTITY;
        }
        else {
            return mMetaData.getString(ATTR_KEY_FORMAT);
        }
    }

    /**
     * 获取密钥格式的版本
     */
    public int[] getKeyFormatVersions() {
        if (!mMetaData.containsKey(ATTR_KEY_FORMAT_VERSION)) {
            /**
             * if it is not present, its value is considered to be "1"
             */
            return new int[] { 1 };
        }
        else {
            return mMetaData.getIntegerArray(ATTR_KEY_FORMAT_VERSION);
        }
    }
}
