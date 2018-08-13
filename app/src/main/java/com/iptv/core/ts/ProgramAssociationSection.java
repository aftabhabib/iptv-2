package com.iptv.core.ts;

import java.util.Map;

final class ProgramAssociationSection {
    private int mTableVersion;

    private int mSectionNumber;
    private int mLastSectionNumber;

    private Map<Integer, Integer> mAssociations;

    private ProgramAssociationSection(int tableVersion, int sectionNumber, int lastSectionNumber,
                   Map<Integer, Integer> associations) {
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
     * 节目关联（program_number与program_map_PID的对应关系）
     */
    public Map<Integer, Integer> getAssociations() {
        return mAssociations;
    }

    public static ProgramAssociationSection parse(byte[] data) {
        return null;
    }
}
