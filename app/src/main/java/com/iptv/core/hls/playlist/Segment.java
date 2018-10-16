package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.tag.ByteRangeTag;
import com.iptv.core.hls.playlist.tag.DiscontinuityTag;
import com.iptv.core.hls.playlist.tag.InfTag;
import com.iptv.core.hls.playlist.tag.KeyTag;
import com.iptv.core.hls.playlist.tag.MapTag;

/**
 * 片段
 */
public final class Segment {
    private MapTag mMapTag;
    private KeyTag mKeyTag;

    private ByteRangeTag mRangeTag;
    private DiscontinuityTag mDiscontinuityTag;

    private InfTag mInfTag;
    private String mUri;

    /**
     * 构造函数
     */
    public Segment(MapTag mapTag, KeyTag keyTag,
                   ByteRangeTag rangeTag, DiscontinuityTag discontinuityTag,
                   InfTag infTag, String uri) {
        mMapTag = mapTag;
        mKeyTag = keyTag;

        mRangeTag = rangeTag;
        mDiscontinuityTag = discontinuityTag;

        mInfTag = infTag;
        mUri = uri;
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mUri;
    }
}
