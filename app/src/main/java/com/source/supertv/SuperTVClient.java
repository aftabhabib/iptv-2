package com.source.supertv;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

import com.iptv.demo.channel.ChannelGroup;
import com.iptv.demo.channel.ChannelTable;
import com.source.BaseClient;
import com.iptv.demo.channel.Channel;
import com.source.ProtocolType;
import com.utils.GzipHelper;
import com.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 超级直播
 */
public final class SuperTVClient extends BaseClient {
    private static final String TAG = "SuperTVClient";

    private static final String CONFIG_URL = "http://119.29.74.92:8123/config.json";
    private static final String IPTV_URL_FORMULAR = "https://119.29.74.92/iptv_auth?area=%s";

    private static final String LIVE_DB_NAME = "live.db";
    private static final String IPTV_TXT_NAME = "iptv.txt";

    private static final long DEFAULT_DB_UPTIME = 0;
    private static final String SETTINGS_DB_UPTIME = "db_uptime";

    private File mDataDir;
    private SharedPreferences mSettings;

    private SuperTVConfig mConfig;
    private List<Channel> mChannelList;
    private List<ChannelGroup.GroupInfo> mGroupInfoList;

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

        addIPTVChannels();

        mListener.onSetup(new ChannelTable(mChannelList, mGroupInfoList));
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

        /**
         * 读取频道数据
         */
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

        mChannelList = reader.getChannelList();
        mGroupInfoList = reader.getGroupInfoList();

        reader.release();
        return true;
    }

    private void addIPTVChannels() {
        File txtFile = new File(mDataDir, IPTV_TXT_NAME);
        if (!txtFile.exists()) {
            /**
             * 不存在，下载
             */
            if (!downloadIPTVList(txtFile)) {
                Log.e(TAG, "download " + txtFile.getName() + " fail");
                return;
            }
        }

        List<Channel> iptvChannelList = IPTVListParser.parse(txtFile);
        if (!iptvChannelList.isEmpty()) {
            mChannelList.addAll(iptvChannelList);
            mGroupInfoList.add(IPTVListParser.getGroupInfo());
        }
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

    @Override
    protected void onDecodeSource(String source) {
        String url = source;

        if (ProtocolType.isHttp(url)
                || ProtocolType.isRtsp(url)
                || ProtocolType.isFlashgetX(url)) {
            /**
             * 可以直接使用
             */
            mListener.onDecodeSource(url);
        }
        else {
            /**
             * 不能直接使用
             */
        }
    }
}
