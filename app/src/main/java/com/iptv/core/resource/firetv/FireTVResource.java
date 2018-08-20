package com.iptv.core.resource.firetv;

import android.content.Context;
import android.util.Log;

import com.iptv.core.resource.AbstractResource;
import com.iptv.core.resource.Plugin;
import com.iptv.core.resource.PluginManager;
import com.iptv.core.resource.firetv.plugin.ChengboPlugin;
import com.iptv.core.utils.OkHttp;
import com.iptv.core.utils.ProtocolType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

public final class FireTVResource extends AbstractResource {
    private static final String TAG = "FireTVResource";

    private static final String SERVER_URL = "http://myzhibo8.oss-cn-shanghai.aliyuncs.com/soft/";
    private static final String SOFT_TXT_URL = SERVER_URL + "soft5.txt";
    private static final String TVLIST_ZIP_URL = SERVER_URL + "tvlist.zip";

    private static final String PREF_TVLIST_DATE = "tvlist_date";

    private static final String SOURCE_PARAMETER_AD = "#ad";
    private static final String SOURCE_PARAMETER_JIEMA = "jiema";
    private static final String SOURCE_PARAMETER_USERAGENT = "useragent";
    private static final String SOURCE_PARAMETER_REFER = "refer";

    private SoftParameters mSoftParameters;
    private PluginManager mPluginManager;
    private TVListZip mTVListZip;

    /**
     * 构造函数
     */
    public FireTVResource(Context context) {
        super(context);

        mSoftParameters = new SoftParameters();
        mPluginManager = new PluginManager();
        mTVListZip = new TVListZip(getFilesDir());
    }

    @Override
    protected String getName() {
        return "星火New直播";
    }

    @Override
    protected void onLoadChannelTable() {
        fetchSoftParameters();

        if (mSoftParameters.isEmpty()) {
            notifyError("获取参数失败");
            return;
        }

        mPluginManager.register(new ChengboPlugin(mSoftParameters.getKey()));

        if (!containsPreference(PREF_TVLIST_DATE)
                || !getPreference(PREF_TVLIST_DATE).equals(mSoftParameters.getTVListDate())) {
            /**
             * 没有记录或者已过期，下载
             */
            fetchTVListZip();

            if (!mTVListZip.exists()) {
                notifyError("下载频道包失败");
                return;
            }

            putPreference(PREF_TVLIST_DATE, mSoftParameters.getTVListDate());
        }
        else {
            if (!mTVListZip.exists()) {
                notifyError("本地频道包缺失");
                return;
            }
        }

        try {
            TVListParser parser = new TVListParser();
            notifyChannelTable(parser.readChannelGroups(mTVListZip.extract()));
        }
        catch (Exception e) {
            notifyError("读取频道数据出错");
            return;
        }
    }

    /**
     * 获取参数
     */
    private void fetchSoftParameters() {
        Response response = null;

        try {
            response = OkHttp.get(SOFT_TXT_URL, null);

            if (response.isSuccessful()) {
                InputStream input = response.body().byteStream();
                mSoftParameters.write(input);
            }
            else {
                Log.e(TAG, "GET " + SOFT_TXT_URL + " fail, " + response.message());
            }
        }
        catch (IOException e) {
            Log.e(TAG, "GET " + SOFT_TXT_URL + " error");
        }
        finally {
            /**
             * 释放网络连接
             */
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * 获取频道包
     */
    private void fetchTVListZip() {
        Response response = null;

        try {
            response = OkHttp.get(TVLIST_ZIP_URL, null);

            if (response.isSuccessful()) {
                InputStream input = response.body().byteStream();
                mTVListZip.write(input);
            }
            else {
                Log.e(TAG, "GET " + TVLIST_ZIP_URL + " fail, " + response.message());
            }
        }
        catch (IOException e) {
            Log.e(TAG, "GET " + TVLIST_ZIP_URL + " error");
        }
        finally {
            /**
             * 释放网络连接
             */
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    protected void onDecodeSource(String source) {
        String url = source;
        Map<String, String> properties = new HashMap<String, String>();

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
             * HTTP/HTTPS协议，请求头部中的User-Agent字段
             */
            String[] results = url.split(SOURCE_PARAMETER_USERAGENT);

            url = results[0];
            properties.put("User-Agent", results[1]);
        }

        if (url.contains(SOURCE_PARAMETER_REFER)) {
            /**
             * HTTP/HTTPS协议，请求头部中的Refer字段
             */
            String[] results = url.split(SOURCE_PARAMETER_REFER);

            url = results[0];
            properties.put("Refer", results[1]);
        }

        if (url.contains("&amp;")) {
            /**
             * HTML语法中的转义字符，替换
             */
            url = url.replaceAll("&amp;", "&");
        }

        if (!ProtocolType.isOpen(url)) {
            /**
             * 自定义协议，需要翻译
             */
            Plugin plugin = mPluginManager.getSuitablePlugin(url);
            if (plugin != null) {
                url = plugin.translate(url);
                properties.putAll(plugin.getProperties());
            }
            else {
                Log.w(TAG, "no suitable plugin for " + url);
            }
        }

        notifySource(url, properties);
    }
}
