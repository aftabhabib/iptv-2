package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.attribute.AttributeName;
import com.iptv.core.hls.playlist.datatype.EnumeratedString;

/**
 * 媒体数据的密钥
 */
public final class Key {
    private AttributeList mAttributeList = new AttributeList();

    /**
     * 构造函数
     */
    public Key() {
        /**
         * nothing
         */
    }

    /**
     * 设置加密方式
     */
    public void setMethod(String method) {
        if ((method == null) || !isValidMethod(method)) {
            throw new IllegalArgumentException("invalid method");
        }

        mAttributeList.putString(AttributeName.METHOD, method);
    }

    /**
     * 是否有效的加密方式
     */
    private static boolean isValidMethod(String method) {
        return method.equals(EnumeratedString.NONE)
                || method.equals(EnumeratedString.AES_128)
                || method.equals(EnumeratedString.SAMPLE_AES);
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        if ((uri == null) || uri.isEmpty()) {
            throw new IllegalArgumentException("invalid uri");
        }

        mAttributeList.putString(AttributeName.URI, uri);
    }

    /**
     * 设置初始向量
     */
    public void setInitVector(byte[] iv) {
        if ((iv == null) || !isValidInitVector(iv)) {
            throw new IllegalArgumentException("invalid iv");
        }

        mAttributeList.putByteArray(AttributeName.IV, iv);
    }

    /**
     * 是否有效的初始向量
     */
    private static boolean isValidInitVector(byte[] iv) {
        return iv.length == 16;
    }

    /**
     * 设置密钥格式
     */
    public void setFormat(String format) {
        if ((format == null) || format.isEmpty()) {
            throw new IllegalArgumentException("invalid format");
        }

        mAttributeList.putString(AttributeName.KEY_FORMAT, format);
    }

    /**
     * 设置密钥格式符合的（各个）版本
     */
    public void setVersions(int[] versions) {
        if ((versions == null) || (versions.length == 0)) {
            throw new IllegalArgumentException("invalid versions");
        }

        mAttributeList.putIntArray(AttributeName.KEY_FORMAT_VERSIONS, versions);
    }

    /**
     * 是否定义了加密方式
     */
    public boolean containsMethod() {
        return mAttributeList.containsName(AttributeName.METHOD);
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mAttributeList.containsName(AttributeName.URI);
    }

    /**
     * 是否定义了初始向量
     */
    public boolean containsInitVector() {
        return mAttributeList.containsName(AttributeName.IV);
    }

    /**
     * 获取加密方式
     */
    public String getMethod() {
        return mAttributeList.getString(AttributeName.METHOD);
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mAttributeList.getString(AttributeName.URI);
    }

    /**
     * 获取初始向量
     */
    public byte[] getInitVector() {
        return mAttributeList.getByteArray(AttributeName.IV);
    }

    /**
     * 获取密钥格式
     */
    public String getFormat() {
        String format;
        if (!mAttributeList.containsName(AttributeName.KEY_FORMAT)) {
            /**
             * 默认值：标准格式
             */
            format = "identity";
        }
        else {
            format = mAttributeList.getString(AttributeName.KEY_FORMAT);
        }

        return format;
    }

    /**
     * 获取密钥格式符合的（各个）版本
     */
    public int[] getVersions() {
        int[] versions;
        if (!mAttributeList.containsName(AttributeName.KEY_FORMAT_VERSIONS)) {
            /**
             * 默认值：版本是1
             */
            versions = new int[] { 1 };
        }
        else {
            versions = mAttributeList.getIntArray(AttributeName.KEY_FORMAT_VERSIONS);
        }

        return versions;
    }
}
