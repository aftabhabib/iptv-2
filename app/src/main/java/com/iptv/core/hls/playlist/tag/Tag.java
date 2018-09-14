package com.iptv.core.hls.playlist.tag;

/**
 * 标签
 */
public final class Tag {
    private String mName;
    private String mValue;

    /**
     * 构造函数
     */
    public Tag(String name) {
        this(name,null);
    }

    /**
     * 构造函数
     */
    public Tag(String name, String value) {
        mName = name;
        mValue = value;
    }

    /**
     * 是否有值
     */
    public boolean containsValue() {
        return mValue != null;
    }

    /**
     * 获取名称
     */
    public String getName() {
        return mName;
    }

    /**
     * 获取值
     */
    public String getValue() {
        if (!containsValue()) {
            throw new IllegalStateException("no value");
        }

        return mValue;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(mName);
        if (containsValue()) {
            buffer.append(":");
            buffer.append(mValue);
        }

        return buffer.toString();
    }

    /**
     * 来自字符串
     */
    public static Tag valueOf(String line) {
        if (!isTag(line)) {
            throw new IllegalArgumentException("not a tag");
        }

        String[] results = line.split(":");
        if (results.length == 1) {
            return new Tag(results[0]);
        }
        else {
            return new Tag(results[0], results[1]);
        }
    }

    /**
     * 是不是标签
     */
    public static boolean isTag(String line) {
        if (line == null) {
            throw new IllegalArgumentException("invalid line");
        }

        return line.startsWith("#EXT");
    }
}
