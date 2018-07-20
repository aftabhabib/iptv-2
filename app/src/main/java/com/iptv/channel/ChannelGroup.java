package com.iptv.channel;

import java.util.LinkedList;
import java.util.List;

/**
 * 频道组
 */
public class ChannelGroup {
    private GroupInfo mGroupInfo;
    private List<Channel> mChannelList;

    public ChannelGroup(GroupInfo groupInfo) {
        mGroupInfo = groupInfo;
        mChannelList = new LinkedList<Channel>();
    }

    public String getId() {
        return mGroupInfo.getId();
    }

    public void addChannel(Channel channel) {
        mChannelList.add(channel);
    }

    public void addChannels(List<Channel> channels) {
        mChannelList.addAll(channels);
    }

    public String getName() {
        return mGroupInfo.getName();
    }

    public List<Channel> getChannelList() {
        return mChannelList;
    }

    public Channel getChannel(int index) {
        return mChannelList.get(index);
    }

    public int getChannelCount() {
        return mChannelList.size();
    }

    /**
     * 分组信息
     */
    public static class GroupInfo {
        private String mId;
        private String mName;

        public GroupInfo(String id, String name) {
            mId = id;
            mName = name;
        }

        public String getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }
    }
}
