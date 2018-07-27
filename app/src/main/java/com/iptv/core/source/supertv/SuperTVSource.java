package com.iptv.core.source.supertv;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.iptv.core.channel.ChannelGroup;
import com.iptv.core.channel.ChannelTable;
import com.iptv.core.channel.Channel;
import com.iptv.core.source.Plugin;
import com.iptv.core.source.Source;
import com.iptv.core.utils.OkHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import okhttp3.Request;
import okhttp3.Response;

public final class SuperTVSource implements Source {
    private static final String TAG = "SuperTVSource";

    private static final String CONFIG_URL = "http://119.29.74.92:8123/config.json";
    private static final String IPTV_URL_FORMULA = "https://119.29.74.92/iptv_auth?area=%s";

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
        Map<String, String> parameters = new HashMap<String, String>();

        String url = source;

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
        boolean ret = false;

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("User-Agent", "lgsg/1.0");

        Request request = OkHttp.createGetRequest(CONFIG_URL, properties);
        Response response = null;
        try {
            response = OkHttp.getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                mConfig = SuperTVConfig.parse(response.body().string());

                ret = true;
            }
            else {
                Log.e(TAG, "GET " + CONFIG_URL + " fail, " + response.message());

                response.close();
            }
        }
        catch (IOException e) {
            Log.e(TAG, "GET " + CONFIG_URL + " error");

            if (response != null) {
                response.close();
            }
        }

        return ret;
    }

    private boolean prepareChannelTable() {
        File dbFile = new File(mDataDir, LIVE_DB_NAME);

        if (isChannelDBExpired()) {
            /**
             * 过期，重新下载
             */
            if (!downloadChannelDB(dbFile)) {
                /**
                 * 删除未完成的文件
                 */
                if (dbFile.exists()) {
                    dbFile.delete();
                }

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

    private boolean isChannelDBExpired() {
        long dbUptime = mSettings.getLong(SETTINGS_DB_UPTIME, DEFAULT_DB_UPTIME);
        if (dbUptime < mConfig.getDatabaseUptime()) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean downloadChannelDB(File dbFile) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("User-Agent", "lgsg/1.0");

        return download(mConfig.getDatabaseUrl(), properties, dbFile);
    }

    private static boolean download(String url, Map<String, String> properties, File file) {
        boolean ret = false;

        Request request = OkHttp.createGetRequest(url, properties);
        Response response = null;
        try {
            response = OkHttp.getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                GZIPInputStream gzInput = new GZIPInputStream(response.body().byteStream());
                ret = save(gzInput, file);
                gzInput.close();
            }
            else {
                Log.e(TAG, "GET " + url + " fail, " + response.message());

                response.close();
            }
        }
        catch (IOException e) {
            Log.e(TAG, "GET " + url + " error");

            if (response != null) {
                response.close();
            }
        }

        return ret;
    }

    private static boolean save(InputStream input, File file) {
        boolean ret = false;

        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            while (true) {
                int bytesRead = input.read(buf);
                if (bytesRead == -1) {
                    break;
                }

                output.write(buf, 0, bytesRead);
            }

            ret = true;
        }
        catch (IOException e) {
            Log.e(TAG, "download error");
        }
        finally {
            if (output != null) {
                try {
                    output.close();
                }
                catch (IOException e) {
                    //ignore
                }
            }
        }

        return ret;
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
            group.addChannels(IPTVListParser.parse(txtFile));
        }

        return group;
    }

    private boolean downloadIPTVList(File txtFile) {
        String area = getAreaByUserIp("https://g3.le.com/r?format=1", null);
        if (area.isEmpty()) {
            Log.e(TAG, "can not get area by user ip");
            return false;
        }

        String url = "";
        try {
            url = String.format(IPTV_URL_FORMULA, URLEncoder.encode(area, "UTF-8"));
        }
        catch (UnsupportedEncodingException e) {
            //ignore
        }

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("User-Agent", "lgsg/1.0");

        return download(url, properties, txtFile);
    }

    private static String getAreaByUserIp(String url, Map<String, String> properties) {
        String area = "";

        Request request = OkHttp.createGetRequest(url, properties);
        Response response = null;
        try {
            response = OkHttp.getClient().newCall(request).execute();
            if (response.isSuccessful()) {
                String content = response.body().string();

                try {
                    JSONObject rootObj = new JSONObject(content);

                    area = rootObj.getString("desc");
                }
                catch (JSONException e) {
                    Log.e(TAG, "parse " + content + " error, " + e.getMessage());
                }
            }
            else {
                Log.e(TAG, "GET " + url + " fail, " + response.message());

                response.close();
            }
        }
        catch (IOException e) {
            Log.e(TAG, "GET " + url + " error");

            if (response != null) {
                response.close();
            }
        }

        return area;
    }

    private void preparePlugin() {
        mPluginList = new ArrayList<Plugin>(15);
    }
}
