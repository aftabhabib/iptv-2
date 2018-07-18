package com.source;

import java.util.List;

public class ChannelGroup {
    private GroupInfo mInfo;
    private List<Channel> mChannelList;

    public ChannelGroup(GroupInfo info, List<Channel> channelList) {
        mInfo = info;
        mChannelList = channelList;
    }

    public String getName() {
        return mInfo.getName();
    }

    public List<Channel> getChannelList() {
        return mChannelList;
    }
}
