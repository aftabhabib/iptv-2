package com.iptv.core.source.firetv;

import android.util.Xml;

import com.iptv.core.channel.Channel;
import com.iptv.core.channel.ChannelGroup;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 解析tvlist.xml
 */
final class TVListParser {
    private static final String XML_TAG_TVLISTS = "tvlists";
    private static final String XML_TAG_TVLIST = "tvlist";
    private static final String XML_TAG_TVNAME = "tvname";
    private static final String XML_TAG_TVADDR = "tvaddr";

    private static final String XML_ATTR_ID = "id";

    private XmlPullParser mParser;

    /**
     * 构造函数
     */
    public TVListParser() {
        mParser = Xml.newPullParser();
    }

    /**
     * 读取频道列表
     */
    public List<Channel> readChannelList(InputStream input) throws IOException {
        List<Channel> channelList = new LinkedList<Channel>();

        try {
            mParser.setInput(input, "utf-8");

            /**
             * xml档案中只关注<tvlists></tvlists>标签
             */
            while (mParser.next() != XmlPullParser.END_DOCUMENT) {
                if (mParser.getEventType() == XmlPullParser.START_TAG) {
                    String tagName = mParser.getName();
                    if (tagName.equals(XML_TAG_TVLISTS)) {
                        parseTVLists(channelList);
                    }
                    else {
                        skipTag();
                    }
                }
            }
        }
        catch (XmlPullParserException e) {
            /**
             * ignore
             */
        }

        return channelList;
    }

    /**
     * 解析<tvlists></tvlists>标签里的内容
     */
    private void parseTVLists(List<Channel> channelList) throws XmlPullParserException, IOException {
        mParser.require(XmlPullParser.START_TAG, null, XML_TAG_TVLISTS);

        /**
         * 次一级标签里只关注<tvlist></tvlist>
         */
        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() == XmlPullParser.START_TAG) {
                String tagName = mParser.getName();
                if (tagName.equals(XML_TAG_TVLIST)) {
                    Channel.Builder builder = new Channel.Builder();

                    parseTVList(builder);

                    channelList.add(builder.build());
                }
                else {
                    skipTag();
                }
            }
        }

        mParser.require(XmlPullParser.END_TAG, null, XML_TAG_TVLISTS);
    }

    /**
     * 解析<tvlist></tvlist>标签里的内容
     */
    private void parseTVList(Channel.Builder builder) throws XmlPullParserException, IOException {
        mParser.require(XmlPullParser.START_TAG, null, XML_TAG_TVLIST);

        /**
         * 分组Id是标签的属性
         */
        String groupId = mParser.getAttributeValue(null, XML_ATTR_ID);
        if (groupId.contains("|")) {
            String[] results = groupId.split("|");

            for (int i = 0; i < results.length; i++) {
                builder.addGroupId(results[i]);
            }
        }
        else {
            builder.addGroupId(groupId);
        }

        /**
         * 次一级标签里只关注<tvname></tvname>和<tvaddr></tvaddr>
         */
        while (mParser.next() != XmlPullParser.END_TAG) {
            if (mParser.getEventType() == XmlPullParser.START_TAG) {
                String tagName = mParser.getName();
                if (tagName.equals(XML_TAG_TVNAME)) {
                    parseTVName(builder);
                }
                else if (tagName.equals(XML_TAG_TVADDR)) {
                    parseTVAddress(builder);
                }
                else {
                    skipTag();
                }
            }
        }

        mParser.require(XmlPullParser.END_TAG, null, XML_TAG_TVLIST);
    }

    /**
     * 解析<tvname></tvname>标签的内容
     */
    private void parseTVName(Channel.Builder builder) throws XmlPullParserException, IOException {
        mParser.require(XmlPullParser.START_TAG, null, XML_TAG_TVNAME);

        /**
         * 标签的内容应该是文本
         */
        if (mParser.next() == XmlPullParser.TEXT) {
            String name = mParser.getText();
            builder.setName(name);

            mParser.nextTag();
        }

        mParser.require(XmlPullParser.END_TAG, null, XML_TAG_TVNAME);
    }

    /**
     * 解析<tvaddr></tvaddr>标签的内容
     */
    private void parseTVAddress(Channel.Builder builder) throws XmlPullParserException, IOException {
        mParser.require(XmlPullParser.START_TAG, null, XML_TAG_TVADDR);

        /**
         * 标签的内容应该是文本
         */
        if (mParser.next() == XmlPullParser.TEXT) {
            String address = mParser.getText();

            if (address.contains("|")) {
                String[] results = address.split("|");

                for (int i = 0; i < results.length; i++) {
                    builder.addSource(results[i]);
                }
            }
            else {
                builder.addSource(address);
            }

            mParser.nextTag();
        }

        mParser.require(XmlPullParser.END_TAG, null, XML_TAG_TVADDR);
    }

    /**
     * 跳过标签（以及里面的内容）
     */
    private void skipTag() throws XmlPullParserException, IOException {
        int depth = 1;

        while (depth != 0) {
            switch (mParser.next()) {
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

    /**
     * 分组信息
     */
    private static final String[] GROUP_NAME = {
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

    /**
     * 频道分组
     */
    public List<ChannelGroup> groupChannels(Channel[] channels) {
        List<ChannelGroup> groupList = new ArrayList<ChannelGroup>(GROUP_NAME.length);

        for (int i = 0; i < GROUP_NAME.length; i++) {
            groupList.add(ChannelGroup.create(GROUP_NAME[i], String.valueOf(i + 1), channels));
        }

        return groupList;
    }
}
