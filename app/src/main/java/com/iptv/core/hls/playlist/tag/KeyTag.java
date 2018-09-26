package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.playlist.attribute.AttributeList;

/**
 * 密钥标签
 */
public final class KeyTag extends Tag {
    private AttributeList mAttributeList;

    /**
     * 构造函数
     */
    public KeyTag(AttributeList attributeList) {
        super(Name.KEY);

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
