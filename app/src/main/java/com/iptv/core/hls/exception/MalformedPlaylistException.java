package com.iptv.core.hls.exception;

/**
 * 播放列表格式异常
 */
public final class MalformedPlaylistException extends Exception {
    /**
     * 构造函数
     */
    public MalformedPlaylistException(String desc) {
        super(desc);
    }
}
