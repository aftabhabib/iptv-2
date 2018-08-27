package com.iptv.core.utils;

import java.io.IOException;

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
    public MalformedFormatException() {
        super();
    }
}
