package com.iptv.core.hls.playlist.rendition;

import com.iptv.core.hls.playlist.Media;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 展示列表（内部按组归并）
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
     * 生成展示组的定义
     */
    private static String makeName(String type, String groupId) {
        return type + "-" + groupId;
    }

    /**
     * 添加一个展示
     */
    public void addRendition(Media rendition) {
        if (!isValidRendition(rendition)) {
            throw new IllegalArgumentException("invalid rendition");
        }

        String name = makeName(rendition.getType(), rendition.getGroupId());

        if (!mGroupTable.containsKey(name)) {
            mGroupTable.put(name, new ArrayList<Media>());
        }

        mGroupTable.get(name).add(rendition);
    }

    /**
     * 是不是有效的展示
     */
    private static boolean isValidRendition(Media rendition) {
        return rendition.containsType() && rendition.containsGroupId();
    }

    /**
     * 获取指定（展示）组内的所有展示
     */
    public Media[] getRenditionsInGroup(String type, String groupId) {
        String name = makeName(type, groupId);

        if (!mGroupTable.containsKey(name)) {
            return null;
        }
        else {
            List<Media> group = mGroupTable.get(name);
            return group.toArray(new Media[group.size()]);
        }
    }
}
