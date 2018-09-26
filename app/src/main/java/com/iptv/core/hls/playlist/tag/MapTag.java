package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.playlist.attribute.AttributeList;

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

    @Override
    protected boolean containsValue() {
        return true;
    }

    @Override
    protected String getStringValue() {
        return mAttributeList.toString();
    }
}
