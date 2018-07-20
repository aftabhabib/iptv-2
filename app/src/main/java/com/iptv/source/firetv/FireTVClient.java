package com.iptv.source.firetv;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

import com.iptv.demo.channel.Channel;
import com.iptv.demo.channel.ChannelGroup;
import com.iptv.demo.channel.ChannelTable;
import com.iptv.source.ProtocolType;
import com.iptv.source.firetv.plugin.ChengboPlugin;
import com.iptv.source.BaseClient;
import com.utils.ZipHelper;
import com.utils.HttpHelper;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 星火New直播
 */
public final class FireTVClient extends BaseClient {
    private static final String TAG = "FireTVClient";

    private static final String SERVER_URL = "http://myzhibo8.oss-cn-shanghai.aliyuncs.com/soft";
    private static final String SOFT_TXT_URL = SERVER_URL + "/soft.txt";
    private static final String TVLIST_ZIP_URL = SERVER_URL + "/tvlist.zip";

    private static final String TVLIST_XML_NAME = "tvlist.xml";

    private static final String DEFAULT_TVLIST_DATE = "0110";
    private static final String SETTINGS_TVLIST_DATE = "tvlist_date";

    private File mDataDir;
    private SharedPreferences mSettings;

    private FireTVConfig mConfig;
    private List<Channel> mChannelList;
    private List<ChannelGroup.GroupInfo> mGroupInfoList;

    private List<Plugin> mPluginList;

    public FireTVClient(Context context, Looper looper) {
        super(looper);

        mDataDir = new File(context.getFilesDir(), "firetv");
        if (!mDataDir.exists()) {
            mDataDir.mkdir();
        }

        mSettings = context.getSharedPreferences("firetv", 0);
    }

    @Override
    protected void onSetup() {
        if (!prepareConfig()) {
            mListener.onError("setup fail");
            return;
        }

        preparePlugin();

        if (!prepareChannelTable()) {
            mListener.onError("setup fail");
            return;
        }

        mListener.onSetup(new ChannelTable(mChannelList, mGroupInfoList));
    }

    private boolean prepareConfig() {
        byte[] content = HttpHelper.opGet(SOFT_TXT_URL, null);
        if (content == null) {
            Log.e(TAG, "get " + SOFT_TXT_URL + " fail");
            return false;
        }

        mConfig = FireTVConfig.parse(new String(content));

        return mConfig != null;
    }

    private void preparePlugin() {
        mPluginList = new LinkedList<Plugin>();

        mPluginList.add(new ChengboPlugin(mConfig.getYzkey()));
        /**
         * TODO: more plugins
         */
    }

    private boolean prepareChannelTable() {
        if (isTvlistExpired()) {
            if (!downloadTvlist()) {
                Log.e(TAG, "download tvlist fail");
                return false;
            }

            /**
             * 更新本地的频道文件日期
             */
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(SETTINGS_TVLIST_DATE, mConfig.getTvlistDate());
            editor.commit();
        }

        return readTvlist();
    }

    private boolean isTvlistExpired() {
        String tvListDate = mSettings.getString(SETTINGS_TVLIST_DATE, DEFAULT_TVLIST_DATE);
        if (tvListDate.equals(mConfig.getTvlistDate())) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean downloadTvlist() {
        File zipFile = new File(mDataDir, "temp.zip");

        if (!HttpHelper.opDownload(TVLIST_ZIP_URL, null, zipFile)) {
            Log.e(TAG, "download " + TVLIST_ZIP_URL + " fail");
            return false;
        }

        final String password = "FiReTvtEst@Qq.cOm2";
        if (!ZipHelper.extract(zipFile, mDataDir.getPath(), password)) {
            Log.e(TAG, "extract " + zipFile.getName() + " fail");
            return false;
        }

        /**
         * 解压之后就删除压缩包文件
         */
        zipFile.delete();

        return true;
    }

    private boolean readTvlist() {
        File xmlFile = new File(mDataDir, TVLIST_XML_NAME);
        if (!xmlFile.exists()) {
            Log.e(TAG, xmlFile.getName() + " is not exist");
            return false;
        }

        mChannelList = TVListParser.parse(xmlFile);
        mGroupInfoList = TVListParser.getGroupInfoList();

        return true;
    }

    @Override
    protected void onDecodeSource(String source) {
        String url = source;
        Map<String, String> property = new HashMap<String, String>();

        if (url.endsWith("#ad")) {
            url = url.substring(0, url.length() - 3);
        }

        if (url.endsWith("jiema0")
                || url.endsWith("jiema1")
                || url.endsWith("jiema2")
                || url.endsWith("jiema3")
                || url.endsWith("jiema4")) {
            url = url.substring(0, url.length() - 6);
        }

        if (url.contains("useragent")) {
            String[] results = url.split("useragent");

            url = results[0];
            property.put("UserAgent", results[1]);
        }

        if (url.contains("refer")) {
            String[] results = url.split("refer");

            url = results[0];
            property.put("Refer", results[1]);
        }

        if (url.contains("&amp;")) {
            url = url.replaceAll("&amp;", "&");
        }

        if (ProtocolType.isHttp(url)
                || ProtocolType.isRtsp(url)
                || ProtocolType.isRtmp(url)
                || ProtocolType.isNaga(url)
                || ProtocolType.isTVBus(url)
                || ProtocolType.isForceP2P(url)) {
            /**
             * 可以直接使用
             */
            mListener.onDecodeSource(url);
        }
        else {
            /**
             * 不能直接使用
             */
            Plugin plugin = findPlugin(url);
            if (plugin != null) {
                url = plugin.process(url, property);
                if (!url.isEmpty()) {
                    mListener.onDecodeSource(url);
                }
                else {
                    Log.w(TAG, plugin.getName() + " process " + url + " fail");
                    mListener.onError("decode " + source + " fail");
                }
            }
            else {
                Log.w(TAG, "no suitable plugin for " + url);
                mListener.onError("decode " + source + " fail");
            }
        }
    }

    private Plugin findPlugin(String url) {
        Plugin plugin = null;

        for (int i = 0; i < mPluginList.size(); i++) {
            plugin = mPluginList.get(i);
            if (plugin.isSupported(url)) {
                break;
            }
        }

        return plugin;
    }
}
