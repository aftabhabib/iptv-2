package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.attribute.AttributeName;

/**
 * 映射（片段的初始化数据）
 */
public final class Map {
    private AttributeList mAttributeList = new AttributeList();

    /**
     * 构造函数
     */
    public Map() {
        /**
         * nothing
         */
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
     * 设置字节范围
     */
    public void setByteRange(ByteRange range) {
        if (range == null) {
            throw new IllegalArgumentException("invalid range");
        }

        mAttributeList.putByteRange(AttributeName.BYTE_RANGE, range);
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mAttributeList.containsName(AttributeName.URI);
    }

    /**
     * 是否定义了字节范围
     */
    public boolean containsByteRange() {
        return mAttributeList.containsName(AttributeName.BYTE_RANGE);
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mAttributeList.getString(AttributeName.URI);
    }

    /**
     * 获取字节范围
     */
    public ByteRange getByteRange() {
        return mAttributeList.getByteRange(AttributeName.BYTE_RANGE);
    }
}
