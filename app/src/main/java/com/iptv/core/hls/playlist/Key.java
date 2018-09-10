package com.iptv.core.hls.playlist;

import java.util.HashMap;
import java.util.Map;

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
    private static final String ATTR_KEY_FORMAT_VERSIONS = "KEYFORMATVERSIONS";

    /**
     * 加密方式
     */
    public static final String METHOD_AES_128 = "AES-128";
    public static final String METHOD_SAMPLE_AES = "SAMPLE-AES";

    /**
     * 密钥格式
     */
    public static final String FORMAT_IDENTITY = "identity";

    private Map<String, String> mAttributes = new HashMap<String, String>();

    /**
     * 构造函数
     */
    public Key() {
        /**
         * nothing
         */
    }

    /**
     * 设置属性
     */
    void setAttribute(String name, String value) {
        mAttributes.put(name, value);
    }

    /**
     * 设置加密方式
     */
    public void setMethod(String method) {
        if (!method.equals(AttributeValue.ENUM_STRING_NONE)
                && !method.equals(METHOD_AES_128)
                && !method.equals(METHOD_SAMPLE_AES)) {
            throw new IllegalArgumentException("invalid method");
        }

        mAttributes.put(ATTR_METHOD, AttributeValue.writeEnumeratedString(method));
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        mAttributes.put(ATTR_URI, AttributeValue.writeQuotedString(uri));

    }

    /**
     * 设置初始向量
     */
    public void setInitVector(byte[] iv) {
        if (iv.length != 16) {
            throw new IllegalArgumentException("IV should be 128-bit");
        }

        mAttributes.put(ATTR_IV, AttributeValue.writeHexadecimalSequence(iv));
    }

    /**
     * 设置密钥格式
     */
    public void setKeyFormat(String format) {
        if (!format.equals(FORMAT_IDENTITY)) {
            throw new IllegalArgumentException("invalid format");
        }

        mAttributes.put(ATTR_KEY_FORMAT, AttributeValue.writeQuotedString(format));
    }

    /**
     * 设置密钥格式的版本
     */
    public void setKeyFormatVersions(int[] versions) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < versions.length; i++) {
            if (i > 0) {
                buffer.append("/");
            }

            buffer.append(String.valueOf(versions[i]));
        }

        mAttributes.put(ATTR_KEY_FORMAT_VERSIONS, AttributeValue.writeQuotedString(buffer.toString()));
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mAttributes.containsKey(ATTR_URI);
    }

    /**
     * 是否定义了初始向量
     */
    public boolean containsInitVector() {
        return mAttributes.containsKey(ATTR_IV);
    }

    /**
     * 获取加密方式
     */
    public String getMethod() {
        String method;
        if (!mAttributes.containsKey(ATTR_METHOD)) {
            method = AttributeValue.ENUM_STRING_NONE;
        }
        else {
            method = AttributeValue.readEnumeratedString(mAttributes.get(ATTR_METHOD));
        }

        return method;
    }

    /**
     * 获取uri
     */
    public String getUri() {
        if (!containsUri()) {
            throw new IllegalStateException("no URI attribute");
        }

        return AttributeValue.readQuotedString(mAttributes.get(ATTR_URI));
    }

    /**
     * 获取初始向量
     */
    public byte[] getInitVector() {
        if (!containsInitVector()) {
            throw new IllegalStateException("no IV attribute");
        }

        return AttributeValue.readHexadecimalSequence(mAttributes.get(ATTR_IV));
    }

    /**
     * 获取密钥格式
     */
    public String getKeyFormat() {
        String keyFormat;
        if (!mAttributes.containsKey(ATTR_KEY_FORMAT)) {
            /**
             * 默认值：标准格式
             */
            keyFormat = FORMAT_IDENTITY;
        }
        else {
            keyFormat = AttributeValue.readQuotedString(mAttributes.get(ATTR_KEY_FORMAT));
        }

        return keyFormat;
    }

    /**
     * 获取密钥格式的版本
     */
    public int[] getKeyFormatVersions() {
        int[] versions;
        if (!mAttributes.containsKey(ATTR_KEY_FORMAT_VERSIONS)) {
            /**
             * 默认值：版本是1
             */
            versions = new int[] { 1 };
        }
        else {
            String content = AttributeValue.readQuotedString(mAttributes.get(ATTR_KEY_FORMAT_VERSIONS));

            /**
             * one or more values separated by the "/" character
             */
            if (content.contains("/")) {
                String[] results = content.split("/");

                versions = new int[results.length];
                for (int i = 0; i < results.length; i++) {
                    versions[i] = Integer.parseInt(results[i]);
                }
            }
            else {
                versions = new int[] { Integer.parseInt(content) };
            }
        }

        return versions;
    }

    /**
     * 生成属性列表
     */
    public String makeAttributeList() {
        StringBuilder builder = new StringBuilder();

        int attributeCnt = 0;
        for (String name : mAttributes.keySet()) {
            /**
             * 多个attribute之间通过“,”分隔
             */
            if (attributeCnt > 0) {
                builder.append(",");
            }

            builder.append(name);
            builder.append("=");
            builder.append(mAttributes.get(name));

            attributeCnt++;
        }

        return builder.toString();
    }
}
