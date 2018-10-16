package com.iptv.core.hls.playlist.rendition;

import com.iptv.core.hls.playlist.tag.MediaTag;

import java.util.HashMap;
import java.util.Map;

/**
 * 展示列表
 */
public final class RenditionList {
    private Map<String, RenditionGroup> mGroupTable = new HashMap<>();

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
    public void put(MediaTag rendition) {
        String groupSpec = makeGroupSpec(rendition.getType(), rendition.getGroupId());

        if (!mGroupTable.containsKey(groupSpec)) {
            mGroupTable.put(groupSpec, new RenditionGroup());
        }

        mGroupTable.get(groupSpec).add(rendition);
    }

    /**
     * 获取指定的（展示）组
     */
    public RenditionGroup getGroup(String type, String groupId) {
        String groupSpec = makeGroupSpec(type, groupId);

        if (!mGroupTable.containsKey(groupSpec)) {
            return null;
        }
        else {
            return mGroupTable.get(groupSpec);
        }
    }

    /**
     * 生成（展示）组的定义
     */
    private static String makeGroupSpec(String type, String groupId) {
        return type + "-" + groupId;
    }
}
