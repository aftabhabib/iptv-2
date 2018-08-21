package com.iptv.core.resource.firetv;

/**
 * 服务器相关配置
 */
public final class ServerConfig {
    private static final String BASE_URL = "http://myzhibo8.oss-cn-shanghai.aliyuncs.com/soft/";
    private static final String FILE_SOFT_TXT = "soft5.txt";
    private static final String FILE_TVLIST_ZIP = "tvlist.zip";
    private static final String FILE_KEY_PHP = "key.php";

    /**
     * 获取参数的url
     */
    public static String getSoftParametersUrl() {
        return BASE_URL + "/" + FILE_SOFT_TXT;
    }

    /**
     * 获取频道包的url
     */
    public static String getTVListZipUrl() {
        return BASE_URL + "/" + FILE_TVLIST_ZIP;
    }

    /**
     * 获取key的url
     */
    public static String getKeyUrl() {
        return BASE_URL + "/" + FILE_KEY_PHP;
    }

    /**
     * 构造函数（私有属性，不允许创建实例）
     */
    private ServerConfig() {
        /**
         * nothing
         */
    }
}
