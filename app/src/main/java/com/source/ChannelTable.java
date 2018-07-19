package com.source;

import java.util.LinkedList;
import java.util.List;

public class ChannelTable {
    private List<ChannelGroup> mGroupList;

    public ChannelTable(List<Channel> channelList, List<GroupInfo> infoList) {
        mGroupList = new LinkedList<ChannelGroup>();

        for (int i = 0; i < infoList.size(); i++) {
            GroupInfo info = infoList.get(i);
            List<Channel> channels = getChannelsInGroup(info, channelList);

            mGroupList.add(new ChannelGroup(info, channels));
        }
    }

    private static List<Channel> getChannelsInGroup(GroupInfo info, List<Channel> channelList) {
        List<Channel> groupChannels = new LinkedList<Channel>();

        for (int i = 0; i < channelList.size(); i++) {
            Channel channel = channelList.get(i);

            for (String groupId : channel.getGroupIdList()) {
                if (info.getId().equals(groupId)) {
                    groupChannels.add(channel);
                    break;
                }
            }
        }

        return groupChannels;
    }

    public int getGroupCount() {
        return mGroupList.size();
    }

    public List<ChannelGroup> getGroupList() {
        return mGroupList;
    }

    public ChannelGroup getGroup(int index) {
        return mGroupList.get(index);
    }
}
