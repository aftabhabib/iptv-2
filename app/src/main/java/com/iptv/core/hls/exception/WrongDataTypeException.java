package com.iptv.core.hls.exception;

/**
 * 错误的数据类型异常
 */
public final class WrongDataTypeException extends RuntimeException {
    /**
     * 构造函数
     */
    public WrongDataTypeException(String desc) {
        super(desc);
    }
}
