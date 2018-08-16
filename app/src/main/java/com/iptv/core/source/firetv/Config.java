package com.iptv.core.source.firetv;

/**
 * 配置
 */
final class Config {
    private String mTVListDate;
    private String mKey;

    /**
     * 构造函数
     */
    private Config(String tvListDate, String key) {
        mTVListDate = tvListDate;
        mKey = key;
    }

    /**
     * 获取频道列表文件的日期
     */
    public String getTVListDate() {
        return mTVListDate;
    }

    /**
     * 获取key（意义不明）
     */
    public String getkey() {
        return mKey;
    }

    /**
     * 解析数据，创建Config
     */
    public static Config parse(String content) {
        /**
         * 分为四个部分，部分之间使用‘|’分隔
         */
        String[] results = content.split("|");
        if (results.length < 4) {
            return null;
        }

        /**
         * 这里只使用第一个和最后一个部分
         */
        return new Config(results[0], results[3]);
    }
}
