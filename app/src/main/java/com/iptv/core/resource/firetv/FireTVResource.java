package com.iptv.core.resource.firetv;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.iptv.core.resource.AbstractResource;
import com.iptv.core.resource.Plugin;
import com.iptv.core.resource.PluginManager;
import com.iptv.core.resource.firetv.plugin.ChengboPlugin;
import com.iptv.core.resource.firetv.plugin.KingSoftLivePlugin;
import com.iptv.core.utils.OkHttp;
import com.iptv.core.utils.ProtocolType;

import java.io.IOException;

import okhttp3.Response;

/**
 * 星火New直播的频道资源
 */
public final class FireTVResource extends AbstractResource {
    private static final String TAG = "FireTVResource";

    private static final int MSG_GET_SOFT_PARAMETERS = 0;
    private static final int MSG_DOWNLOAD_TVLIST = 1;
    private static final int MSG_READ_CHANNELS = 2;
    private static final int MSG_REGISTER_PLUGINS = 3;
    private static final int MSG_DECODE_URL = 4;

    private static final String PREF_TVLIST_DATE = "tvlist_date";

    private Authorization mAuthorization;
    private PluginManager mPluginManager;

    /**
     * 构造函数
     */
    public FireTVResource(Context context) {
        super(context);
    }

    @Override
    protected String getName() {
        return "星火New直播";
    }

    @Override
    public void loadChannelTable() {
        sendMessage(MSG_GET_SOFT_PARAMETERS);
    }

    @Override
    public void decodeUrl(String url) {
        sendMessage(MSG_DECODE_URL, url);
    }

    @Override
    protected boolean onHandleMessage(Message msg) {
        switch (msg.what) {
            case MSG_GET_SOFT_PARAMETERS: {
                onGetSoftParameters();

                break;
            }
            case MSG_DOWNLOAD_TVLIST: {
                String tvListDate = (String)msg.obj;
                onDownloadTVList(tvListDate);

                break;
            }
            case MSG_READ_CHANNELS: {
                onReadChannels();

                break;
            }
            case MSG_REGISTER_PLUGINS: {
                String yzKey = (String)msg.obj;
                onRegisterPlugins(yzKey);

                break;
            }
            case MSG_DECODE_URL: {
                String url = (String)msg.obj;
                onDecodeUrl(url);

                break;
            }
            default: {
                /**
                 * message not handled
                 */
                return false;
            }
        }

        return true;
    }

    /**
     * 响应获取参数
     */
    private void onGetSoftParameters() {
        SoftParameters softParameters = new SoftParameters();

        if (!fetchSoftParameters(softParameters)) {
            notifyError("获取参数失败");
        }
        else {
            String[] parameters = softParameters.parse();
            if (parameters.length < 5) {
                notifyError("参数格式错误");
            }

            String tvListDate = parameters[0];
            String tips = parameters[1];
            String defaultUrl = parameters[2];
            String yzKey = parameters[3]; //also named “logo”
            String enableAD = parameters[4];

            sendMessage(MSG_DOWNLOAD_TVLIST, tvListDate);
            sendMessage(MSG_REGISTER_PLUGINS, yzKey);
        }
    }

    /**
     * 获取参数
     */
    private static boolean fetchSoftParameters(SoftParameters softParameters) {
        boolean ret = false;

        Response response = null;
        try {
            response = OkHttp.get(ServerConfig.getSoftParametersUrl(), null);

            if (response.isSuccessful()) {
                ret = softParameters.write(response.body().byteStream());
            }
            else {
                Log.e(TAG, "GET soft parameters fail, " + response.message());
            }
        }
        catch (IOException e) {
            Log.e(TAG, "read soft parameters error");
        }
        finally {
            /**
             * 释放网络连接
             */
            if (response != null) {
                response.close();
            }
        }

        return ret;
    }

    /**
     * 响应下载频道包
     */
    private void onDownloadTVList(String tvListDate) {
        if (!containsPreference(PREF_TVLIST_DATE)
                || !getPreference(PREF_TVLIST_DATE).equals(tvListDate)) {
            /**
             * 没有记录或者已过期，下载
             */
            TVListZip tvListZip = new TVListZip(getFilesDir());
            if (!fetchTVListZip(tvListZip)) {
                notifyError("下载频道包失败");
                return;
            }

            putPreference(PREF_TVLIST_DATE, tvListDate);
        }

        sendMessage(MSG_READ_CHANNELS);
    }

    /**
     * 下载频道包
     */
    private static boolean fetchTVListZip(TVListZip tvListZip) {
        boolean ret = false;

        Response response = null;
        try {
            response = OkHttp.get(ServerConfig.getTVListZipUrl(), null);

            if (response.isSuccessful()) {
                ret = tvListZip.write(response.body().byteStream());
            }
            else {
                Log.e(TAG, "GET tvlist.zip fail, " + response.message());
            }
        }
        catch (IOException e) {
            Log.e(TAG, "read tvlist.zip error");
        }
        finally {
            /**
             * 释放网络连接
             */
            if (response != null) {
                response.close();
            }
        }

        return ret;
    }

    /**
     * 响应读取频道数据
     */
    private void onReadChannels() {
        TVListZip tvListZip = new TVListZip(getFilesDir());

        if (!tvListZip.exists()) {
            notifyError("本地频道包缺失");
        }
        else {
            TVListParser parser = new TVListParser();

            try {
                notifyChannelTable(parser.readChannelGroups(tvListZip.extract()));
            }
            catch (Exception e) {
                notifyError("读取频道数据出错");
            }
        }
    }

    /**
     * 响应注册插件
     */
    private void onRegisterPlugins(String yzKey) {
        mAuthorization = new Authorization(yzKey);

        mPluginManager = new PluginManager();
        mPluginManager.register(new ChengboPlugin(mAuthorization));
        mPluginManager.register(new KingSoftLivePlugin(mAuthorization));
    }

    /**
     * 响应解码频道源url
     */
    private void onDecodeUrl(String url) {
        SourceSpec sourceSpec = SourceSpec.decode(url);
        if (sourceSpec == null) {
            Log.e(TAG, "decode " + url + " fail");
            return;
        }

        url = sourceSpec.getUrl();

        if (!ProtocolType.isOpen(url)) {
            /**
             * 自定义协议，需要翻译
             */
            Plugin plugin = mPluginManager.getSuitablePlugin(url);
            if (plugin == null) {
                Log.e(TAG, "no suitable plugin for " + url);
                return;
            }

            url = plugin.translate(url);
        }

        notifySource(url, sourceSpec.getProperties());
    }
}
