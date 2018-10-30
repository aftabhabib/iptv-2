package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.tag.StreamInfTag;
import com.iptv.core.hls.utils.UrlHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 流
 */
public final class Stream {
    private StreamInfTag mStreamInfTag;
    private String mUri;

    /**
     * 构造函数
     */
    public Stream(StreamInfTag streamInfTag, String uri) {
        mStreamInfTag = streamInfTag;
        mUri = uri;
    }

    /**
     * 获取带宽
     */
    public int getBandwidth() {
        return mStreamInfTag.getBandwidth();
    }

    /**
     * 是否包含（可替代的）音频展示
     */
    public boolean containsAudioRendition() {
        return mStreamInfTag.containsAttribute(Attribute.Name.AUDIO);
    }

    /**
     * 获取（可替代的）音频展示
     */
    public Rendition[] getAudioRenditions(Rendition[] renditions) {
        if (!containsAudioRendition()) {
            throw new IllegalStateException("no alternative audio rendition");
        }

        List<Rendition> audioGroup = new ArrayList<>();

        String audioGroupId = mStreamInfTag.getAudioGroupId();
        for (Rendition rendition : renditions) {
            if (rendition.isAudio() && rendition.getGroupId().equals(audioGroupId)) {
                audioGroup.add(rendition);
            }
        }

        return audioGroup.toArray(new Rendition[audioGroup.size()]);
    }

    /**
     * 是否包含（可替代的）视频展示
     */
    public boolean containsVideoRendition() {
        return mStreamInfTag.containsAttribute(Attribute.Name.VIDEO);
    }

    /**
     * 获取（可替代的）视频展示
     */
    public Rendition[] getVideoRenditions(Rendition[] renditions) {
        if (!containsVideoRendition()) {
            throw new IllegalStateException("no alternative video rendition");
        }

        List<Rendition> videoGroup = new ArrayList<>();

        String videoGroupId = mStreamInfTag.getVideoGroupId();
        for (Rendition rendition : renditions) {
            if (rendition.isVideo() && rendition.getGroupId().equals(videoGroupId)) {
                videoGroup.add(rendition);
            }
        }

        return videoGroup.toArray(new Rendition[videoGroup.size()]);
    }

    /**
     * 是否包含（可替代的）字幕展示
     */
    public boolean containsSubtitleRendition() {
        return mStreamInfTag.containsAttribute(Attribute.Name.SUBTITLES);
    }

    /**
     * 获取（可替代的）字幕展示
     */
    public Rendition[] getSubtitleRenditions(Rendition[] renditions) {
        if (!containsSubtitleRendition()) {
            throw new IllegalStateException("no alternative subtitle rendition");
        }

        List<Rendition> subtitleGroup = new ArrayList<>();

        String subtitleGroupId = mStreamInfTag.getSubtitleGroupId();
        for (Rendition rendition : renditions) {
            if (rendition.isSubtitle() && rendition.getGroupId().equals(subtitleGroupId)) {
                subtitleGroup.add(rendition);
            }
        }

        return subtitleGroup.toArray(new Rendition[subtitleGroup.size()]);
    }

    /**
     * 是否包含（可替代的）隱藏字幕展示
     */
    public boolean containsClosedCaptionRendition() {
        return mStreamInfTag.containsAttribute(Attribute.Name.CLOSED_CAPTIONS);
    }

    /**
     * 获取（可替代的）隱藏字幕展示
     */
    public Rendition[] getClosedCaptionRenditions(Rendition[] renditions) {
        if (!containsClosedCaptionRendition()) {
            throw new IllegalStateException("no alternative closed caption rendition");
        }

        List<Rendition> ccGroup = new ArrayList<>();

        String ccGroupId = mStreamInfTag.getClosedCaptionGroupId();
        for (Rendition rendition : renditions) {
            if (rendition.isClosedCaption() && rendition.getGroupId().equals(ccGroupId)) {
                ccGroup.add(rendition);
            }
        }

        return ccGroup.toArray(new Rendition[ccGroup.size()]);
    }

    /**
     * 获取媒体播放列表
     */
    public MediaPlaylist getMediaPlaylist(String baseUri) throws IOException {
        Playlist playlist = Playlist.load(UrlHelper.makeUrl(baseUri, mUri), null);
        if (playlist.getType() != Playlist.TYPE_MEDIA) {
            throw new IllegalStateException("should be media playlist");
        }

        return (MediaPlaylist)playlist;
    }
}
