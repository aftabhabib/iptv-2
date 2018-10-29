package com.iptv.core.hls.playlist;

import java.util.List;

/**
 * 主播放列表
 */
public final class MasterPlaylist extends Playlist {
    private List<Rendition> mRenditionList;

    private List<Stream> mStreamList;
    private List<IFrameStream> mIFrameStreamList;

    /**
     * 构造函数
     */
    public MasterPlaylist(List<Rendition> renditionList,
                          List<Stream> streamList, List<IFrameStream> iFrameStreamList) {
        super();

        mRenditionList = renditionList;

        mStreamList = streamList;
        mIFrameStreamList = iFrameStreamList;
    }
}
