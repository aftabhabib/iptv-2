package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.datatype.EnumeratedString;
import com.iptv.core.hls.playlist.tag.StreamInfTag;
import com.iptv.core.hls.utils.HttpHelper;
import com.iptv.core.hls.utils.UrlHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 流
 */
public final class Stream implements Comparable<Integer> {
    private String mPlaylistUrl;

    private List<Rendition> mAudioRenditionList = new ArrayList<>();
    private List<Rendition> mVideoRenditionList = new ArrayList<>();
    private List<Rendition> mSubtitleRenditionList = new ArrayList<>();
    private List<Rendition> mClosedCaptionRenditionList = new ArrayList<>();

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

            if (type.equals(EnumeratedString.AUDIO)) {
                if (containsAudioRenditions()
                        && mStreamInfTag.getAudioGroupId().equals(groupId)) {
                    mAudioRenditionList.add(rendition);
                }
            }
            else if (type.equals(EnumeratedString.VIDEO)) {
                if (containsVideoRenditions()
                        && mStreamInfTag.getVideoGroupId().equals(groupId)) {
                    mVideoRenditionList.add(rendition);
                }
            }
            else if (type.equals(EnumeratedString.SUBTITLES)) {
                if (containsSubtitleRenditions()
                        && mStreamInfTag.getSubtitleGroupId().equals(groupId)) {
                    mSubtitleRenditionList.add(rendition);
                }
            }
            else if (type.equals(EnumeratedString.CLOSED_CAPTIONS)) {
                if (containsClosedCaptionRenditions()
                        && mStreamInfTag.getClosedCaptionGroupId().equals(groupId)) {
                    mClosedCaptionRenditionList.add(rendition);
                }
            }
        }
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
    public boolean containsAudioRenditions() {
        return mStreamInfTag.containsAttribute(Attribute.Name.AUDIO);
    }

    /**
     * 获取（可替代的）音频展示
     */
    public Rendition[] getAudioRenditions() {
        return mAudioRenditionList.toArray(
                new Rendition[mAudioRenditionList.size()]);
    }

    /**
     * 获取默认的音频展示
     */
    public Rendition getDefaultAudioRendition() {
        for (Rendition rendition : mAudioRenditionList) {
            if (rendition.isDefaultSelection()) {
                return rendition;
            }
        }

        return null;
    }

    /**
     * 获取与指定语言环境匹配的音频展示
     */
    public Rendition getAudioRenditionByLanguage(String language) {
        for (Rendition rendition : mAudioRenditionList) {
            if (rendition.getLanguage().equals(language)) {
                return rendition;
            }
        }

        return null;
    }

    /**
     * 是否包含（可替代的）视频展示
     */
    public boolean containsVideoRenditions() {
        return mStreamInfTag.containsAttribute(Attribute.Name.VIDEO);
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
     * 是否包含（可替代的）字幕展示
     */
    public boolean containsSubtitleRenditions() {
        return mStreamInfTag.containsAttribute(Attribute.Name.SUBTITLES);
    }

    /**
     * 获取（可替代的）字幕展示
     */
    public Rendition[] getSubtitleRenditions() {
        return mSubtitleRenditionList.toArray(
                new Rendition[mSubtitleRenditionList.size()]);
    }

    /**
     * 获取默认的字幕展示
     */
    public Rendition getDefaultSubtitleRendition() {
        for (Rendition rendition : mSubtitleRenditionList) {
            if (rendition.isDefaultSelection()) {
                return rendition;
            }
        }

        return null;
    }

    /**
     * 获取与指定语言环境匹配的字幕展示
     */
    public Rendition getSubtitleRenditionByLanguage(String language) {
        for (Rendition rendition : mSubtitleRenditionList) {
            if (rendition.getLanguage().equals(language)) {
                return rendition;
            }
        }

        return null;
    }

    /**
     * 是否包含（可替代的）隱藏字幕展示
     */
    public boolean containsClosedCaptionRenditions() {
        return mStreamInfTag.containsAttribute(Attribute.Name.CLOSED_CAPTIONS);
    }

    /**
     * 获取（可替代的）隱藏字幕展示
     */
    public Rendition[] getClosedCaptionRenditions() {
        return mClosedCaptionRenditionList.toArray(
                new Rendition[mClosedCaptionRenditionList.size()]);
    }

    /**
     * 获取默认的隱藏字幕展示
     */
    public Rendition getDefaultClosedCaptionRendition() {
        for (Rendition rendition : mClosedCaptionRenditionList) {
            if (rendition.isDefaultSelection()) {
                return rendition;
            }
        }

        return null;
    }

    /**
     * 获取与指定语言环境匹配的隱藏字幕展示
     */
    public Rendition getClosedCaptionRenditionByLanguage(String language) {
        for (Rendition rendition : mClosedCaptionRenditionList) {
            if (rendition.getLanguage().equals(language)) {
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
                UrlHelper.makeUrl(mPlaylistUrl, mUri), null);

        return null;
    }

    @Override
    public int compareTo(Integer bandwidth) {
        return getBandwidth() - bandwidth;
    }
}
