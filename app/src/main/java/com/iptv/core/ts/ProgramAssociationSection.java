package com.iptv.core.ts;

import com.iptv.core.utils.BitReader;

import java.util.HashMap;
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
        BitReader reader = new BitReader(data);

        int tableId = reader.readInt(8);
        if (tableId != 0x00) {
            return null;
        }

        reader.skip(1);
        reader.skip(1);
        reader.skip(2);
        int sectionLength = reader.readInt(12);
        /**
         * check section length
         */
        if ((sectionLength < 9)
                || (sectionLength > reader.available() / 8)
                || ((sectionLength - 9) % 4 > 0)) {
            return null;
        }

        reader.skip(16);

        reader.skip(2);
        int version = reader.readInt(5);
        reader.skip(1);

        int sectionNumber = reader.readInt(8);
        int lastSectionNumber = reader.readInt(8);

        Map<Integer, Program> association = new HashMap<Integer, Program>();

        int count = (sectionLength - 9) / 4;
        while (count > 0) {
            int programNumber = reader.readInt(16);

            reader.skip(3);
            int packetId = reader.readInt(13);

            if (programNumber == 0) {
                /**
                 * network_PID, ignore
                 */
            }
            else {
                association.put(packetId, new Program(programNumber));
            }

            count--;
        }

        /**
         * FIXME: CRC verify
         */
        reader.skip(32);

        return new ProgramAssociationSection(version, sectionNumber, lastSectionNumber, association);
    }
}
