package com.iptv.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class StringOutputStream extends OutputStream {
    private ByteArrayOutputStream mBuffer;

    /**
     * 构造函数
     */
    public StringOutputStream() {
        mBuffer = new ByteArrayOutputStream();
    }

    @Override
    public void write(int value) {
        mBuffer.write(value);
    }

    /**
     * 转换为指定编码格式的字符串
     */
    public String toString(String charset) throws UnsupportedEncodingException {
        return new String(mBuffer.toByteArray(), charset);
    }

    @Override
    public String toString() {
        String ret = "";

        try {
            ret = toString("UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            /**
             * ignore
             */
        }

        return ret;
    }
}
