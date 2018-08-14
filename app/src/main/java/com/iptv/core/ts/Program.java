package com.iptv.core.ts;

import java.util.Map;

final class Program {
    private int mProgramNumber;

    private int mMapVersion = -1;
    private Map<Integer, Element> mElements = null;

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
     * 是否有定义（映射版本和包含的元素）
     */
    public boolean containsDefinition() {
        return ((mMapVersion != -1) && (mElements != null));
    }

    /**
     * 设置定义（映射版本和包含的元素）
     */
    public void setDefinition(int mapVersion, Map<Integer, Element> elements) {
        mMapVersion = mapVersion;
        mElements = elements;
    }

    /**
     * 节目映射版本
     */
    public int getMapVersion() {
        return mMapVersion;
    }

    /**
     * 是否包含对应elementary_PID的元素
     */
    public boolean containsElement(int packetId) {
        if (mElements == null) {
            throw new IllegalStateException("no definition yet");
        }

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
