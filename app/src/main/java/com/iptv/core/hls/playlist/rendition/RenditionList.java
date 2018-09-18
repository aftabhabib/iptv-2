package com.iptv.core.hls.playlist.rendition;

import com.iptv.core.hls.playlist.Media;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 展示列表
 */
public final class RenditionList {
    private Map<String, List<Media>> mGroupTable = new HashMap<>();

    /**
     * 构造函数
     */
    public RenditionList() {
        /**
         * nothing
         */
    }

    /**
     * 是否为空
     */
    public boolean isEmpty() {
        return mGroupTable.isEmpty();
    }

    /**
     * 放入展示
     */
    public void put(Media rendition) {
        String key = makeKey(rendition.getType(), rendition.getGroupId());

        if (!mGroupTable.containsKey(key)) {
            mGroupTable.put(key, new ArrayList<Media>());
        }

        mGroupTable.get(key).add(rendition);
    }

    /**
     * 获取指定的（展示）组
     */
    public Media[] getGroup(String type, String groupId) {
        String key = makeKey(type, groupId);

        if (!mGroupTable.containsKey(key)) {
            return null;
        }
        else {
            List<Media> group = mGroupTable.get(key);
            return group.toArray(new Media[group.size()]);
        }
    }

    /**
     * 生成（展示）组的定义
     */
    private static String makeKey(String type, String groupId) {
        return type + "-" + groupId;
    }
}
