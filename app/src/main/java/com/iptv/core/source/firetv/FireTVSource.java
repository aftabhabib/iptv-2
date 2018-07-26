package com.iptv.core.source.firetv;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.iptv.core.channel.Channel;
import com.iptv.core.channel.ChannelGroup;
import com.iptv.core.channel.ChannelTable;
import com.iptv.core.source.Plugin;
import com.iptv.core.source.firetv.plugin.ChengboPlugin;
import com.iptv.core.utils.ProtocolType;
import com.iptv.core.source.Source;
import com.iptv.core.source.utils.HttpHelper;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FireTVSource implements Source {
    private static final String TAG = "FireTVSource";

    private static final String SERVER_URL = "http://myzhibo8.oss-cn-shanghai.aliyuncs.com/soft/";
    private static final String SOFT_TXT_URL = SERVER_URL + "soft.txt";
    private static final String TVLIST_ZIP_URL = SERVER_URL + "tvlist.zip";

    private static final String TVLIST_XML_NAME = "tvlist.xml";

    private static final String DEFAULT_TVLIST_DATE = "0110";
    private static final String SETTINGS_TVLIST_DATE = "tvlist_date";

    private static final String SOURCE_PARAMETER_AD = "#ad";
    private static final String SOURCE_PARAMETER_JIEMA = "jiema";
    private static final String SOURCE_PARAMETER_USERAGENT = "useragent";
    private static final String SOURCE_PARAMETER_REFER = "refer";

    private File mDataDir;
    private SharedPreferences mSettings;

    private FireTVConfig mConfig;
    private ChannelTable mChannelTable;
    private List<Plugin> mPluginList;

    public FireTVSource(Context context) {
        mDataDir = new File(context.getFilesDir(), "firetv");
        if (!mDataDir.exists()) {
            mDataDir.mkdir();
        }

        mSettings = context.getSharedPreferences("firetv", 0);
    }

    @Override
    public String getName() {
        return "星火New直播";
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

        if (url.endsWith(SOURCE_PARAMETER_AD)) {
            /**
             * 源代码中直接删除，这里也同样处理
             */
            url = url.substring(0, url.length() - SOURCE_PARAMETER_AD.length());
        }

        if (url.contains(SOURCE_PARAMETER_JIEMA)) {
            /**
             * 与源代码中的播放器类型有关，这里不使用
             */
            String[] results = url.split(SOURCE_PARAMETER_JIEMA);

            url = results[0];
        }

        if (url.contains(SOURCE_PARAMETER_USERAGENT)) {
            /**
             * HTTP/HTTPS协议，头部中的UserAgent字段
             */
            String[] results = url.split(SOURCE_PARAMETER_USERAGENT);

            url = results[0];
            parameters.put("UserAgent", results[1]);
        }

        if (url.contains(SOURCE_PARAMETER_REFER)) {
            /**
             * HTTP/HTTPS协议，头部中的Refer字段
             */
            String[] results = url.split(SOURCE_PARAMETER_REFER);

            url = results[0];
            parameters.put("Refer", results[1]);
        }

        if (url.contains("&amp;")) {
            /**
             * HTML语法中的转义字符，替换
             */
            url = url.replaceAll("&amp;", "&");
        }

        if (isEncodedUrl(url)) {
            /**
             * 自定义协议，需要进一步解码
             */
            for (int i = 0; i < mPluginList.size(); i++) {
                Plugin plugin = mPluginList.get(i);
                if (plugin.isSupported(url)) {
                    url = plugin.decode(url);
                    break;
                }
            }
        }

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
        byte[] content = HttpHelper.opGet(SOFT_TXT_URL, null);
        if (content == null) {
            Log.e(TAG, "get " + SOFT_TXT_URL + " fail");
            return false;
        }

        mConfig = FireTVConfig.parse(new String(content));

        return mConfig != null;
    }

    private boolean prepareChannelTable() {
        if (isChannelListExpired()) {
            if (!downloadChannelList()) {
                Log.e(TAG, "download channel list fail");
                return false;
            }

            /**
             * 更新本地的频道文件日期
             */
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(SETTINGS_TVLIST_DATE, mConfig.getTvlistDate());
            editor.commit();
        }

        return readChannelList();
    }

    private boolean isChannelListExpired() {
        String tvListDate = mSettings.getString(SETTINGS_TVLIST_DATE, DEFAULT_TVLIST_DATE);
        if (tvListDate.equals(mConfig.getTvlistDate())) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean downloadChannelList() {
        File zipFile = new File(mDataDir, System.currentTimeMillis() + ".zip");

        if (!HttpHelper.opDownload(TVLIST_ZIP_URL, null, zipFile)) {
            Log.e(TAG, "download " + TVLIST_ZIP_URL + " fail");
            return false;
        }

        if (!extract(zipFile, mDataDir)) {
            Log.e(TAG, "extract " + zipFile.getName() + " fail");
            return false;
        }

        /**
         * 解压之后就删除压缩包文件
         */
        zipFile.delete();

        return true;
    }

    private static boolean extract(File file, File dir) {
        try {
            ZipFile zipFile = new ZipFile(file);

            if (!zipFile.isValidZipFile()) {
                Log.e(TAG, file.getName() + " is not zip format");
                return false;
            }

            if (zipFile.isEncrypted()) {
                zipFile.setPassword("FiReTvtEst@Qq.cOm2");
            }

            zipFile.extractAll(dir.getPath());
        }
        catch (ZipException e) {
            Log.e(TAG, "extract error, " + e.getMessage());
            return false;
        }

        return true;
    }

    private boolean readChannelList() {
        File xmlFile = new File(mDataDir, TVLIST_XML_NAME);
        if (!xmlFile.exists()) {
            Log.e(TAG, xmlFile.getName() + " is not exist");
            return false;
        }

        List<Channel> channelList = TVListParser.parse(xmlFile);
        List<ChannelGroup.GroupInfo> groupInfoList = TVListParser.getGroupInfoList();
        mChannelTable = ChannelTable.create(channelList, groupInfoList);

        return true;
    }

    private void preparePlugin() {
        mPluginList = new ArrayList<Plugin>(15);
        mPluginList.add(new ChengboPlugin(mConfig.getYzkey()));
    }

    private static boolean isEncodedUrl(String url) {
        if (!ProtocolType.isHttpOrHttps(url)
                && !ProtocolType.isRtsp(url)
                && !ProtocolType.isRtmp(url)
                && !ProtocolType.isNaga(url)
                && !ProtocolType.isTVBus(url)
                && !ProtocolType.isForceP2P(url)) {
            return true;
        }

        return false;
    }
}
