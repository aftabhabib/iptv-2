package com.iptv.core.hls.playlist.tag;

/**
 * 版本标签
 */
public final class VersionTag extends Tag {
    private int mVersion;

    /**
     * 构造函数
     */
    public VersionTag(int version) {
        super(Name.VERSION);

        mVersion = version;
    }

    /**
     * 获取版本
     */
    public int getVersion() {
        return mVersion;
    }

    @Override
    public int getProtocolVersion() {
        return 2;
    }

    @Override
    public String toString() {
        return mName + ":" + String.valueOf(mVersion);
    }
}
