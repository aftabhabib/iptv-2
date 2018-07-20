package com.iptv.channel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 频道
 */
public class Channel implements Parcelable {
    private String mName;
    private List<String> mSourceList;
    private List<String> mGroupIdList;

    public Channel() {
        mName = "";
        mSourceList = new ArrayList<String>(10);
        mGroupIdList = new ArrayList<String>(5);
    }

    protected Channel(Parcel in) {
        mName = in.readString();
        in.readStringList(mSourceList);
        in.readStringList(mGroupIdList);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mName);
        out.writeStringList(mSourceList);
        out.writeStringList(mGroupIdList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };

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

    public int getSourceCount() {
        return mSourceList.size();
    }

    public String getSource(int index) {
        return mSourceList.get(index);
    }

    public void addGroupId(String groupId) {
        mGroupIdList.add(groupId);
    }

    public List<String> getGroupIdList() {
        return mGroupIdList;
    }
}
