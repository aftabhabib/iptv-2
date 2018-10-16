package com.iptv.core.hls.playlist.tag;

public final class EndListTag extends Tag {
    /**
     * 构造函数
     */
    public EndListTag() {
        super(Name.END_LIST);
    }

    @Override
    public int getProtocolVersion() {
        return 1;
    }
}
