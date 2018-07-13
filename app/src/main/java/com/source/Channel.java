package com.source;

import java.util.Vector;

public class Channel {
    private String mName;
    private Vector<String> mSources;
    private Vector<String> mGroupIds;

    public Channel() {
        mName = "";
        mSources = new Vector<String>(10);
        mGroupIds = new Vector<String>(5);
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void addSource(String source) {
        mSources.add(source);
    }

    public String[] getSource() {
        return mSources.toArray(new String[mSources.size()]);
    }

    public void addGroupId(String groupId) {
        mGroupIds.add(groupId);
    }

    public String[] getGroupId() {
        return mGroupIds.toArray(new String[mGroupIds.size()]);
    }
}
