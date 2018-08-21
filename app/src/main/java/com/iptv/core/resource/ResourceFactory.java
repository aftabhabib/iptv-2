package com.iptv.core.resource;

import android.content.Context;
import android.os.Looper;

import com.iptv.core.resource.firetv.FireTVResource;

/**
 * 资源工厂
 */
public final class ResourceFactory {
    /**
     * 创建（默认）资源
     */
    public static Resource createResource(Looper looper, Context context) {
        return createResource(ResourceType.APP_FIRETV, looper, context);
    }

    /**
     * 创建对应类型的资源
     */
    public static Resource createResource(int type, Looper looper, Context context) {
        switch (type) {
            case ResourceType.APP_FIRETV: {
                return new FireTVResource(looper, context);
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
