package com.iptv.plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginManager {
    private static PluginManager sInstance = null;

    public static PluginManager getInstance() {
        if (sInstance == null) {
            sInstance = new PluginManager();
        }

        return sInstance;
    }

    private List<Plugin> mPluginList = null;

    private PluginManager() {
        mPluginList = new ArrayList<Plugin>(10);
    }

    public void register(Plugin plugin) {
        mPluginList.add(plugin);
    }

    public Plugin find(String url) {
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
