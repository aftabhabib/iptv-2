package com.source.supertv;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

import com.source.BaseClient;
import com.source.Channel;
import com.source.GroupInfo;
import com.utils.GzipHelper;
import com.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 超级直播
 */
public final class SuperTVClient extends BaseClient {
    private static final String TAG = "SuperTVClient";

    private static final String CONFIG_URL = "http://119.29.74.92:8123/config.json";
    private static final String IPTV_URL = "https://119.29.74.92/iptv_auth?area=%s";

    private static final String LIST_GZIP_NAME = "list.gz";
    private static final String LIVE_DB_NAME = "live.db";

    private static final long DEFAULT_DB_UPTIME = 0;
    private static final String SETTINGS_DB_UPTIME = "db_uptime";

    private static final String IPTV_GROUP = "IPTV";

    private File mDataDir;
    private SharedPreferences mSettings;

    private SuperTVConfig mConfig;
    private List<Channel> mChannelList;
    private List<GroupInfo> mGroupList;

    public SuperTVClient(Context context, Looper looper) {
        super(looper);

        mDataDir = new File(context.getFilesDir(), "supertv");
        if (!mDataDir.exists()) {
            mDataDir.mkdir();
        }

        mSettings = context.getSharedPreferences("supertv", 0);
    }

    @Override
    protected void onSetup() {
        if (!prepareConfig()) {
            mListener.onError("setup fail");
            return;
        }

        if (!prepareChannelTable()) {
            mListener.onError("setup fail");
            return;
        }

        mListener.onSetup(mChannelList, mGroupList);
    }

    private boolean prepareConfig() {
        Map<String, String> property = new HashMap<String, String>();
        property.put("User-Agent", "lgsg/1.0");

        /**
         * 获取参数
         */
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

        return true;
    }

    private boolean prepareChannelTable() {
        File gzFile = new File(mDataDir, LIST_GZIP_NAME);
        File dbFile = new File(mDataDir, LIVE_DB_NAME);

        if (isDatabaseExpired()) {
            /**
             * 数据库过期，重新下载
             */
            Map<String, String> property = new HashMap<String, String>();
            property.put("User-Agent", "lgsg/1.0");

            if (!HttpHelper.opDownload(mConfig.getDatabaseUrl(), property, gzFile)) {
                Log.e(TAG, "download " + LIST_GZIP_NAME + " fail");
                return false;
            }

            /**
             * 解压
             */
            if (!GzipHelper.extract(gzFile, dbFile)) {
                Log.e(TAG, "extract " + LIST_GZIP_NAME + " fail");
                return false;
            }

            /**
             * 更新本地的版本信息
             */
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putLong(SETTINGS_DB_UPTIME, mConfig.getDatabaseUptime());
            editor.commit();
        }

        /**
         * 解析频道数据
         */
        DatabaseReader reader = DatabaseReader.create(dbFile);
        if (reader == null) {
            Log.e(TAG, "open " + LIVE_DB_NAME + " fail");
            return false;
        }

        mChannelList = reader.readChannels();
        mGroupList = reader.readGroups();
        reader.release();

        /**
         * IPTV频道
         */
        String area = getArea();
        if (!area.isEmpty()) {
            List<Channel> iptvList = getIptvList(area);
            if (!iptvList.isEmpty()) {
                mChannelList.addAll(iptvList);
                mGroupList.add(new GroupInfo(IPTV_GROUP, IPTV_GROUP));
            }
        }

        return true;
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

    private static String getArea() {
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

    private List<Channel> getIptvList(String area) {
        List<Channel> iptvList = new LinkedList<Channel>();

        File gzFile = new File(mDataDir, "iptv.gz");
        File txtFile = new File(mDataDir, "iptv.txt");
        try {
            Map<String, String> property = new HashMap<String, String>();
            property.put("User-Agent", "lgsg/1.0");

            if (!HttpHelper.opDownload(String.format(IPTV_URL, URLEncoder.encode(area, "UTF-8")), property, gzFile)) {
                throw new IOException("download " + gzFile.getName() + " fail");
            }

            if (!GzipHelper.extract(gzFile, txtFile)) {
                throw new IOException("extract " + txtFile.getName() + " fail");
            }

            BufferedReader reader = new BufferedReader(new FileReader(txtFile));
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

                iptvList.add(channel);
            }
            reader.close();
        }
        catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        finally {
            if (txtFile.exists()) {
                txtFile.delete();
            }

            if (gzFile.exists()) {
                gzFile.delete();
            }
        }

        return iptvList;
    }

    @Override
    protected void onDecodeSource(String source) {
        //TODO
    }
}
