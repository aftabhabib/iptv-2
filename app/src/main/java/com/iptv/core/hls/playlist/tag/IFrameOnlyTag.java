package com.iptv.core.hls.playlist.tag;

public final class IFrameOnlyTag extends Tag {
    /**
     * 构造函数
     */
    public IFrameOnlyTag() {
        super(Name.I_FRAMES_ONLY);
    }

    @Override
    public int getProtocolVersion() {
        return 4;
    }
}
