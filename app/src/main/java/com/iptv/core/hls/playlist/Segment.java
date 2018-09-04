package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;

import java.util.Arrays;

/**
 * 片段
 */
public final class Segment {
    private float mDuration = 0.0f;
    private int mSequenceNumber = -1;
    private int mDiscontinuitySequenceNumber = -1;
    private String mUri = null;
    private MetaData mMetaData = new MetaData();
    private Key mKey = null;

    /**
     * 构造函数
     */
    public Segment() {
        /**
         * nothing
         */
    }

    /**
     * 设置时长
     */
    public void setDuration(float duration) {
        mDuration = duration;
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        mUri = uri;
    }

    /**
     * 设置序号
     */
    public void setSequenceNumber(int sequenceNumber) {
        mSequenceNumber = sequenceNumber;
    }

    /**
     * 设置Discontinuity序号
     */
    public void setDiscontinuitySequenceNumber(int discontinuitySequenceNumber) {
        mDiscontinuitySequenceNumber = discontinuitySequenceNumber;
    }

    /**
     * 设置字节范围
     */
    public void setByteRange(long offset, long length) {
        mMetaData.putString("range", "bytes=" + offset + "-" + (offset + length - 1));
    }

    /**
     * 设置不连续标记
     */
    public void setDiscontinuity() {
        mMetaData.putBoolean("discontinuity", true);
    }

    /**
     * 设置密钥
     */
    public void setKey(Key key) {
        mKey = key;
    }

    /**
     * 获取时长（毫秒）
     */
    public int getDuration() {
        return (int)(mDuration * 1000);
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mUri;
    }

    /**
     * 获取序号
     */
    public int getSequenceNumber() {
        return mSequenceNumber;
    }

    /**
     * 获取Discontinuity序号
     */
    public int getDiscontinuitySequenceNumber() {
        return mDiscontinuitySequenceNumber;
    }

    /**
     * 是否定义了数据范围
     */
    public boolean containsRange() {
        return mMetaData.containsKey("range");
    }

    /**
     * 获取范围的长度
     */
    public String getRange() {
        if (!containsRange()) {
            throw new IllegalStateException("no BYTERANGE");
        }

        return mMetaData.getString("range");
    }

    /**
     * 是否不连续
     */
    public boolean discontinuity() {
        if (!mMetaData.containsKey("discontinuity")) {
            return false;
        }
        else {
            return mMetaData.getBoolean("discontinuity");
        }
    }

    /**
     * 是否加密
     */
    public boolean isEncrypted() {
        return (mKey != null) && !mKey.getMethod().equals(Key.METHOD_NONE);
    }

    /**
     * 获取加密方式
     */
    public String getEncryptMethod() {
        if (!isEncrypted()) {
            throw new IllegalStateException("no KEY or METHOD is NONE");
        }

        return mKey.getMethod();
    }

    /**
     * 获取密钥的uri
     */
    public String getKeyUri() {
        if (!isEncrypted()) {
            throw new IllegalStateException("no KEY or METHOD is NONE");
        }

        return mKey.getUri();
    }

    /**
     * 获取密钥的初始向量
     */
    public byte[] getKeyInitVector() {
        if (!isEncrypted()) {
            throw new IllegalStateException("no KEY or METHOD is NONE");
        }

        byte[] iv;
        if (mKey.containsInitVector()) {
            iv = mKey.getInitVector();
        }
        else {
            iv = makeInitVector();
        }

        return iv;
    }

    /**
     * 根据片段序号生成密钥的初始化向量
     */
    private byte[] makeInitVector() {
        byte[] iv = new byte[16];

        Arrays.fill(iv, 0, 12, (byte)0x00);
        iv[15] = (byte)(mSequenceNumber & 0xff);
        iv[14] = (byte)((mSequenceNumber >> 8) & 0xff);
        iv[13] = (byte)((mSequenceNumber >> 16) & 0xff);
        iv[12] = (byte)((mSequenceNumber >> 24) & 0xff);

        return iv;
    }
}
