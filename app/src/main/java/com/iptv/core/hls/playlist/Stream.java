package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.tag.StreamInfTag;

/**
 * 流
 */
public final class Stream {
    private StreamInfTag mTag;
    private String mUri;

    /**
     * 构造函数
     */
    public Stream(StreamInfTag tag, String uri) {
        mTag = tag;
        mUri = uri;
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mUri;
    }
}
