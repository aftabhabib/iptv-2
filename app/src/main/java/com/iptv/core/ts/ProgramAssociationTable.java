package com.iptv.core.ts;

import java.util.HashMap;
import java.util.Map;

/**
 * PAT
 */
class ProgramAssociationTable {
    private int mVersion;

    private int mTotalSection;
    private int mExpectedSection;

    private Map<Integer, Integer> mTable;

    public ProgramAssociationTable(int version, int totalSection) {
        mVersion = version;

        mTotalSection = totalSection;
        mExpectedSection = 0;

        mTable = new HashMap<Integer, Integer>();
    }

    /**
     * PAT是否过期
     */
    public boolean isExpired(int version) {
        return mVersion != version;
    }

    /**
     * 是不是期待的Section
     */
    public boolean isExpectedSection(Section section) {
        return section.getIndex() == mExpectedSection;
    }

    /**
     * 放入期待的Section
     */
    public void putExpectedSection(Section section) {
        if (!isExpectedSection(section)) {
            throw new IllegalStateException("not expected section");
        }

        mTable.putAll(section.getContent());

        /**
         * next expected section
         */
        mExpectedSection++;
    }

    /**
     * PAT是否结束（所有的Section都放入了）
     */
    public boolean isComplete() {
        return mExpectedSection == mTotalSection;
    }

    /**
     * PAT被分成若干个section
     */
    public static class Section {
        private int mTableVersion;

        private int mIndex;
        private int mTotal;

        private Map<Integer, Integer> mContent;

        public Section(int tableVersion, int index, int total,
                       Map<Integer, Integer> content) {
            mTableVersion = tableVersion;

            mIndex = index;
            mTotal = total;

            mContent = content;
        }

        /**
         * PAT的版本号
         */
        public int getTableVersion() {
            return mTableVersion;
        }

        /**
         * 索引
         */
        public int getIndex() {
            return mIndex;
        }

        /**
         * 总数
         */
        public int getTotal() {
            return mTotal;
        }

        /**
         * 内容（program_number与program_map_PID的对应关系）
         */
        public Map<Integer, Integer> getContent() {
            return mContent;
        }
    }

    public static Section parseSection(byte[] data) {
        return null;
    }
}