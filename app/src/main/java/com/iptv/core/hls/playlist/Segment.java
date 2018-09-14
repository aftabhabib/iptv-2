package com.iptv.core.hls.playlist;

/**
 * 片段
 */
public final class Segment {
    private float mDuration = 0.0f;
    private boolean mIsContinuous = true;
    private String mUri = null;

    private ByteRange mRange = null;
    private Key mKey = null;
    private Map mMap = null;

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
        if (duration <= 0.0f) {
            throw new IllegalArgumentException("invalid duration");
        }

        mDuration = duration;
    }

    /**
     * 设置密钥
     */
    public void setKey(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("invalid key");
        }

        mKey = key;
    }

    /**
     * 设置不连续
     */
    public void setDiscontinuity() {
        mIsContinuous = false;
    }

    /**
     * 设置偏移量
     */
    public void setByteRange(ByteRange range) {
        if (range == null) {
            throw new IllegalArgumentException("invalid range");
        }

        mRange = range;
    }

    /**
     * 设置映射
     */
    public void setMap(Map map) {
        if (map == null) {
            throw new IllegalArgumentException("invalid map");
        }

        mMap = map;
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        if ((uri == null) || uri.isEmpty()) {
            throw new IllegalArgumentException("invalid uri");
        }

        mUri = uri;
    }

    /**
     * 是否定义了时长
     */
    public boolean containsDuration() {
        return mDuration > 0.0f;
    }

    /**
     * 是否定义了密钥
     */
    public boolean containsKey() {
        return mKey != null;
    }

    /**
     * 是否定义了数据范围
     */
    public boolean containsByteRange() {
        return mRange != null;
    }

    /**
     * 是否定义了映射
     */
    public boolean containsMap() {
        return mMap != null;
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mUri != null;
    }

    /**
     * 获取时长（毫秒）
     */
    public float getDuration() {
        if (!containsDuration()) {
            throw new IllegalStateException("no duration");
        }

        return mDuration;
    }

    /**
     * 获取密钥
     */
    public Key getKey() {
        if (!containsKey()) {
            throw new IllegalStateException("no key");
        }

        return mKey;
    }

    /**
     * 是否连续
     */
    public boolean isContinuous() {
        return mIsContinuous;
    }

    /**
     * 获取数据范围
     */
    public ByteRange getByteRange() {
        if (!containsByteRange()) {
            throw new IllegalStateException("no range");
        }

        return mRange;
    }

    /**
     * 获取映射
     */
    public Map getMap() {
        if (!containsMap()) {
            throw new IllegalStateException("no map");
        }

        return mMap;
    }

    /**
     * 获取uri
     */
    public String getUri() {
        if (!containsUri()) {
            throw new IllegalStateException("no uri");
        }

        return mUri;
    }
}
