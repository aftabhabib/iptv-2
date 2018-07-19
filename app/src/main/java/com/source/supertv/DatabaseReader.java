package com.source.supertv;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.iptv.demo.channel.Channel;
import com.iptv.demo.channel.ChannelGroup;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class DatabaseReader {
    private static final String TAG = "DatabaseReader";

    private SQLiteDatabase mSqlite;

    private DatabaseReader(SQLiteDatabase sqlite) {
        mSqlite = sqlite;
    }

    public List<ChannelGroup.GroupInfo> getGroupInfoList() {
        List<ChannelGroup.GroupInfo> groupInfoList = new LinkedList<ChannelGroup.GroupInfo>();

        Cursor cursor = mSqlite.rawQuery("select * from tvitem order by num asc", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));

                ChannelGroup.GroupInfo groupInfo = new ChannelGroup.GroupInfo(String.valueOf(id), name);
                groupInfoList.add(groupInfo);
            }

            cursor.close();

            /**
             * 手动添加地区分组
             */
            addAreaGroupInfo(groupInfoList);
        }
        else {
            Log.e(TAG, "query tvitem table fail");
        }

        return groupInfoList;
    }

    private void addAreaGroupInfo(List<ChannelGroup.GroupInfo> groupInfoList) {
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

        for (int i = 0; i < AREA_NAME.length; i++) {
            ChannelGroup.GroupInfo groupInfo = new ChannelGroup.GroupInfo(AREA_NAME[i], AREA_NAME[i]);
            groupInfoList.add(groupInfo);
        }
    }

    public List<Channel> getChannelList() {
        List<Channel> channelList = new LinkedList<Channel>();

        Cursor cursor = mSqlite.rawQuery("select * from tvdata order by num asc", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Channel channel = new Channel();

                String title = cursor.getString(cursor.getColumnIndex("title"));
                channel.setName(title);

                String itemId = cursor.getString(cursor.getColumnIndex("itemid"));
                if (!itemId.isEmpty()) {
                    /**
                     * 对应tvitem表中的id
                     */
                    Matcher matcher = Pattern.compile("\\d+").matcher(itemId);

                    while (matcher.find()) {
                        String id = matcher.group();
                        channel.addGroupId(id);
                    }
                }

                String area = cursor.getString(cursor.getColumnIndex("area"));
                if (!area.isEmpty()) {
                    /**
                     * 对应地区分组
                     */
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
        }
        else {
            Log.e(TAG, "query tvdata table fail");
        }

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

        /**
         * 更新数据库
         */
        update(sqlite);

        return new DatabaseReader(sqlite);
    }

    private static void update(SQLiteDatabase sqlite) {
        /**
         * 删除，港澳台频道不再有效
         */
        sqlite.execSQL("DELETE FROM tvitem WHERE cid=2048");
        sqlite.execSQL("DELETE FROM tvdata WHERE cid&2048>=2048");

        /**
         * 删除，电影抢先看频道
         */
        sqlite.execSQL("DELETE FROM tvitem WHERE cid=4096");
        sqlite.execSQL("DELETE FROM tvdata WHERE cid&4096>=4096");
    }
}
