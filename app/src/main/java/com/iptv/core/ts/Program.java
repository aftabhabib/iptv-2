package com.iptv.core.ts;

import java.util.Map;

final class Program {
    private int mProgramNumber;

    private int mMapVersion;
    private Map<Integer, Element> mElements;

    /**
     * 构造函数
     */
    public Program(int programNumber) {
        mProgramNumber = programNumber;
    }

    /**
     * 节目号
     */
    public int getProgramNumber() {
        return mProgramNumber;
    }

    /**
     * 节目映射版本
     */
    public int getMapVersion() {
        return mMapVersion;
    }

    /**
     * 设置定义（映射版本和包含的元素）
     */
    public void setDefinition(int version, Map<Integer, Element> elements) {
        mMapVersion = version;
        mElements = elements;
    }

    /**
     * 是否包含对应elementary_PID的元素
     */
    public boolean containsElement(int packetId) {
        return mElements.containsKey(packetId);
    }

    /**
     * 对应elementary_PID的元素
     */
    public Element getElement(int packetId) {
        if (!containsElement(packetId)) {
            return null;
        }

        return mElements.get(packetId);
    }
}
