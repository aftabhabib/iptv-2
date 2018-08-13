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
    private Map<Integer, Integer> mTable;

    /**
     * 构造函数
     */
    public ProgramAssociationTable(int version) {
        mVersion = version;
        mTable = new HashMap<Integer, Integer>();
    }

    /**
     * 获取版本
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * 放入节目关联（program_number与program_map_PID的对应关系）
     */
    public void putAssociations(Map<Integer, Integer> associations) {
        mTable.putAll(associations);
    }

    /**
     * 指定的PID是否是program_map_PID
     */
    public boolean isMapPacketId(int packetId) {
        return mTable.containsValue(packetId);
    }

    /**
     * 返回节目数组
     */
    public Program[] toProgramArray() {
        List<Program> programList = new ArrayList<Program>(mTable.size());

        for (Integer programNumber : mTable.keySet()) {
            programList.add(new Program(programNumber));
        }

        /**
         * 按节目号排序
         */
        Collections.sort(programList, new Comparator<Program>() {
            @Override
            public int compare(Program program1, Program program2) {
                return program1.getProgramNumber() - program2.getProgramNumber();
            }
        });

        return programList.toArray(new Program[mTable.size()]);
    }
}
