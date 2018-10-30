package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.tag.VersionTag;

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
    public MasterPlaylist(String baseUri,
                          VersionTag versionTag,
                          List<Rendition> renditionList,
                          List<Stream> streamList,
                          List<IFrameStream> iFrameStreamList) {
        super(baseUri, versionTag);

        mRenditionList = renditionList;

        mStreamList = streamList;
        mIFrameStreamList = iFrameStreamList;
    }

    @Override
    public int getType() {
        return TYPE_MASTER;
    }
}
