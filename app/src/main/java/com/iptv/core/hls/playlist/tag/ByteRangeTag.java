package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.playlist.datatype.ByteRange;

/**
 * 字节范围标签
 */
public final class ByteRangeTag extends Tag {
    private ByteRange mRange;

    /**
     * 构造函数
     */
    public ByteRangeTag(ByteRange range) {
        super(Name.BYTE_RANGE);

        mRange = range;
    }

    /**
     * 获取范围
     */
    public ByteRange getRange() {
        return mRange;
    }

    @Override
    public String toString() {
        return mName + ":" + mRange.toString();
    }
}
