package com.iptv.core.channel;

import java.util.ArrayList;

/**
 * 频道信息
 */
public class Channel {
    private String mName;
    private String[] mSources;
    private String[] mGroupIds;

    /**
     * 构造函数
     */
    private Channel(String name, String[] sources, String[] groupIds) {
        mName = name;
        mSources = sources;
        mGroupIds = groupIds;
    }

    /**
     * 获取频道名称
     */
    public String getName() {
        return mName;
    }

    /**
     * 获取频道源
     */
    public String[] getSources() {
        return mSources;
    }

    /**
     * 获取分组id
     */
    public String[] getGroupIds() {
        return mGroupIds;
    }

    /**
     * 构造器
     */
    public static class Builder {
        private String mName;
        private ArrayList<String> mSourceList;
        private ArrayList<String> mGroupIdList;

        /**
         * 构造函数
         */
        public Builder() {
            mName = "未知";
            mSourceList = new ArrayList<String>();
            mGroupIdList = new ArrayList<String>();
        }

        /**
         * 设置频道名称
         */
        public void setName(String name) {
            mName = name;
        }

        /**
         * 增加源
         */
        public void addSource(String source) {
            mSourceList.add(source);
        }

        /**
         * 增加分组id
         */
        public void addGroupId(String groupId) {
            mGroupIdList.add(groupId);
        }

        /**
         * 创建频道
         */
        public Channel build() {
            return new Channel(mName,
                    mSourceList.toArray(new String[mSourceList.size()]),
                    mGroupIdList.toArray(new String[mGroupIdList.size()]));
        }
    }
}
