package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.datatype.EnumeratedString;
import com.iptv.core.hls.playlist.tag.MediaTag;
import com.iptv.core.hls.utils.HttpHelper;
import com.iptv.core.hls.utils.UrlHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * 展示
 */
public final class Rendition {
    private String mPlaylistUrl;

    private MediaTag mMediaTag;

    /**
     * 构造函数
     */
    public Rendition(MediaTag mediaTag) {
        mMediaTag = mediaTag;
    }

    /**
     * 设置播放列表url
     */
    void setPlaylistUrl(String playlistUrl) {
        mPlaylistUrl = playlistUrl;
    }

    /**
     * 获取类型
     */
    public String getType() {
        return mMediaTag.getType();
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
        String state = EnumeratedString.NO;

        if (mMediaTag.containsAttribute(Attribute.Name.DEFAULT)) {
            state = mMediaTag.getDefaultSelection();
        }

        return state.equals(EnumeratedString.YES);
    }

    /**
     * 展示是不是在流里
     */
    public boolean isIncludedInStream() {
        return !mMediaTag.containsAttribute(Attribute.Name.URI);
    }

    /**
     * 获取语言（如果有定义，否则即为未知）
     */
    public String getLanguage() {
        String language = "und";

        if (mMediaTag.containsAttribute(Attribute.Name.LANGUAGE)) {
            language = mMediaTag.getLanguage();
        }

        return language;
    }

    /**
     * 获取媒体列表
     */
    public MediaPlaylist getPlaylist() throws IOException {
        InputStream input = HttpHelper.get(
                UrlHelper.makeUrl(mPlaylistUrl, mMediaTag.getUri()), null);

        return null;
    }

    /**
     * 获取隐藏字幕（在媒体数据中）的id
     */
    public String getClosedCaptionId() {
        return mMediaTag.getInStreamId();
    }
}
