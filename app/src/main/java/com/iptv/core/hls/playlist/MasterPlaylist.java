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

    /**
     * 是否定义有展示
     */
    public boolean containsRendition() {
        return !mRenditionList.isEmpty();
    }

    /**
     * 获取所有的展示
     */
    public Rendition[] getRenditions() {
        if (!containsRendition()) {
            throw new IllegalStateException("no rendition");
        }

        return mRenditionList.toArray(new Rendition[mRenditionList.size()]);
    }

    /**
     * 是否定义有流
     */
    public boolean containsStream() {
        return !mStreamList.isEmpty();
    }

    /**
     * 根据指定带宽获取适合的流
     */
    public Stream getStreamByBandwidth(int bandwidth) {
        if (!containsStream()) {
            throw new IllegalStateException("no stream");
        }

        for (int i = 0; i < mStreamList.size(); i++) {
            if (mStreamList.get(i).getBandwidth() > bandwidth) {
                return i > 0 ? mStreamList.get(i - 1) : mStreamList.get(0);
            }
        }

        return mStreamList.get(mStreamList.size() - 1);
    }

    /**
     * 是否定义有I帧流
     */
    public boolean containsIFrameStream() {
        return !mIFrameStreamList.isEmpty();
    }

    /**
     * 根据指定带宽获取适合的I帧流
     */
    public IFrameStream getIFrameStreamByBandwidth(int bandwidth) {
        if (!containsIFrameStream()) {
            throw new IllegalStateException("no I-frame stream");
        }

        for (int i = 0; i < mIFrameStreamList.size(); i++) {
            if (mIFrameStreamList.get(i).getBandwidth() > bandwidth) {
                return i > 0 ? mIFrameStreamList.get(i - 1) : mIFrameStreamList.get(0);
            }
        }

        return mIFrameStreamList.get(mIFrameStreamList.size() - 1);
    }
}
