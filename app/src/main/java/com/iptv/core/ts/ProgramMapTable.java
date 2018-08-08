package com.iptv.core.ts;

import java.util.HashMap;
import java.util.Map;

class ProgramMapTable extends HashMap<Integer, ProgramMapTable.ProgramDefinition> {
    public ProgramMapTable() {
        super();
    }

    public void addSection(Section section) {
        if (!containsKey(section.getProgramNumber())) {
            /**
             * 还没有这个节目的定义
             */
            put(section.getProgramNumber(),
                    new ProgramDefinition(section.getVersion(), section.getElementTable()));
        }
        else {
            /**
             * 已经有这个节目的定义了，检查版本
             */
            ProgramDefinition programDef = get(section.getProgramNumber());
            if (programDef.isNewVersion(section.getVersion())) {
                /**
                 * 版本更新
                 */
                put(section.getProgramNumber(),
                        new ProgramDefinition(section.getVersion(), section.getElementTable()));
            }
        }
    }

    public static class Section {
        private int mVersion;

        private int mProgramNumber;
        private Map<Integer, Integer> mElementTable;

        public Section(int version, int programNumber, Map<Integer, Integer> elementTable) {
            mVersion = version;

            mProgramNumber = programNumber;
            mElementTable = elementTable;
        }

        public int getVersion() {
            return mVersion;
        }

        public int getProgramNumber() {
            return mProgramNumber;
        }

        public Map<Integer, Integer> getElementTable() {
            return mElementTable;
        }
    }

    class ProgramDefinition {
        private int mVersion;
        private Map<Integer, Integer> mElementTable;

        public ProgramDefinition(int version, Map<Integer, Integer> elementTable) {
            mVersion = version;
            mElementTable = elementTable;
        }

        public boolean isNewVersion(int version) {
            return mVersion != version;
        }

        public boolean isElementaryPacketId(int packetId) {
            return mElementTable.containsKey(packetId);
        }

        public int getElementType(int packetId) {
            if (!mElementTable.containsKey(packetId)) {
                throw new IllegalStateException();
            }

            return mElementTable.get(packetId);
        }
    }
}
