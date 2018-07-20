package com.iptv.channel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;
import java.util.List;

/**
 * 频道组
 */
public class ChannelGroup implements Parcelable {
    private GroupInfo mGroupInfo;
    private List<Channel> mChannelList;

    public ChannelGroup(GroupInfo groupInfo) {
        mGroupInfo = groupInfo;
        mChannelList = new LinkedList<Channel>();
    }

    protected ChannelGroup(Parcel in) {
        mGroupInfo = in.readParcelable(GroupInfo.class.getClassLoader());
        in.readTypedList(mChannelList, Channel.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeParcelable(mGroupInfo, flag);
        out.writeTypedList(mChannelList);
    }

    public static final Creator<ChannelGroup> CREATOR = new Creator<ChannelGroup>() {
        @Override
        public ChannelGroup createFromParcel(Parcel in) {
            return new ChannelGroup(in);
        }

        @Override
        public ChannelGroup[] newArray(int size) {
            return new ChannelGroup[size];
        }
    };

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
    public static class GroupInfo implements Parcelable {
        private String mId;
        private String mName;

        public GroupInfo(String id, String name) {
            mId = id;
            mName = name;
        }

        protected GroupInfo(Parcel in) {
            mId = in.readString();
            mName = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeString(mId);
            out.writeString(mName);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<GroupInfo> CREATOR = new Creator<GroupInfo>() {
            @Override
            public GroupInfo createFromParcel(Parcel in) {
                return new GroupInfo(in);
            }

            @Override
            public GroupInfo[] newArray(int size) {
                return new GroupInfo[size];
            }
        };

        public String getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }
    }
}
