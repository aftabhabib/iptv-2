package com.iptv.core.ts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PAT
 */
final class ProgramAssociationTable {
    private int mVersion;
    private Map<Integer, Program> mTable;

    /**
     * 构造函数
     */
    public ProgramAssociationTable(int version) {
        mVersion = version;
        mTable = new HashMap<Integer, Program>();
    }

    /**
     * 获取版本
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * 放入新的关联
     */
    public void putAssociations(Map<Integer, Program> associations) {
        mTable.putAll(associations);
    }

    /**
     * 是否包含对应program_map_PID的节目
     */
    public boolean containsProgram(int packetId) {
        return mTable.containsKey(packetId);
    }

    /**
     * 获取对应program_map_PID的节目
     */
    public Program getProgram(int packetId) {
        if (!containsProgram(packetId)) {
            return null;
        }

        return mTable.get(packetId);
    }
}
