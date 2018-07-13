package com.forcetech.android;

import android.util.Log;
import android.util.Xml;

import com.utils.HttpHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
    private ForceChannel mCurrChannel = null;

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
     * @return
     */
    public int startP2PService() {
        return start(mServPort, mBufferSize);
    }

    /**
     * 停止p2p服务
     * @return
     */
    public int stopP2PService() {
        return stop();
    }

    /**
     * 开始取流
     * @param url ---- 频道
     */
    public void startChannel(String url) {
        mCurrChannel = ForceChannel.parse(url);

        String api = ForceApi.switchChannel(mServPort, mCurrChannel);
        sendCommand(api);
    }

    /**
     * 当前的频道停止取流
     */
    public void stopChannel() {
        if (mCurrChannel == null) {
            throw new IllegalStateException("no active channel");
        }

        String api = ForceApi.stopChannel(mServPort, mCurrChannel);
        sendCommand(api);

        mCurrChannel = null;
    }

    /**
     * 获取当前频道的播放地址
     */
    public String getPlayUrl() {
        return "http://127.0.0.1:" + mServPort + "/" + mCurrChannel.getPath();
    }

    private native int start(int port, int bufferSize);

    private native int stop();

    /**
     * FIXME: 检查命令是否得到正确响应
     */
    private void sendCommand(String api) {
        byte[] content = HttpHelper.opGet(api, null);
        if (content == null) {
            Log.e(TAG, "send " + api + " fail");
        }
        else {
            XmlPullParser xmlParser = Xml.newPullParser();

            try {
                xmlParser.setInput(new ByteArrayInputStream(content), "utf-8");

                int eventType = xmlParser.getEventType();
                do {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT: {
                            break;
                        }
                        case XmlPullParser.START_TAG: {
                            break;
                        }
                        case XmlPullParser.END_TAG: {
                            break;
                        }
                        default: {
                            break;
                        }
                    }

                    eventType = xmlParser.next();
                }
                while (eventType != XmlPullParser.END_DOCUMENT);
            }
            catch (IOException e) {
                //ignore
            }
            catch (XmlPullParserException e) {
                Log.e(TAG, "parse xml fail, " + e.getMessage());
            }
        }
    }
}
