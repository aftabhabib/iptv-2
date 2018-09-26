package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.exception.MalformedPlaylistException;
import com.iptv.core.hls.playlist.attribute.AttributeList;

/**
 * 媒体标签
 */
public final class MediaTag extends Tag {
    private AttributeList mAttributeList;

    /**
     * 构造函数
     */
    public MediaTag(String strAttributeList) throws MalformedPlaylistException {
        super(Name.MEDIA);

        mAttributeList = AttributeList.parse(strAttributeList);
    }

    /**
     * 构造函数
     */
    public MediaTag(AttributeList attributeList) {
        super(Name.MEDIA);

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
