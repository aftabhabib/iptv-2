package com.iptv.core.ts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * PAT
 */
class ProgramAssociationTable {
    private int mVersion;

    /**
     * The section_number of the first section in the Program Association Table shall be 0x00.
     * It shall be incremented by 1 with each additional section in the Program Association Table.
     */
    private int mNextSectionNumber = 0;
    private Map<Integer, Integer> mTable = new HashMap<Integer, Integer>();

    /**
     * 构造函数
     */
    public ProgramAssociationTable(int version) {
        mVersion = version;
    }

    /**
     * 获取版本
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * 获取下一个section的序号
     */
    public int getNextSectionNumber() {
        return mNextSectionNumber;
    }

    /**
     * 放入section
     */
    public void putSection(Section section) {
        mTable.putAll(section.getContent());

        mNextSectionNumber = section.getSectionNumber() + 1;
    }

    /**
     * 指定的PID是否是program_map_PID
     */
    public boolean isProgramMapPacketId(int packetId) {
        return mTable.containsValue(packetId);
    }

    /**
     * 获取已知节目的个数
     */
    public int size() {
        return mTable.size();
    }

    /**
     * 获取节目列表
     */
    public List<Program> getProgramList() {
        List<Program> programList = new ArrayList<Program>(mTable.size());

        Iterator<Integer> it = mTable.keySet().iterator();
        while (it.hasNext()) {
            programList.add(new Program(it.next()));
        }

        return programList;
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
         * section的序号
         */
        public int getSectionNumber() {
            return mIndex;
        }

        /**
         * 最后一个section的序号
         */
        public int getLastSectionNumber() {
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
