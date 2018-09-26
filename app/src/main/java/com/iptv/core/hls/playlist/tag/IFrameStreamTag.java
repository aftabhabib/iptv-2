package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.playlist.attribute.AttributeList;

/**
 * I帧流标签
 */
public final class IFrameStreamTag extends Tag {
    private AttributeList mAttributeList;

    /**
     * 构造函数
     */
    public IFrameStreamTag(AttributeList attributeList) {
        super(Name.I_FRAME_STREAM_INF);

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
