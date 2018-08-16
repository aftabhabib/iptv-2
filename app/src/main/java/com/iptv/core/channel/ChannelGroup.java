package com.iptv.core.channel;

import java.util.ArrayList;

/**
 * 频道组
 */
public class ChannelGroup {
    private String mName;
    private Channel[] mChannels;

    /**
     * 构造函数
     */
    private ChannelGroup(String name, Channel[] channels) {
        mName = name;
        mChannels = channels;
    }

    /**
     * 获取组名
     */
    public String getName() {
        return mName;
    }

    /**
     * 获取组内的频道
     */
    public Channel[] getChannels() {
        return mChannels;
    }

    /**
     * 创建分组
     */
    public static ChannelGroup create(String name, String id, Channel[] channels) {
        ArrayList<Channel> channelList = new ArrayList<Channel>();

        for (Channel channel : channels) {
            for (String groupId : channel.getGroupIds()) {
                if (groupId.equals(id)) {
                    channelList.add(channel);
                    break;
                }
            }
        }

        return new ChannelGroup(name,
                channelList.toArray(new Channel[channelList.size()]));
    }
}
