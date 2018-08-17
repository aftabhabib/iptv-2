package com.iptv.core.resource;

import android.content.Context;

import com.iptv.core.resource.firetv.FireTVResource;
import com.iptv.core.resource.supertv.SuperTVSource;

/**
 * 资源工厂
 */
public final class ResourceFactory {
    public static final int RESOURCE_TYPE_FIRETV = 0;
    public static final int RESOURCE_TYPE_SUPERTV = 1;

    /**
     * 创建资源
     */
    public static Resource createResource(int type, Context context) {
        switch (type) {
            case RESOURCE_TYPE_FIRETV: {
                return new FireTVResource(context);
            }
            case RESOURCE_TYPE_SUPERTV: {
                return new SuperTVSource(context);
            }
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
