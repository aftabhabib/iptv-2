package com.iptv.core.ts;

import java.util.Map;

final class ProgramMapSection {
    private int mProgramNumber;

    private int mVersion;
    private Map<Integer, Element> mElements;

    private ProgramMapSection(int programNumber, int version, Map<Integer, Element> elements) {
        mProgramNumber = programNumber;

        mVersion = version;
        mElements = elements;
    }

    /**
     * 节目号
     */
    public int getProgramNumber() {
        return mProgramNumber;
    }

    /**
     * 版本
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * 元素
     */
    public Map<Integer, Element> getElements() {
        return mElements;
    }

    /**
     * 解析数据，创建section
     */
    public static ProgramMapSection parse(byte[] data) {
        return null;
    }
}
