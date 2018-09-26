package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.exception.MalformedPlaylistException;
import com.iptv.core.hls.playlist.ByteRange;

/**
 * 字节范围标签
 */
public final class ByteRangeTag extends Tag {
    private ByteRange mRange;

    /**
     * 构造函数
     */
    public ByteRangeTag(String strRange) throws MalformedPlaylistException {
        super(Name.BYTE_RANGE);

        mRange = ByteRange.valueOf(strRange);
    }

    /**
     * 构造函数
     */
    public ByteRangeTag(ByteRange range) {
        super(Name.BYTE_RANGE);

        mRange = range;
    }

    @Override
    protected boolean containsValue() {
        return true;
    }

    @Override
    protected String getStringValue() {
        return mRange.toString();
    }
}
