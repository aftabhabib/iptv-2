package com.iptv.core.hls.playlist;

import com.iptv.core.hls.datatype.EnumeratedString;
import com.iptv.core.hls.datatype.HexadecimalSequence;
import com.iptv.core.hls.datatype.QuotedString;

/**
 * 密钥
 */
public final class Key {
    private Attribute mAttribute = new Attribute();

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
        if (name.equals(Attribute.METHOD)) {
            mAttribute.putEnumeratedString(name, EnumeratedString.parse(value));
        }
        else if (name.equals(Attribute.URI)) {
            mAttribute.putQuotedString(name, QuotedString.parse(value));
        }
        else if (name.equals(Attribute.IV)) {
            mAttribute.putHexadecimalSequence(name, HexadecimalSequence.parse(value));
        }
        else if (name.equals(Attribute.KEY_FORMAT)) {
            mAttribute.putQuotedString(name, QuotedString.parse(value));
        }
        else if (name.equals(Attribute.KEY_FORMAT_VERSIONS)) {
            mAttribute.putQuotedString(name, QuotedString.parse(value));
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
        if (!method.equals(Attribute.METHOD_AES_128)
                && !method.equals(Attribute.METHOD_SAMPLE_AES)) {
            throw new IllegalArgumentException("invalid method");
        }

        mAttribute.putEnumeratedString(Attribute.METHOD, new EnumeratedString(method));
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        mAttribute.putQuotedString(Attribute.URI, new QuotedString(uri));
    }

    /**
     * 设置初始向量
     */
    public void setInitVector(byte[] iv) {
        if (iv.length != 16) {
            throw new IllegalArgumentException("iv should be 128 bits");
        }

        mAttribute.putHexadecimalSequence(Attribute.IV, new HexadecimalSequence(iv));
    }

    /**
     * 设置密钥格式
     */
    public void setFormat(String format) {
        if (!format.equals(Attribute.FORMAT_IDENTITY)) {
            throw new IllegalArgumentException("invalid format");
        }

        mAttribute.putQuotedString(Attribute.KEY_FORMAT, new QuotedString(format));
    }

    /**
     * 设置密钥格式符合的（各个）版本
     */
    public void setVersions(int[] versions) {
        mAttribute.putQuotedString(Attribute.KEY_FORMAT_VERSIONS,
                new QuotedString(versions, "/"));
    }

    /**
     * 是否定义了加密方式
     */
    public boolean containsMethod() {
        return mAttribute.containsName(Attribute.METHOD);
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mAttribute.containsName(Attribute.URI);
    }

    /**
     * 是否定义了初始向量
     */
    public boolean containsInitVector() {
        return mAttribute.containsName(Attribute.IV);
    }

    /**
     * 获取加密方式
     */
    public String getMethod() {
        EnumeratedString method = mAttribute.getEnumeratedString(Attribute.METHOD);
        return method.value();
    }

    /**
     * 获取uri
     */
    public String getUri() {
        QuotedString uri = mAttribute.getQuotedString(Attribute.URI);
        return uri.value();
    }

    /**
     * 获取初始向量
     */
    public byte[] getInitVector() {
        HexadecimalSequence iv = mAttribute.getHexadecimalSequence(Attribute.IV);
        return iv.toByteArray();
    }

    /**
     * 获取密钥格式
     */
    public String getFormat() {
        if (!mAttribute.containsName(Attribute.KEY_FORMAT)) {
            /**
             * 默认值：标准格式
             */
            return Attribute.FORMAT_IDENTITY;
        }
        else {
            QuotedString format = mAttribute.getQuotedString(Attribute.KEY_FORMAT);
            return format.value();
        }
    }

    /**
     * 获取密钥格式符合的（各个）版本
     */
    public int[] getVersions() {
        if (!mAttribute.containsName(Attribute.KEY_FORMAT_VERSIONS)) {
            /**
             * 默认值：版本是1
             */
            return new int[] { 1 };
        }
        else {
            QuotedString versions = mAttribute.getQuotedString(Attribute.KEY_FORMAT_VERSIONS);
            /**
             * one or more integers separated by the "/" character
             */
            return versions.splitToIntArray("/");
        }
    }

    /**
     * 生成属性列表
     */
    public String makeAttributeList() {
        return mAttribute.toString();
    }
}
