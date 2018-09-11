package com.iptv.core.hls.datatype;

public final class DecimalResolution {
    private int mWidth;
    private int mHeight;

    /**
     * 构造函数
     */
    public DecimalResolution(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    /**
     * 获取宽
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * 获取高
     */
    public int getHeight() {
        return mHeight;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(mWidth);
        buffer.append("x");
        buffer.append(mHeight);

        return buffer.toString();
    }

    /**
     * 解析自字符串
     */
    public static DecimalResolution parse(String content) {
        if (!content.contains("x")) {
            throw new IllegalArgumentException("should be two decimal-integers separated " +
                    "by the \"x\" character");
        }

        String[] result = content.split("x");
        if (result.length != 2) {
            throw new IllegalArgumentException("should be two decimal-integers separated " +
                    "by the \"x\" character");
        }

        int width = Integer.parseInt(result[0]);
        int height = Integer.parseInt(result[1]);
        return new DecimalResolution(width, height);
    }
}
