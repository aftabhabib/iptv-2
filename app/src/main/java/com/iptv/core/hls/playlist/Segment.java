package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;

/**
 * 片段
 */
public final class Segment {
    private MetaData mMetaData = new MetaData();
    private String mUri;

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
     * 获取元信息
     */
    public MetaData getMetaData() {
        return mMetaData;
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        mUri = uri;
    }

    /**
     * 设置密钥
     */
    public void setKey(Key key) {
        mKey = key;
    }

    /**
     * 获取时长
     */
    public float getDuration() {
        if (!mMetaData.containsKey(MetaData.KEY_SEGMENT_DURATION)) {
            throw new IllegalStateException("duration is required");
        }

        return mMetaData.getFloat(MetaData.KEY_SEGMENT_DURATION);
    }

    /**
     * 获取范围的偏移
     */
    public long getRangeOffset() {
        if (!mMetaData.containsKey(MetaData.KEY_RANGE_OFFSET)) {
            return 0;
        }
        else {
            return mMetaData.getLong(MetaData.KEY_RANGE_OFFSET);
        }
    }

    /**
     * 获取范围的长度
     */
    public long getRangeLength() {
        if (!mMetaData.containsKey(MetaData.KEY_RANGE_LENGTH)) {
            return -1;
        }
        else {
            return mMetaData.getLong(MetaData.KEY_RANGE_LENGTH);
        }
    }

    /**
     * 是否不连续
     */
    public boolean discontinuity() {
        if (!mMetaData.containsKey(MetaData.KEY_DISCONTINUITY)) {
            return false;
        }
        else {
            return mMetaData.getBoolean(MetaData.KEY_DISCONTINUITY);
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
            throw new IllegalStateException("not encrypt");
        }

        return mKey.getMethod();
    }

    /**
     * 获取密钥的uri
     */
    public String getKeyUri() {
        if (!isEncrypted()) {
            throw new IllegalStateException("not encrypt");
        }

        return mKey.getUri();
    }

    /**
     * 获取密钥的初始向量
     */
    public byte[] getKeyInitVector() {
        if (!isEncrypted()) {
            throw new IllegalStateException("not encrypt");
        }

        return mKey.getInitVector();
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mUri;
    }
}
