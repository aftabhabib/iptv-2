package com.iptv.core.resource.firetv;

/**
 * 配置
 */
final class Config {
    private String mTVListDate;
    private String mLogo;

    /**
     * 构造函数
     */
    private Config(String[] parameters) {
        mTVListDate = parameters[0];
        mLogo = parameters[3];
    }

    /**
     * 获取频道列表文件的日期
     */
    public String getTVListDate() {
        return mTVListDate;
    }

    /**
     * 获取logo（意义不明）
     */
    public String getLogo() {
        return mLogo;
    }

    /**
     * 解析数据，创建Config
     */
    public static Config parse(String content) {
        /**
         * 分为多个部分，之间用‘|’分隔
         */
        String[] parameters = content.split("|");

        return new Config(parameters);
    }
}
