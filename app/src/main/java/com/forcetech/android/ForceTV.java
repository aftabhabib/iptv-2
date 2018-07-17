package com.forcetech.android;

import android.util.Log;

import com.utils.HttpHelper;

/**
 * 原力SDK
 */
public class ForceTV {
    private static final String TAG = "ForceTV";

    static {
        System.loadLibrary("forcetv");
    }

    private int mServPort;
    private int mBufferSize;

    /**
     * 同一时刻只能有一个活跃频道
     */
    private ChannelInfo mChannel = null;

    /**
     * 构造函数
     * @param servPort ---- p2p服务端口号
     * @param bufferSize ---- 缓冲大小
     */
    public ForceTV(int servPort, int bufferSize) {
        mServPort = servPort;
        mBufferSize = bufferSize;
    }

    /**
     * 启动p2p服务
     */
    public int startP2PService() {
        return start(mServPort, mBufferSize);
    }

    /**
     * 停止p2p服务
     */
    public int stopP2PService() {
        return stop();
    }

    /**
     * 开始取流
     */
    public void startChannel(String url) {
        mChannel = ChannelInfo.parse(url);
        sendCommand(switchCommand());
    }

    /**
     * 当前的频道停止取流
     */
    public void stopChannel() {
        if (mChannel != null) {
            sendCommand(stopCommand());
            mChannel = null;
        }
    }

    /**
     * 获取当前频道的播放地址
     */
    public String getPlayUrl() {
        return "http://127.0.0.1:" + mServPort + "/" + mChannel.getPath();
    }

    private native int start(int port, int bufferSize);

    private native int stop();

    private String switchCommand() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://127.0.0.1:");
        buffer.append(mServPort);
        buffer.append("/cmd.xml?cmd=switch_chan");
        buffer.append("&server=");
        buffer.append(mChannel.getServer());
        buffer.append("&id=");
        buffer.append(mChannel.getId());

        return buffer.toString();
    }

    private String stopCommand() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://127.0.0.1:");
        buffer.append(mServPort);
        buffer.append("/cmd.xml?cmd=stop_chan");
        buffer.append("&id=");
        buffer.append(mChannel.getId());

        return buffer.toString();
    }

    private void sendCommand(String cmd) {
        byte[] content = HttpHelper.opGet(cmd, null);
        if (content == null) {
            Log.e(TAG, "GET " + cmd + " fail");
        }
        else {
            Log.d(TAG, "server response " + new String(content));
        }
    }
}
