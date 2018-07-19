package com.iptv.demo.channel;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * 频道表
 */
public class ChannelTable {
    private static final String TAG = "ChannelTable";

    private List<ChannelGroup> mGroupList;

    public ChannelTable(List<Channel> channelList, List<ChannelGroup.GroupInfo> groupInfoList) {
        mGroupList = new LinkedList<ChannelGroup>();

        for (int i = 0; i < groupInfoList.size(); i++) {
            ChannelGroup group = new ChannelGroup(groupInfoList.get(i));

            for (int j = 0; j < channelList.size(); j++) {
                Channel channel = channelList.get(i);

                boolean isHomeless = true;
                for (String groupId : channel.getGroupIdList()) {
                    if (group.getId().equals(groupId)) {
                        group.addChannel(channel);
                        isHomeless = false;

                        break;
                    }
                }

                if (isHomeless) {
                    Log.w(TAG, channel.getName() + " not belong to any group");
                }
            }

            mGroupList.add(group);
        }
    }

    public List<ChannelGroup> getGroupList() {
        return mGroupList;
    }

    public int getGroupCount() {
        return mGroupList.size();
    }

    public ChannelGroup getGroup(int index) {
        return mGroupList.get(index);
    }
}
