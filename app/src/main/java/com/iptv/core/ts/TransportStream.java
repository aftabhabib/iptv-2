package com.iptv.core.ts;

import java.util.Iterator;
import java.util.Map;

public class TransportStream {
    private static final int PAT_PACKET_ID = 0x00;

    private ProgramAssociationTable mProgramAssociationTable;
    private ProgramMapTable mProgramMapTable;

    public TransportStream() {
        /**
         * nothing
         */
    }

    public boolean isProgramAssociationTablePacketId(int packetId) {
        return packetId == PAT_PACKET_ID;
    }

    public void addProgramAssociationSection(ProgramAssociationTable.Section section) {
        if ((mProgramAssociationTable == null)
                || mProgramAssociationTable.isNewVersion(section.getTableVersion())) {
            /**
             * PAT未创建或者PAT的版本有变化
             */
            if ((mProgramMapTable != null) && !mProgramMapTable.isEmpty()) {
                mProgramMapTable.clear();
            }

            mProgramAssociationTable = new ProgramAssociationTable(
                    section.getTableVersion(), section.getLastNumber());
        }

        if (!mProgramAssociationTable.isComplete()) {
            /**
             * PAT没有完
             */
            mProgramAssociationTable.addSection(section);
        }
    }

    public boolean isProgramMapTablePacketId(int packetId) {
        if (mProgramAssociationTable == null) {
            return false;
        }

        return mProgramAssociationTable.containsValue(packetId);
    }

    public void addProgramMapSection(ProgramMapTable.Section section) {
        if (mProgramMapTable == null) {
            mProgramMapTable = new ProgramMapTable();
        }

        mProgramMapTable.addSection(section);
    }

    public int getProgramCount() {
        return mProgramAssociationTable.size();
    }

    public int getProgramNumber(int index) {
        if (index < 0 || index >= getProgramCount()) {
            throw new IllegalArgumentException("bad index");
        }

        Iterator<Integer> iterator = mProgramAssociationTable.keySet().iterator();

        int programNumber;
        do {
            programNumber = iterator.next();
        }
        while (--index >= 0);

        return programNumber;
    }

    public boolean containsProgramMapping(int programNumber) {
        if (mProgramMapTable == null) {
            return false;
        }

        return mProgramMapTable.containsKey(programNumber);
    }

    public boolean isProgramElementaryPacketId(int programNumber, int packetId) {
        if (!containsProgramMapping(programNumber)) {
            return false;
        }

        return mProgramMapTable.get(programNumber).isElementaryPacketId(packetId);
    }

    public int getProgramElementType(int programNumber, int packetId) {
        if (!containsProgramMapping(programNumber)) {
            throw new IllegalStateException();
        }

        return mProgramMapTable.get(programNumber).getElementType(packetId);
    }
}
