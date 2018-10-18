package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.datatype.EnumeratedString;
import com.iptv.core.hls.playlist.rendition.RenditionGroup;
import com.iptv.core.hls.playlist.rendition.RenditionList;
import com.iptv.core.hls.playlist.tag.StreamInfTag;
import com.iptv.core.hls.utils.HttpHelper;
import com.iptv.core.hls.utils.UrlHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * 流
 */
public final class Stream {
    private RenditionList mRenditionList;

    private StreamInfTag mStreamInfTag;
    private String mUri;
    private String mPlaylistUrl;

    /**
     * 构造函数
     */
    public Stream(RenditionList renditionList,
                  StreamInfTag streamInfTag, String uri, String playlistUrl) {
        mRenditionList = renditionList;

        mStreamInfTag = streamInfTag;
        mUri = uri;
        mPlaylistUrl = playlistUrl;
    }

    /**
     * 获取带宽
     */
    public int getBandwidth() {
        return mStreamInfTag.getBandwidth();
    }

    /**
     * 是否包含可替代的音频展示
     */
    public boolean containsAlternativeAudioRenditions() {
        return mStreamInfTag.containsAttribute(Attribute.Name.AUDIO);
    }

    /**
     * 获取可替代的音频展示
     */
    public RenditionGroup getAlternativeAudioRenditions() {
        return mRenditionList.getGroup(
                EnumeratedString.AUDIO, mStreamInfTag.getAudioGroupId());
    }

    /**
     * 是否包含可替代的视频展示
     */
    public boolean containsAlternativeVideoRenditions() {
        return mStreamInfTag.containsAttribute(Attribute.Name.VIDEO);
    }

    /**
     * 获取可替代的视频展示
     */
    public RenditionGroup getAlternativeVideoRenditions() {
        return mRenditionList.getGroup(
                EnumeratedString.VIDEO, mStreamInfTag.getVideoGroupId());
    }

    /**
     * 是否包含可替代的字幕展示
     */
    public boolean containsAlternativeSubtitleRenditions() {
        return mStreamInfTag.containsAttribute(Attribute.Name.SUBTITLES);
    }

    /**
     * 获取可替代的字幕展示
     */
    public RenditionGroup getAlternativeSubtitleRenditions() {
        return mRenditionList.getGroup(
                EnumeratedString.SUBTITLES, mStreamInfTag.getSubtitleGroupId());
    }

    /**
     * 是否包含可替代的CC展示
     */
    public boolean containsAlternativeClosedCaptionRenditions() {
        return mStreamInfTag.containsAttribute(Attribute.Name.CLOSED_CAPTIONS);
    }

    /**
     * 获取可替代的CC展示
     */
    public RenditionGroup getAlternativeClosedCaptionRenditions() {
        return mRenditionList.getGroup(
                EnumeratedString.CLOSED_CAPTIONS, mStreamInfTag.getClosedCaptionGroupId());
    }

    /**
     * 获取媒体列表
     */
    public MediaPlaylist getPlaylist() throws IOException {
        InputStream input = HttpHelper.get(
                UrlHelper.makeUrl(mPlaylistUrl, mUri), null);

        return MediaPlaylist.read(input);
    }
}
