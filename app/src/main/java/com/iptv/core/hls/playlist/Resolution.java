package com.iptv.core.hls.playlist;

/**
 * 视频图像分辨率
 */
public class Resolution {
    private int mWidth;
    private int mHeight;

    /**
     * 构造函数
     */
    public Resolution(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    /**
     * 获取视频图像宽
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * 获取视频图像高
     */
    public int getHeight() {
        return mHeight;
    }
}
