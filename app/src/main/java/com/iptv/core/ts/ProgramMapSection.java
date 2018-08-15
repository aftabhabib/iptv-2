package com.iptv.core.ts;

import com.iptv.core.utils.BitReader;

import java.util.HashMap;
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
        BitReader reader = new BitReader(data);
        if (reader.available() < 16 * 8) {
            return null;
        }

        int tableId = reader.readInt(8);
        if (tableId != 0x02) {
            return null;
        }

        reader.skip(1);
        reader.skip(1);
        reader.skip(2);
        int sectionLength = reader.readInt(12);
        /**
         * check section length
         */
        if ((sectionLength < 13)
                || (sectionLength > reader.available() / 8)) {
            return null;
        }

        int programNumber = reader.readInt(16);

        reader.skip(2);
        int version = reader.readInt(5);
        reader.skip(1);

        reader.skip(8);
        reader.skip(8);

        reader.skip(3);
        reader.skip(13);

        reader.skip(4);
        int programInfoLength = reader.readInt(12);

        if (programInfoLength > 0) {
            /**
             * check program info length
             */
            if (programInfoLength > sectionLength - 13) {
                return null;
            }

            /**
             * we do not care descriptor field
             */
            reader.skip(programInfoLength * 8);
        }

        Map<Integer, Element> elements = new HashMap<Integer, Element>();
        if (sectionLength - 13 - programInfoLength > 0) {
            int availableLength = sectionLength - 13 - programInfoLength;

            while (availableLength > 0) {
                /**
                 * check
                 */
                if (availableLength < 5) {
                    return null;
                }

                int streamType = reader.readInt(8);

                reader.skip(3);
                int packetId = reader.readInt(13);

                reader.skip(4);
                int esInfoLength = reader.readInt(12);

                if (esInfoLength > 0) {
                    /**
                     * check es info length
                     */
                    if (esInfoLength > availableLength - 5) {
                        return null;
                    }

                    /**
                     * we do not care descriptor field
                     */
                    reader.skip(esInfoLength * 8);
                }

                elements.put(packetId, new Element(streamType));

                availableLength -= (5 + esInfoLength);
            }
        }

        /**
         * FIXME: CRC verify
         */
        reader.skip(32);

        return new ProgramMapSection(programNumber, version, elements);
    }
}
