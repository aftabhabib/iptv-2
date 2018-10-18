package com.iptv.core.hls.playlist.rendition;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示组
 */
public final class RenditionGroup {
    private List<Rendition> mRenditionList = new ArrayList<>();

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
    public void add(Rendition rendition) {
        mRenditionList.add(rendition);
    }

    /**
     * 获取默认的展示
     */
    public Rendition getDefaultRendition() {
        for (Rendition rendition : mRenditionList) {
            if (rendition.isDefaultSelection()) {
                return rendition;
            }
        }

        return null;
    }

    /**
     * 获取与语言环境匹配的展示
     */
    public Rendition getRenditionByLanguage(String language) {
        for (Rendition rendition : mRenditionList) {
            if (rendition.getLanguage().equals(language)) {
                return rendition;
            }
        }

        return null;
    }
}
