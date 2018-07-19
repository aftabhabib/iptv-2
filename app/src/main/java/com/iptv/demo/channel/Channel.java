package com.iptv.demo.channel;

import java.util.ArrayList;
import java.util.List;

/**
 * 频道
 */
public class Channel {
    private String mName;
    private List<String> mSourceList;
    private List<String> mGroupIdList;

    public Channel() {
        mName = "";
        mSourceList = new ArrayList<String>(10);
        mGroupIdList = new ArrayList<String>(5);
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void addSource(String source) {
        mSourceList.add(source);
    }

    public List<String> getSourceList() {
        return mSourceList;
    }

    public void addGroupId(String groupId) {
        mGroupIdList.add(groupId);
    }

    public List<String> getGroupIdList() {
        return mGroupIdList;
    }
}
