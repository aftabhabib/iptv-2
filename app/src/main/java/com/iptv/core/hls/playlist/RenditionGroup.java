package com.iptv.core.hls.playlist;

import java.util.ArrayList;
import java.util.List;

public final class RenditionGroup {
    private List<Media> mMediaList;

    public RenditionGroup() {
        mMediaList = new ArrayList<Media>();
    }

    /**
     * 添加媒体
     */
    public void addMedia(Media media) {
        mMediaList.add(media);
    }

    /**
     * 获取默认的表现
     */
    public Media getDefaultRendition() {
        Media media = null;

        for (int i = 0; i < mMediaList.size(); i++) {
            media = mMediaList.get(i);
            if (media.isDefault()) {
                break;
            }
        }

        return media;
    }

    /**
     * 获取对应语言的表现
     */
    public Media getRenditionByLanguage(String language) {
        Media media = null;

        for (int i = 0; i < mMediaList.size(); i++) {
            media = mMediaList.get(i);
            if (media.containsLanguage()
                    && media.getLanguage().equals(language)) {
                break;
            }
        }

        return media;
    }
}
