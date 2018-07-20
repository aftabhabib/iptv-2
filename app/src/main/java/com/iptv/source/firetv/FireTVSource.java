package com.iptv.source.firetv;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

import com.iptv.channel.Channel;
import com.iptv.channel.ChannelGroup;
import com.iptv.channel.ChannelTable;
import com.iptv.plugin.PluginManager;
import com.iptv.plugin.firetv.ChengboPlugin;
import com.iptv.source.AbstractSource;
import com.utils.ZipHelper;
import com.utils.HttpHelper;

import java.io.File;
import java.util.List;

/**
 * 星火New直播
 */
public final class FireTVSource extends AbstractSource {
    private static final String TAG = "FireTVSource";

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

    public FireTVSource(Context context, Looper looper) {
        super(looper);

        mDataDir = new File(context.getFilesDir(), "firetv");
        if (!mDataDir.exists()) {
            mDataDir.mkdir();
        }

        mSettings = context.getSharedPreferences("firetv", 0);
    }

    @Override
    protected void onSetup(OnSetupListener listener) {
        if (!prepareConfig()) {
            listener.onError("setup fail");
            return;
        }

        preparePlugin();

        if (!prepareChannelTable()) {
            listener.onError("setup fail");
            return;
        }

        listener.onSetup(new ChannelTable(mChannelList, mGroupInfoList));
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
        PluginManager pluginMgr = PluginManager.getInstance();
        /**
         * register plugins
         */
        pluginMgr.register(new ChengboPlugin(mConfig.getYzkey()));
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
}
