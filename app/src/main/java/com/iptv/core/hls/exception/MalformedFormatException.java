package com.iptv.core.hls.exception;

import java.io.IOException;

/**
 * 格式异常
 */
public final class MalformedFormatException extends IOException {
    /**
     * 构造函数
     */
    public MalformedFormatException(String desc) {
        super(desc);
    }

    /**
     * 构造函数
     */
    private MalformedFormatException() {
        super();
    }
}
