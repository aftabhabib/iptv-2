package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.playlist.ByteRange;
import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.datatype.QuotedString;

/**
 * 映射标签
 */
public class MapTag extends Tag {
    private AttributeList mAttributeList;

    /**
     * 构造函数
     */
    public MapTag(AttributeList attributeList) {
        super(Name.MAP);

        mAttributeList = attributeList;
    }

    /**
     * 获取属性列表
     */
    public AttributeList getAttributeList() {
        return mAttributeList;
    }

    @Override
    protected boolean containsValue() {
        return true;
    }

    @Override
    protected String getStringValue() {
        return mAttributeList.toString();
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
         * 创建
         */
        public MapTag build() {
            return new MapTag(mAttributeList);
        }
    }
}
