package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.exception.MalformedPlaylistException;
import com.iptv.core.hls.playlist.attribute.AttributeList;

/**
 * 流标签
 */
public final class StreamTag extends Tag {
    private AttributeList mAttributeList;

    /**
     * 构造函数
     */
    public StreamTag(String strAttributeList) throws MalformedPlaylistException {
        super(Name.STREAM_INF);

        mAttributeList = AttributeList.parse(strAttributeList);
    }

    /**
     * 构造函数
     */
    public StreamTag(AttributeList attributeList) {
        super(Name.STREAM_INF);

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
