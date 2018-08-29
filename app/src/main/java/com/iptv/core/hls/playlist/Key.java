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

    /**
     * 加密方式
     */
    public static final String METHOD_NONE = "NONE";
    public static final String METHOD_AES_128 = "AES-128";
    public static final String METHOD_SAMPLE_AES = "SAMPLE-AES";

    private MetaData mMetaData = new MetaData();
    private String mUri = null;

    /**
     * 构造函数
     */
    public Key(String attributeList) throws MalformedFormatException {
        String[] attributes = attributeList.split(",");
        for (int i = 0; i < attributes.length; i++) {
            String[] result = attributes[i].split("=");

            if (result[0].equals(ATTR_METHOD)) {
                mMetaData.putString(MetaData.KEY_CIPHER_METHOD, result[1]);
            }
            else if (result[0].equals(ATTR_IV)) {
                String value;
                if (result[1].startsWith("0x") || result[1].startsWith("0X")) {
                    value = result[1].substring(2);
                }
                else {
                    value = result[1];
                }

                if (value.length() != 32) {
                    throw new IllegalArgumentException("iv must be 128-bit");
                }

                byte[] iv = new BigInteger(value, 16).toByteArray();
                mMetaData.putByteArray(MetaData.KEY_CIPHER_IV, iv);
            }
            else if (result[0].equals(ATTR_URI)) {
                mUri = result[1];
            }
            else {
                /**
                 * not support yet
                 */
            }
        }

        /**
         * 检查必要参数
         */
        if (!mMetaData.containsKey(MetaData.KEY_CIPHER_METHOD)) {
            throw new MalformedFormatException("method is required");
        }
        else {
            if (!getMethod().equals(METHOD_NONE) && (mUri == null)) {
                throw new MalformedFormatException("uri is required when method is not NONE");
            }
        }
    }

    /**
     * 获取加密方式
     */
    public String getMethod() {
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

    /**
     * 获取url
     */
    public String getUri() {
        return mUri;
    }
}
