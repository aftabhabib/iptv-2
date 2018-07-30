package com.iptv.core.player.source.hls.playlist;

import java.util.ArrayList;
import java.util.List;

public class RenditionGroup {
    private List<Media> mMediaList;

    public RenditionGroup() {
        mMediaList = new ArrayList<Media>();
    }

    public void addMedia(Media media) {
        mMediaList.add(media);
    }

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
}
