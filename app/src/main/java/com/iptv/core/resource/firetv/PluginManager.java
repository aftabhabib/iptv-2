package com.iptv.core.resource.firetv;

import java.util.ArrayList;
import java.util.List;

final class PluginManager {
    private List<Plugin> mPluginList;

    /**
     * 构造函数
     */
    public PluginManager() {
        mPluginList = new ArrayList<Plugin>();
    }

    /**
     * 注册插件
     */
    public void register(Plugin plugin) {
        mPluginList.add(plugin);
    }

    /**
     * 获取合适的插件
     */
    public Plugin getSuitablePlugin(String url) {
        Plugin plugin = null;

        for (int i = 0; i < mPluginList.size(); i++) {
            plugin = mPluginList.get(i);
            if (plugin.isSupported(url)) {
                break;
            }
        }

        return plugin;
    }
}
