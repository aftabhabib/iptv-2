package com.iptv.core.hls.playlist;

import java.util.ArrayList;
import java.util.List;

final class MediaGroup {
    private List<Media> mMediaList;

    public MediaGroup() {
        mMediaList = new ArrayList<Media>();
    }

    /**
     * 添加媒体
     */
    public void addMedia(Media media) {
        mMediaList.add(media);
    }

    /**
     * 获取默认的媒体
     */
    public Media getDefaultMedia() {
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
     * 获取对应语言的媒体
     */
    public Media getMediaByLanguage(String language) {
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
