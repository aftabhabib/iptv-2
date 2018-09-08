package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;

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
    public Key() {
        /**
         * nothing
         */
    }

    /**
     * 设置属性
     */
    public void setAttribute(String name, String value) {
        if (name.equals(ATTR_METHOD)) {
            setMethod(AttributeValue.readEnumeratedString(value));
        }
        else if (name.equals(ATTR_URI)) {
            setUri(AttributeValue.readQuotedString(value));
        }
        else if (name.equals(ATTR_IV)) {
            setInitVector(AttributeValue.readHexadecimalSequence(value));
        }
        else if (name.equals(ATTR_KEY_FORMAT)) {
            setKeyFormat(AttributeValue.readQuotedString(value));
        }
        else if (name.equals(ATTR_KEY_FORMAT_VERSIONS)) {
            String content = AttributeValue.readQuotedString(value);

            int[] versions;
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

            setKeyFormatVersions(versions);
        }
        else {
            /**
             * ignore
             */
        }
    }

    /**
     * 设置加密方式
     */
    public void setMethod(String method) {
        if (!method.equals(METHOD_NONE)
                && !method.equals(METHOD_AES_128)
                && !method.equals(METHOD_SAMPLE_AES)) {
            throw new IllegalArgumentException("invalid METHOD value");
        }

        mMetaData.putString(ATTR_METHOD, method);
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        mMetaData.putString(ATTR_URI, uri);
    }

    /**
     * 设置初始向量
     */
    public void setInitVector(byte[] iv) {
        if (iv.length != 16) {
            throw new IllegalArgumentException("IV should be 128-bit");
        }

        mMetaData.putByteArray(ATTR_IV, iv);
    }

    /**
     * 设置密钥格式
     */
    public void setKeyFormat(String format) {
        mMetaData.putString(ATTR_KEY_FORMAT, format);
    }

    /**
     * 设置密钥格式的版本
     */
    public void setKeyFormatVersions(int[] versions) {
        mMetaData.putIntegerArray(ATTR_KEY_FORMAT_VERSIONS, versions);
    }

    /**
     * 是否定义了加密方式
     */
    public boolean containsMethod() {
        return mMetaData.containsKey(ATTR_METHOD);
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mMetaData.containsKey(ATTR_URI);
    }

    /**
     * 是否定义了初始向量
     */
    public boolean containsInitVector() {
        return mMetaData.containsKey(ATTR_IV);
    }

    /**
     * 获取加密方式
     */
    public String getMethod() {
        if (!containsMethod()) {
            throw new IllegalStateException("no METHOD attribute");
        }

        return mMetaData.getString(ATTR_METHOD);
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
             * 默认值，标准格式
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
        if (!mMetaData.containsKey(ATTR_KEY_FORMAT_VERSIONS)) {
            /**
             * 默认值，认为版本是1
             */
            return new int[] { 1 };
        }
        else {
            return mMetaData.getIntegerArray(ATTR_KEY_FORMAT_VERSIONS);
        }
    }

    /**
     * 生成属性列表
     */
    public String makeAttributeList() {
        StringBuilder builder = new StringBuilder();

        for (String key : mMetaData.keySet()) {
            if (key.equals(ATTR_METHOD)) {
                String method = getMethod();

                builder.append(ATTR_METHOD
                        + "="
                        + AttributeValue.writeEnumeratedString(method));
            }
            else if (key.equals(ATTR_URI)) {
                String uri = getUri();

                builder.append(ATTR_URI
                        + "="
                        + AttributeValue.writeQuotedString(uri));
            }
            else if (key.equals(ATTR_IV)) {
                byte[] iv = getInitVector();

                builder.append(ATTR_IV
                        + "="
                        + AttributeValue.writeHexadecimalSequence(iv));
            }
            else if (key.equals(ATTR_KEY_FORMAT)) {
                String format = getKeyFormat();

                builder.append(ATTR_KEY_FORMAT
                        + "="
                        + AttributeValue.writeQuotedString(format));
            }
            else if (key.equals(ATTR_KEY_FORMAT_VERSIONS)) {
                int[] versions = getKeyFormatVersions();

                StringBuffer contentBuffer = new StringBuffer();
                for (int i = 0; i < versions.length; i++) {
                    if (i > 0) {
                        contentBuffer.append("/");
                    }

                    contentBuffer.append(String.valueOf(versions[i]));
                }

                builder.append(ATTR_KEY_FORMAT_VERSIONS
                        + "="
                        + AttributeValue.writeQuotedString(contentBuffer.toString()));
            }
            else {
                /**
                 * ignore
                 */
                continue;
            }

            if (builder.length() > 0) {
                builder.append(",");
            }
        }

        return builder.toString();
    }
}
