package com.iptv.core.hls.playlist.tag;

public final class DiscontinuityTag extends Tag {
    /**
     * 构造函数
     */
    public DiscontinuityTag() {
        super(Name.DISCONTINUITY);
    }

    @Override
    public String toString() {
        return mName;
    }
}
