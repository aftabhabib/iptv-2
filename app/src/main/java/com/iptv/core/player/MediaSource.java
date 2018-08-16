package com.iptv.core.player;

import java.io.IOException;
import java.util.Map;

public interface MediaSource {
    /**
     * 连接
     */
    boolean connect(String url, Map<String, String> property) throws IOException;

    /**
     * 获取MIME类型
     */
    String getMIMEType();

    /**
     * 读数据
     */
    int read(byte[] buffer, int offset, int size) throws IOException;

    /**
     * 跳过
     */
    long skip(long size) throws IOException;

    /**
     * 断开
     */
    void disconnect();
}
