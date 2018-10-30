package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.datatype.EnumeratedString;
import com.iptv.core.hls.playlist.tag.MediaTag;
import com.iptv.core.hls.utils.UrlHelper;

import java.io.IOException;

/**
 * 展示
 */
public final class Rendition {
    private MediaTag mMediaTag;

    /**
     * 构造函数
     */
    public Rendition(MediaTag mediaTag) {
        mMediaTag = mediaTag;
    }

    /**
     * 是不是音频（展示）
     */
    public boolean isAudio() {
        return mMediaTag.getType().equals(EnumeratedString.AUDIO);
    }

    /**
     * 是不是视频（展示）
     */
    public boolean isVideo() {
        return mMediaTag.getType().equals(EnumeratedString.VIDEO);
    }

    /**
     * 是不是字幕（展示）
     */
    public boolean isSubtitle() {
        return mMediaTag.getType().equals(EnumeratedString.SUBTITLES);
    }

    /**
     * 是不是隐藏字幕（展示）
     */
    public boolean isClosedCaption() {
        return mMediaTag.getType().equals(EnumeratedString.CLOSED_CAPTIONS);
    }

    /**
     * 获取所属（展示）组的id
     */
    public String getGroupId() {
        return mMediaTag.getGroupId();
    }

    /**
     * 是不是默认的选择
     */
    public boolean isDefaultSelection() {
        String state;
        if (mMediaTag.containsAttribute(Attribute.Name.DEFAULT)) {
            state = mMediaTag.getDefaultSelection();
        }
        else {
            state = EnumeratedString.NO;
        }

        return state.equals(EnumeratedString.YES);
    }

    /**
     * 获取语言（如果有定义，否则即为未知）
     */
    public String getLanguage() {
        if (mMediaTag.containsAttribute(Attribute.Name.LANGUAGE)) {
            return mMediaTag.getLanguage();
        }
        else {
            return "und";
        }
    }

    /**
     * 获取隐藏字幕（在媒体数据中）的id
     */
    public String getClosedCaptionId() {
        if (!isClosedCaption()) {
            throw new IllegalStateException("TYPE should be CLOSED-CAPTIONS");
        }

        return mMediaTag.getInStreamId();
    }

    /**
     * 是否定义了源
     */
    public boolean containsSource() {
        return mMediaTag.containsAttribute(Attribute.Name.URI);
    }

    /**
     * 获取媒体播放列表
     */
    public MediaPlaylist getMediaPlaylist(String baseUri) throws IOException {
        if (!containsSource()) {
            throw new IllegalStateException("media data is included in the playlist of stream " +
                    "referencing this rendition");
        }

        Playlist playlist = Playlist.load(
                UrlHelper.makeUrl(baseUri, mMediaTag.getUri()), null);
        if (playlist.getType() != Playlist.TYPE_MEDIA) {
            throw new IllegalStateException("should be media playlist");
        }

        return (MediaPlaylist)playlist;
    }
}
