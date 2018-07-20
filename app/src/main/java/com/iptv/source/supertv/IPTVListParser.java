package com.iptv.source.supertv;

import android.util.Log;

import com.iptv.channel.Channel;
import com.iptv.channel.ChannelGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

final class IPTVListParser {
    private static final String TAG = "IPTVListParser";

    private static final String IPTV_GROUP = "IPTV";

    public static List<Channel> parse(File file) {
        List<Channel> channelList = new LinkedList<Channel>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                String[] results = line.split(",");

                Channel channel = new Channel();
                channel.addGroupId(IPTV_GROUP);
                channel.setName(results[0]);
                channel.addSource(results[1]);
            }
        }
        catch (IOException e) {
            Log.e(TAG, "parse " + file.getName() + " fail");
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    //ignore
                }
            }
        }

        return channelList;
    }

    public static ChannelGroup.GroupInfo getGroupInfo() {
        return new ChannelGroup.GroupInfo(IPTV_GROUP, IPTV_GROUP);
    }
}
