package com.iptv.core.hls.playlist.rendition;

import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.datatype.EnumeratedString;
import com.iptv.core.hls.playlist.tag.MediaTag;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示组
 */
public final class RenditionGroup {
    private List<MediaTag> mTagList = new ArrayList<>();

    /**
     * 构造函数
     */
    public RenditionGroup() {
        /**
         * nothing
         */
    }

    /**
     * 加入展示
     */
    public void add(MediaTag tag) {
        mTagList.add(tag);
    }

    /**
     * 获取默认的展示
     */
    public MediaTag getDefaultRendition() {
        for (MediaTag tag : mTagList) {
            if (tag.containsAttribute(Attribute.Name.DEFAULT)
                    && tag.getDefaultSelect().equals(EnumeratedString.YES)) {
                return tag;
            }
        }

        return null;
    }

    /**
     * 获取与语言环境匹配的展示
     */
    public MediaTag getRenditionByLanguage(String language) {
        for (MediaTag tag : mTagList) {
            if (tag.containsAttribute(Attribute.Name.LANGUAGE)
                    && tag.getLanguage().equals(language)) {
                return tag;
            }
        }

        return null;
    }
}
