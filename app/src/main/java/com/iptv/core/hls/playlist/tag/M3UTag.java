package com.iptv.core.hls.playlist.tag;

/**
 * M3U标签
 */
public final class M3UTag extends Tag {
    /**
     * 构造函数
     */
    public M3UTag() {
        super(Name.M3U);
    }

    @Override
    public int getProtocolVersion() {
        return 1;
    }
}
