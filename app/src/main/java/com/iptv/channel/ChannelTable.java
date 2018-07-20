package com.iptv.channel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * 频道表
 */
public class ChannelTable implements Parcelable {
    private static final String TAG = "ChannelTable";

    private List<ChannelGroup> mGroupList;

    public ChannelTable() {
        mGroupList = new LinkedList<ChannelGroup>();
    }

    protected ChannelTable(Parcel in) {
        in.readTypedList(mGroupList, ChannelGroup.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(mGroupList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChannelTable> CREATOR = new Creator<ChannelTable>() {
        @Override
        public ChannelTable createFromParcel(Parcel in) {
            return new ChannelTable(in);
        }

        @Override
        public ChannelTable[] newArray(int size) {
            return new ChannelTable[size];
        }
    };

    public List<ChannelGroup> getGroupList() {
        return mGroupList;
    }

    public int getGroupCount() {
        return mGroupList.size();
    }

    public ChannelGroup getGroup(int index) {
        return mGroupList.get(index);
    }

    public void addGroup(ChannelGroup group) {
        mGroupList.add(group);
    }

    public static ChannelTable create(List<Channel> channelList, List<ChannelGroup.GroupInfo> groupInfoList) {
        ChannelTable channelTable = new ChannelTable();

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

            channelTable.addGroup(group);
        }

        return channelTable;
    }
}
