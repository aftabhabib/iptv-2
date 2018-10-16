package com.iptv.core.hls.exception;

public final class WrongDataTypeException extends RuntimeException {
    /**
     * 构造函数
     */
    public WrongDataTypeException(String desc) {
        super(desc);
    }
}
