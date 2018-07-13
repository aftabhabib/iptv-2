package com.source.supertv;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.source.Channel;
import com.source.GroupInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class DatabaseReader {
    private SQLiteDatabase mSqlite;

    private DatabaseReader(SQLiteDatabase sqlite) {
        mSqlite = sqlite;
    }

    public List<GroupInfo> readGroups() {
        List<GroupInfo> groupList = new ArrayList<GroupInfo>(50);

        /**
         * 特征分组
         */
        Cursor cursor = mSqlite.rawQuery("select * from tvitem order by num asc", null);
        if (cursor == null) {
            throw new IllegalStateException("query tvitem table fail");
        }

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));

            GroupInfo group = new GroupInfo(String.valueOf(id), name);
            groupList.add(group);
        }

        cursor.close();

        /**
         * 地区分组
         */
        groupList.addAll(getAreaGroups());

        return groupList;
    }

    private static List<GroupInfo> getAreaGroups() {
        final String[] AREA_NAME = {
                "北京",
                "天津",
                "河北",
                "山西",
                "辽宁",
                "吉林",
                "黑龙江",
                "江苏",
                "浙江",
                "安徽",
                "重庆",
                "宁夏",
                "上海",
                "福建",
                "江西",
                "山东",
                "河南",
                "湖北",
                "湖南",
                "广东",
                "广西",
                "海南",
                "四川",
                "贵州",
                "云南",
                "陕西",
                "甘肃",
                "青海",
                "内蒙古",
                "西藏",
                "新疆"
        };

        List<GroupInfo> groupList = new ArrayList<GroupInfo>(AREA_NAME.length);

        for (int i = 0; i < AREA_NAME.length; i++) {
            GroupInfo group = new GroupInfo(AREA_NAME[i], AREA_NAME[i]);
            groupList.add(group);
        }

        return groupList;
    }

    public List<Channel> readChannels() {
        List<Channel> channelList = new LinkedList<Channel>();

        Cursor cursor = mSqlite.rawQuery("select * from tvdata order by num asc", null);
        if (cursor == null) {
            throw new IllegalStateException("query tvdata table fail");
        }

        while (cursor.moveToNext()) {
            Channel channel = new Channel();

            String title = cursor.getString(cursor.getColumnIndex("title"));
            channel.setName(title);

            String itemId = cursor.getString(cursor.getColumnIndex("itemid"));
            if (!itemId.isEmpty()) {
                Matcher matcher = Pattern.compile("\\d+").matcher(itemId);

                while (matcher.find()) {
                    String id = matcher.group();
                    channel.addGroupId(id);
                }
            }

            String area = cursor.getString(cursor.getColumnIndex("area"));
            if (!area.isEmpty()) {
                channel.addGroupId(area);
            }

            String url = cursor.getString(cursor.getColumnIndex("url"));
            if (url.contains("#")) {
                String[] results = url.split("#");

                for (int i = 0; i < results.length; i++) {
                    channel.addSource(results[i]);
                }
            }
            else {
                channel.addSource(url);
            }

            channelList.add(channel);
        }

        cursor.close();

        return channelList;
    }

    public void release() {
        if (mSqlite != null) {
            mSqlite.close();
        }
    }

    public static DatabaseReader create(File file) {
        SQLiteDatabase sqlite = SQLiteDatabase.openDatabase(file.getPath(), null, SQLiteDatabase.OPEN_READONLY);
        if (sqlite == null) {
            return null;
        }

        return new DatabaseReader(sqlite);
    }
}
