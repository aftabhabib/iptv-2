package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.datatype.EnumeratedString;
import com.iptv.core.hls.playlist.tag.IFrameStreamInfTag;
import com.iptv.core.hls.utils.HttpHelper;
import com.iptv.core.hls.utils.UrlHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * I帧流
 */
public final class IFrameStream {
    private String mPlaylistUrl;

    private List<Rendition> mVideoRenditionList = new ArrayList<>();

    private IFrameStreamInfTag mIFrameStreamInfTag;

    /**
     * 构造函数
     */
    public IFrameStream(IFrameStreamInfTag iFrameStreamInfTag) {
        mIFrameStreamInfTag = iFrameStreamInfTag;
    }

    /**
     * 设置播放列表url
     */
    void setPlaylistUrl(String playlistUrl) {
        mPlaylistUrl = playlistUrl;
    }

    /**
     * 设置展示列表
     */
    void setRenditionList(List<Rendition> renditionList) {
        for (Rendition rendition : renditionList) {
            String type = rendition.getType();
            String groupId = rendition.getGroupId();

            if (type.equals(EnumeratedString.VIDEO)) {
                if (containsVideoRenditions()
                        && mIFrameStreamInfTag.getVideoGroupId().equals(groupId)) {
                    mVideoRenditionList.add(rendition);
                }
            }
        }
    }

    /**
     * 获取带宽
     */
    public int getBandwidth() {
        return mIFrameStreamInfTag.getBandwidth();
    }

    /**
     * 是否包含（可替代的）视频展示
     */
    public boolean containsVideoRenditions() {
        return mIFrameStreamInfTag.containsAttribute(Attribute.Name.VIDEO);
    }

    /**
     * 获取（可替代的）视频展示
     */
    public Rendition[] getVideoRenditions() {
        return mVideoRenditionList.toArray(
                new Rendition[mVideoRenditionList.size()]);
    }

    /**
     * 获取默认的音频展示
     */
    public Rendition getDefaultVideoRendition() {
        for (Rendition rendition : mVideoRenditionList) {
            if (rendition.isDefaultSelection()) {
                return rendition;
            }
        }

        return null;
    }

    /**
     * 获取媒体列表
     */
    public Playlist getPlaylist() throws IOException {
        InputStream input = HttpHelper.get(
                UrlHelper.makeUrl(mPlaylistUrl, mIFrameStreamInfTag.getUri()), null);

        return null;
    }
}
