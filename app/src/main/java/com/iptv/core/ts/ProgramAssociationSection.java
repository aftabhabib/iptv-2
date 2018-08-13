package com.iptv.core.ts;

import java.util.Map;

final class ProgramAssociationSection {
    private int mTableVersion;

    private int mSectionNumber;
    private int mLastSectionNumber;

    private Map<Integer, Program> mAssociations;

    /**
     * 构造函数
     */
    private ProgramAssociationSection(int tableVersion, int sectionNumber, int lastSectionNumber,
                   Map<Integer, Program> associations) {
        mTableVersion = tableVersion;

        mSectionNumber = sectionNumber;
        mLastSectionNumber = lastSectionNumber;

        mAssociations = associations;
    }

    /**
     * PAT的版本号
     */
    public int getTableVersion() {
        return mTableVersion;
    }

    /**
     * section的序号
     */
    public int getSectionNumber() {
        return mSectionNumber;
    }

    /**
     * 最后一个section的序号
     */
    public int getLastSectionNumber() {
        return mLastSectionNumber;
    }

    /**
     * 关联
     */
    public Map<Integer, Program> getAssociations() {
        return mAssociations;
    }

    /**
     * 解析数据，创建section
     */
    public static ProgramAssociationSection parse(byte[] data) {
        return null;
    }
}
