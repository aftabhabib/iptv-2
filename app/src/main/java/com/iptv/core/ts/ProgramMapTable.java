package com.iptv.core.ts;

import java.util.HashMap;
import java.util.Map;

/**
 * PMT
 */
class ProgramMapTable {
    private Map<Integer, ProgramDefinition> mTable;

    public ProgramMapTable() {
        mTable = new HashMap<Integer, ProgramDefinition>();
    }

    /**
     * 是否包含指定节目的映射
     */
    public boolean containsProgramMap(int programNumber) {
        return mTable.containsKey(programNumber);
    }

    /**
     * 获取指定节目映射的版本
     */
    public int getProgramMapVersion(int programNumber) {
        if (!containsProgramMap(programNumber)) {
            return -1;
        }

        return mTable.get(programNumber).getVersion();
    }

    /**
     * 放入section
     */
    public void putSection(Section section) {
        ProgramDefinition programDef = new ProgramDefinition(
                section.getVersion(), section.getContent());

        mTable.put(section.getProgramNumber(), programDef);
    }

    /**
     * 获取指定节目的映射
     */
    public Map<Integer, Integer> getProgramMap(int programNumber) {
        if (!containsProgramMap(programNumber)) {
            return null;
        }

        return mTable.get(programNumber).getContent();
    }

    /**
     * 获取已知节目映射的个数
     */
    public int size() {
        return mTable.size();
    }

    /**
     * PMT被分为若干个section
     */
    public static class Section {
        private int mProgramNumber;

        /**
         * The version number shall be incremented by 1 modulo 32
         * when a change in the information carried within the section occurs.
         * Version number refers to the definition of a single program,
         * and therefore to a single section
         */
        private int mVersion;
        private Map<Integer, Integer> mContent;

        public Section(int programNumber, int version, Map<Integer, Integer> content) {
            mProgramNumber = programNumber;

            mVersion = version;
            mContent = content;
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
         * 内容（elementary_PID与stream_type的映射关系）
         */
        public Map<Integer, Integer> getContent() {
            return mContent;
        }
    }

    public static Section parseSection(byte[] data) {
        return null;
    }

    /**
     * 节目定义
     */
    class ProgramDefinition {
        private int mVersion;
        private Map<Integer, Integer> mContent;

        public ProgramDefinition(int version, Map<Integer, Integer> content) {
            mVersion = version;
            mContent = content;
        }

        public int getVersion() {
            return mVersion;
        }

        public Map<Integer, Integer> getContent() {
            return mContent;
        }
    }
}
