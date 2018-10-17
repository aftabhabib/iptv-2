package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.playlist.datatype.ByteRange;
import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.datatype.QuotedString;

/**
 * 映射标签
 */
public final class MapTag extends Tag {
    private AttributeList mAttributeList;

    /**
     * 构造函数
     */
    public MapTag(AttributeList attributeList) {
        super(Name.MAP);

        mAttributeList = attributeList;
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
    public String getUri() {
        Attribute attribute = mAttributeList.get(Attribute.Name.URI);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取字节范围
     */
    public ByteRange getByteRange() {
        Attribute attribute = mAttributeList.get(Attribute.Name.BYTE_RANGE);
        return ByteRange.valueOf(attribute.getQuotedStringValue().getContent());
    }

    @Override
    public int getProtocolVersion() {
        return 5;
    }

    @Override
    public String toString() {
        return mName + ":" + mAttributeList.toString();
    }

    /**
     * 建造者
     */
    public static class Builder {
        private AttributeList mAttributeList = new AttributeList();

        /**
         * 构造函数
         */
        public Builder() {
            /**
             * nothing
             */
        }

        /**
         * 设置uri
         */
        public void setUri(String uri) {
            Attribute attribute = new Attribute(Attribute.Name.URI, new QuotedString(uri));
            mAttributeList.put(attribute);
        }

        /**
         * 设置字节范围
         */
        public void setByteRange(ByteRange range) {
            Attribute attribute = new Attribute(Attribute.Name.BYTE_RANGE,
                    new QuotedString(range.toString()));
            mAttributeList.put(attribute);
        }

        /**
         * 创建
         */
        public MapTag build() {
            return new MapTag(mAttributeList);
        }
    }
}
