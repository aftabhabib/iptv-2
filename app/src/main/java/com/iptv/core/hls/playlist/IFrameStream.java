package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.tag.IFrameStreamInfTag;
import com.iptv.core.hls.utils.UrlHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * I帧流
 */
public final class IFrameStream {
    private IFrameStreamInfTag mIFrameStreamInfTag;

    /**
     * 构造函数
     */
    public IFrameStream(IFrameStreamInfTag iFrameStreamInfTag) {
        mIFrameStreamInfTag = iFrameStreamInfTag;
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
    public Rendition[] getVideoRenditions(Rendition[] renditions) {
        if (!containsVideoRenditions()) {
            throw new IllegalStateException("no alternative video renditions");
        }

        List<Rendition> videoGroup = new ArrayList<>();

        String videoGroupId = mIFrameStreamInfTag.getVideoGroupId();
        for (Rendition rendition : renditions) {
            if (rendition.isVideo() && rendition.getGroupId().equals(videoGroupId)) {
                videoGroup.add(rendition);
            }
        }

        return videoGroup.toArray(new Rendition[videoGroup.size()]);
    }

    /**
     * 获取媒体播放列表
     */
    public MediaPlaylist getMediaPlaylist(String baseUri) throws IOException {
        Playlist playlist = Playlist.load(
                UrlHelper.makeUrl(baseUri, mIFrameStreamInfTag.getUri()), null);
        if (playlist.getType() != Playlist.TYPE_MEDIA) {
            throw new IllegalStateException("should be media playlist");
        }

        return (MediaPlaylist)playlist;
    }
}
