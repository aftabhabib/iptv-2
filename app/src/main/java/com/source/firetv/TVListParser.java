package com.source.firetv;

import android.util.Xml;

import com.iptv.demo.channel.Channel;
import com.iptv.demo.channel.ChannelGroup;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

final class TVListParser {
    private static final String XML_TAG_TVLISTS = "tvlists";
    private static final String XML_TAG_TVLIST = "tvlist";
    private static final String XML_TAG_TVNAME = "tvname";
    private static final String XML_TAG_TVADDR = "tvaddr";

    private static final String XML_ATTR_ID = "id";

    public static List<Channel> parse(File file) {
        List<Channel> channels;

        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            channels = parseXml(input);
        }
        catch (IOException e) {
            channels = null;
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                }
                catch (IOException e) {
                    //ignore
                }
            }
        }

        return channels;
    }

    private static List<Channel> parseXml(InputStream input) throws IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(input, "utf-8");

            parser.nextTag();
            return readChannels(parser);
        }
        catch (XmlPullParserException e) {
            throw new IOException("parse fail");
        }
    }

    private static List<Channel> readChannels(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, XML_TAG_TVLISTS);

        List<Channel> channels = new LinkedList<Channel>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tagName = parser.getName();
            if (tagName.equals(XML_TAG_TVLIST)) {
                Channel channel = readChannel(parser);
                channels.add(channel);
            }
            else {
                skip(parser);
            }
        }

        return channels;
    }

    private static Channel readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, XML_TAG_TVLIST);

        Channel channel = new Channel();

        String value = parser.getAttributeValue(null, XML_ATTR_ID);
        if (value.contains("|")) {
            String[] results = value.split("|");

            for (int i = 0; i < results.length; i++) {
                channel.addGroupId(results[i]);
            }
        }
        else {
            channel.addGroupId(value);
        }

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tagName = parser.getName();
            if (tagName.equals(XML_TAG_TVNAME)) {
                String text = readName(parser);

                channel.setName(text);
            }
            else if (tagName.equals(XML_TAG_TVADDR)) {
                String text = readSource(parser);

                if (text.contains("|")) {
                    String[] results = text.split("|");

                    for (int i = 0; i < results.length; i++) {
                        channel.addSource(results[i]);
                    }
                }
                else {
                    channel.addSource(text);
                }
            }
            else {
                skip(parser);
            }
        }

        return channel;
    }

    private static String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, XML_TAG_TVNAME);

        String name = "";
        if (parser.next() == XmlPullParser.TEXT) {
            name = parser.getText().trim();
            parser.nextTag();
        }

        parser.require(XmlPullParser.END_TAG, null, XML_TAG_TVNAME);

        return name;
    }

    private static String readSource(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, XML_TAG_TVADDR);

        String source = "";
        if (parser.next() == XmlPullParser.TEXT) {
            source = parser.getText().trim();
            parser.nextTag();
        }

        parser.require(XmlPullParser.END_TAG, null, XML_TAG_TVADDR);

        return source;
    }

    private static void skip(XmlPullParser parser) throws IOException, XmlPullParserException {
        int depth = 1;

        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG: {
                    depth--;
                    break;
                }
                case XmlPullParser.START_TAG: {
                    depth++;
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    public static List<ChannelGroup.GroupInfo> getGroupInfoList() {
        final String[] GROUP_NAME = {
                "央视频道",
                "卫视频道",
                "高清频道",
                "数字频道",
                "体育频道",
                "新闻频道",
                "影视频道",
                "少儿频道",
                "网络频道1",
                "网络频道2",
                "港台频道",
                "海外频道",
                "测试频道",
                "北京",
                "上海",
                "天津",
                "重庆",
                "黑龙江",
                "吉林",
                "辽宁",
                "新疆",
                "甘肃",
                "宁夏",
                "青海",
                "陕西",
                "山西",
                "河南",
                "河北",
                "安徽",
                "山东",
                "江苏",
                "浙江",
                "福建",
                "广东",
                "广西",
                "云南",
                "湖南",
                "湖北",
                "江西",
                "海南",
                "四川",
                "贵州",
                "西藏",
                "内蒙古",
                "自定义频道",
                "已收藏频道",
                "临时频道"
        };

        List<ChannelGroup.GroupInfo> groupInfoList = new ArrayList<ChannelGroup.GroupInfo>(GROUP_NAME.length);

        for (int i = 0; i < GROUP_NAME.length; i++) {
            groupInfoList.add(new ChannelGroup.GroupInfo(String.valueOf(i + 1), GROUP_NAME[i]));
        }

        return groupInfoList;
    }
}
