package com.iptv.core.player;

import java.io.IOException;

public interface DataSource {
    /**
     * 读数据
     */
    int read(byte[] buffer, int offset, int size) throws IOException;

    /**
     * 释放
     */
    void release();
}
