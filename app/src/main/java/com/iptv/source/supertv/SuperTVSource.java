package com.iptv.source.supertv;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.iptv.channel.ChannelGroup;
import com.iptv.channel.ChannelTable;
import com.iptv.channel.Channel;
import com.iptv.plugin.Plugin;
import com.iptv.source.Source;
import com.iptv.utils.GzipHelper;
import com.iptv.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SuperTVSource implements Source {
    private static final String TAG = "SuperTVSource";

    private static final String CONFIG_URL = "http://119.29.74.92:8123/config.json";
    private static final String IPTV_URL_FORMULAR = "https://119.29.74.92/iptv_auth?area=%s";

    private static final String LIVE_DB_NAME = "live.db";
    private static final String IPTV_TXT_NAME = "iptv.txt";

    private static final long DEFAULT_DB_UPTIME = 0;
    private static final String SETTINGS_DB_UPTIME = "db_uptime";

    private File mDataDir;
    private SharedPreferences mSettings;

    private SuperTVConfig mConfig;
    private ChannelTable mChannelTable;
    private List<Plugin> mPluginList;

    public SuperTVSource(Context context) {
        mDataDir = new File(context.getFilesDir(), "supertv");
        if (!mDataDir.exists()) {
            mDataDir.mkdir();
        }

        mSettings = context.getSharedPreferences("supertv", 0);
    }

    @Override
    public String getName() {
        return "超级直播";
    }

    @Override
    public boolean setup() {
        if (!prepareConfig()) {
            return false;
        }

        if (!prepareChannelTable()) {
            return false;
        }

        preparePlugin();

        return true;
    }

    @Override
    public ChannelTable getChannelTable() {
        return mChannelTable;
    }

    @Override
    public Map<String, String> decodeSource(String source) {
        String url = source;
        Map<String, String> parameters = new HashMap<String, String>();

        /**
         * TODO：源代码中的处理过程
         */

        parameters.put("url", url);

        return parameters;
    }

    @Override
    public void release() {
        /**
         * TODO：释放资源
         */
    }

    private boolean prepareConfig() {
        Map<String, String> property = new HashMap<String, String>();
        property.put("User-Agent", "lgsg/1.0");

        byte[] content = HttpHelper.opGet(CONFIG_URL, property);
        if (content == null) {
            Log.e(TAG, "get config.json fail");
            return false;
        }

        try {
            mConfig = SuperTVConfig.parse(new String(content, "UTF-8"));
        }
        catch (UnsupportedEncodingException e) {
            //ignore
        }

        return mConfig != null;
    }

    private boolean prepareChannelTable() {
        File dbFile = new File(mDataDir, LIVE_DB_NAME);

        if (isDatabaseExpired()) {
            /**
             * 过期，重新下载
             */
            if (!downloadDatabase(dbFile)) {
                Log.e(TAG, "download " + dbFile.getName() + " fail");
                return false;
            }

            /**
             * 更新本地的记录
             */
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putLong(SETTINGS_DB_UPTIME, mConfig.getDatabaseUptime());
            editor.commit();
        }

        return readDatabase(dbFile);
    }

    private boolean isDatabaseExpired() {
        long dbUptime = mSettings.getLong(SETTINGS_DB_UPTIME, DEFAULT_DB_UPTIME);
        if (dbUptime < mConfig.getDatabaseUptime()) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean downloadDatabase(File dbFile) {
        Map<String, String> property = new HashMap<String, String>();
        property.put("User-Agent", "lgsg/1.0");

        byte[] content = HttpHelper.opGet(mConfig.getDatabaseUrl(), property);
        if (content == null) {
            Log.e(TAG, "get " + mConfig.getDatabaseUrl() + " fail");
            return false;
        }

        if (!GzipHelper.extract(content, dbFile)) {
            Log.e(TAG, "extract to " + dbFile.getName() + " fail");
            return false;
        }

        return true;
    }

    private boolean readDatabase(File dbFile) {
        DatabaseReader reader = DatabaseReader.create(dbFile);
        if (reader == null) {
            Log.e(TAG, "read " + dbFile.getName() + " fail");
            return false;
        }

        List<Channel> channelList = reader.getChannelList();
        List<ChannelGroup.GroupInfo> groupInfoList = reader.getGroupInfoList();
        mChannelTable = ChannelTable.create(channelList, groupInfoList);
        mChannelTable.addGroup(getIPTVGroup());

        reader.release();
        return true;
    }

    private ChannelGroup getIPTVGroup() {
        ChannelGroup group = new ChannelGroup(IPTVListParser.getGroupInfo());

        File txtFile = new File(mDataDir, IPTV_TXT_NAME);
        if (!txtFile.exists()) {
            /**
             * 不存在，下载
             */
            if (!downloadIPTVList(txtFile)) {
                Log.e(TAG, "download " + txtFile.getName() + " fail");
            }
            else {
                group.addChannels(IPTVListParser.parse(txtFile));
            }
        }
        else {
            /**
             * IPTV列表文件没有版本或时间信息
             */
            group.addChannels(IPTVListParser.parse(txtFile));
        }

        return group;
    }

    private static boolean downloadIPTVList(File txtFile) {
        String url = getIPTVUrl();

        Map<String, String> property = new HashMap<String, String>();
        property.put("User-Agent", "lgsg/1.0");

        byte[] content = HttpHelper.opGet(url, property);
        if (content == null) {
            Log.e(TAG, "get " + url + " fail");
            return false;
        }

        if (!GzipHelper.extract(content, txtFile)) {
            Log.e(TAG, "extract to " + txtFile.getName() + " fail");
            return false;
        }

        return true;
    }

    private static String getIPTVUrl() {
        String url = "";

        String area = getAreaByUserIp();
        if (area.isEmpty()) {
            Log.e(TAG, "can not get area by user ip");
        }
        else {
            try {
                url = String.format(IPTV_URL_FORMULAR, URLEncoder.encode(area, "UTF-8"));
            }
            catch (UnsupportedEncodingException e) {
                //ignore
            }
        }

        return url;
    }

    private static String getAreaByUserIp() {
        String area = "";

        byte[] content = HttpHelper.opGet("https://g3.le.com/r?format=1", null);
        if (content == null) {
            Log.e(TAG, "get le json fail");
        }
        else {
            try {
                JSONObject rootObj = new JSONObject(new String(content));

                area = rootObj.getString("desc");
            }
            catch (JSONException e) {
                Log.e(TAG, "parse le json fail");
            }
        }

        return area;
    }

    private void preparePlugin() {
        mPluginList = new ArrayList<Plugin>(15);
    }
}
