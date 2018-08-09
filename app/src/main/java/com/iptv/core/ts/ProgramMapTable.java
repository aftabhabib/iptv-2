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
     * 是否包含指定节目的定义
     */
    public boolean containsProgramDefinition(int programNumber) {
        return mTable.containsKey(programNumber);
    }

    /**
     * 放入section
     */
    public void putSection(Section section) {
        if (!containsProgramDefinition(section.getProgramNumber())) {
            /**
             * new section
             */
            mTable.put(section.getProgramNumber(), section.getProgramDefinition());
        }
        else {
            /**
             * already exist
             */
            ProgramDefinition oldDefinition = mTable.get(section.getProgramNumber());
            ProgramDefinition newDefinition = section.getProgramDefinition();

            if (oldDefinition.isExpired(newDefinition.getVersion())) {
                /**
                 * update
                 */
                mTable.put(section.getProgramNumber(), newDefinition);
            }
        }
    }

    /**
     * 获取program_number对应的program_element
     */
    public Map<Integer, Integer> getProgramElement(int programNumber) {
        if (!containsProgramDefinition(programNumber)) {
            throw new IllegalStateException("program definition not exist");
        }

        ProgramDefinition programDefinition = mTable.get(programNumber);
        return programDefinition.getContent();
    }

    /**
     * PMT被分为若干个section，一个section中有一个节目的定义
     */
    public static class Section {
        private int mProgramNumber;

        private ProgramDefinition mProgramDefinition;

        public Section(int programNumber, ProgramDefinition programDefinition) {
            mProgramNumber = programNumber;
            mProgramDefinition = programDefinition;
        }

        /**
         * 节目号
         */
        public int getProgramNumber() {
            return mProgramNumber;
        }

        /**
         * 节目定义
         */
        public ProgramDefinition getProgramDefinition() {
            return mProgramDefinition;
        }
    }

    public static Section parseSection(byte[] data) {
        return null;
    }

    /**
     * 节目定义
     */
    public static class ProgramDefinition {
        private int mVersion;
        private Map<Integer, Integer> mContent;

        public ProgramDefinition(int version, Map<Integer, Integer> content) {
            mVersion = version;
            mContent = content;
        }

        /**
         * 版本
         */
        public int getVersion() {
            return mVersion;
        }

        /**
         * 是否过期
         */
        public boolean isExpired(int version) {
            return mVersion != version;
        }

        /**
         * 内容（elementary_PID与stream_type的映射关系）
         */
        public Map<Integer, Integer> getContent() {
            return mContent;
        }
    }
}
