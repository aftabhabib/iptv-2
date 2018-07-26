package com.iptv.core.player.source;

import java.io.IOException;

public interface Source {
    /**
     * 连接
     */
    void connect(String url) throws IOException;

    /**
     * 获取类型
     */
    String getMime();

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
