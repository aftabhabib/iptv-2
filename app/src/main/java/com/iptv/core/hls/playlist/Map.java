package com.iptv.core.hls.playlist;

import com.iptv.core.hls.exception.MalformedPlaylistException;
import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.datatype.QuotedString;
import com.iptv.core.hls.playlist.tag.Tag;

/**
 * 映射（片段的初始化数据）
 */
public final class Map {
    private AttributeList mAttributeList;

    /**
     * 构造函数
     */
    public Map() {
        mAttributeList = new AttributeList();
    }

    /**
     * 构造函数
     */
    public Map(AttributeList attributeList) {
        mAttributeList = attributeList;
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        Attribute attribute = Attribute.create(Attribute.Name.URI, new QuotedString(uri));
        mAttributeList.put(attribute);
    }

    /**
     * 设置字节范围
     */
    public void setByteRange(ByteRange range) {
        Attribute attribute = Attribute.create(
                Attribute.Name.BYTE_RANGE, new QuotedString(range.toString()));
        mAttributeList.put(attribute);
    }

    /**
     * 是否定义了uri
     */
    public boolean containsAttribute(String attributeName) {
        return mAttributeList.containsAttribute(attributeName);
    }

    /**
     * 获取uri
     */
    public String getUri() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.URI);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取字节范围
     */
    public ByteRange getByteRange() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.BYTE_RANGE);

        String strRange = attribute.getQuotedStringValue().getContent();
        return ByteRange.valueOf(strRange);
    }

    @Override
    public String toString() {
        return Tag.Name.MAP + ":" + mAttributeList.toString();
    }
}
