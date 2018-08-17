package com.iptv.core.resource;

import android.content.Context;

/**
 * 资源工厂
 */
public final class ResourceFactory {
    /**
     * 创建资源
     */
    public static Resource createResource(int type, Context context) {
        switch (type) {
            default: {
                return null;
            }
        }
    }

    /**
     * 构造函数（私有属性，不允许创建实例）
     */
    private ResourceFactory() {
        /**
         * nothing
         */
    }
}
