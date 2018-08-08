package com.iptv.core.ts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class ProgramAssociationTable extends HashMap<Integer, Integer> {
    private int mVersion;
    private boolean[] mReceivedSection;

    public ProgramAssociationTable(int version, int sectionSize) {
        super();

        mVersion = version;

        mReceivedSection = new boolean[sectionSize];
        Arrays.fill(mReceivedSection, false);
    }

    public boolean isNewVersion(int version) {
        return mVersion != version;
    }

    public void addSection(Section section) {
        if (!mReceivedSection[section.getNumber()]) {
            putAll(section.getSegment());

            mReceivedSection[section.getNumber()] = true;
        }
    }

    public boolean isComplete() {
        for (int i = 0; i < mReceivedSection.length; i++) {
            if (!mReceivedSection[i]) {
                return false;
            }
        }

        return true;
    }

    public static class Section {
        private int mTableVersion;

        private int mNumber;
        private int mLastNumber;

        private int mTransportStreamId;
        private Map<Integer, Integer> mSegment;

        public Section(int tableVersion, int number, int lastNumber,
                       int transportStreamId, Map<Integer, Integer> segment) {
            mTableVersion = tableVersion;

            mNumber = number;
            mLastNumber = lastNumber;

            mTransportStreamId = transportStreamId;
            mSegment = segment;
        }

        public int getTableVersion() {
            return mTableVersion;
        }

        public int getNumber() {
            return mNumber;
        }

        public int getLastNumber() {
            return mLastNumber;
        }

        public int getTransportStreamId() {
            return mTransportStreamId;
        }

        public Map<Integer, Integer> getSegment() {
            return mSegment;
        }
    }
}
