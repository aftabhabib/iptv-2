package com.source.firetv;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

import com.source.Channel;
import com.source.GroupInfo;
import com.source.firetv.plugin.ChengboPlugin;
import com.source.BaseClient;
import com.utils.ZipHelper;
import com.utils.HttpHelper;

import java.io.File;
import java.util.ArrayList;
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

    private static final String TVLIST_ZIP_NAME = "tvlist.zip";
    private static final String TVLIST_XML_NAME = "tvlist.xml";

    private static final String DEFAULT_TVLIST_DATE = "0110";
    private static final String SETTINGS_TVLIST_DATE = "tvlist_date";

    private static final String UNZIP_PASSWORD = "FiReTvtEst@Qq.cOm2";

    private File mDataDir;
    private SharedPreferences mSettings;

    private FireTVConfig mConfig;
    private List<Channel> mChannelList;
    private List<GroupInfo> mGroupList;

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

        mListener.onSetup(mChannelList, mGroupList);
    }

    private boolean prepareConfig() {
        byte[] content = HttpHelper.opGet(SOFT_TXT_URL, null);
        if (content == null) {
            Log.e(TAG, "get soft.txt fail");
            return false;
        }

        mConfig = FireTVConfig.parse(new String(content));

        return true;
    }

    private void preparePlugin() {
        mPluginList = new LinkedList<Plugin>();

        mPluginList.add(new ChengboPlugin(mConfig.getYzkey()));
        /**
         * TODO: more plugins
         */
    }

    private boolean prepareChannelTable() {
        File zipFile = new File(mDataDir, TVLIST_ZIP_NAME);
        File xmlFile = new File(mDataDir, TVLIST_XML_NAME);

        if (isTvlistExpired()) {
            /**
             * 下载
             */
            if (!HttpHelper.opDownload(TVLIST_ZIP_URL, null, zipFile)) {
                Log.e(TAG, "download " + TVLIST_ZIP_NAME + " fail");
                return false;
            }

            /**
             * 解压
             */
            if (!ZipHelper.extract(zipFile, mDataDir.getPath(), UNZIP_PASSWORD)) {
                Log.e(TAG, "extract " + TVLIST_ZIP_NAME + " fail");
                return false;
            }

            /**
             * 更新本地的频道文件日期
             */
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(SETTINGS_TVLIST_DATE, mConfig.getTvlistDate());
            editor.commit();
        }

        /**
         * 解析频道数据
         */
        mChannelList = TVListParser.parse(xmlFile);
        if (mChannelList == null) {
            Log.e(TAG, "parse " + TVLIST_XML_NAME + " fail");
            return false;
        }

        mGroupList = createGroupInfo();

        return true;
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

    private static List<GroupInfo> createGroupInfo() {
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

        List<GroupInfo> groupList = new ArrayList<GroupInfo>(GROUP_NAME.length);

        for (int i = 0; i < GROUP_NAME.length; i++) {
            GroupInfo group = new GroupInfo(String.valueOf(i + 1), GROUP_NAME[i]);
            groupList.add(group);
        }

        return groupList;
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

        if (url.startsWith("http://")
                || url.startsWith("https://")
                || url.startsWith("rtmp://")
                || url.startsWith("rtsp://")
                || url.startsWith("vjms://")
                || url.startsWith("tvbus://")
                || url.startsWith("p2p://")) {
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
                    mListener.onError("decode source fail");
                }
            }
            else {
                Log.w(TAG, "no suitable plugin for " + url);
                mListener.onError("decode source fail");
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
