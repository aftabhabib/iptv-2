package com.iptv.core.player;

import java.util.HashMap;
import java.util.Map;

/**
 * 元信息
 */
public class MetaData {
    private Map<String, Object> mTable;

    /**
     * 构造函数
     */
    public MetaData() {
        mTable = new HashMap<String, Object>();
    }

    /**
     * 是否包含
     */
    public boolean containsKey(String key) {
        return mTable.containsKey(key);
    }

    /**
     * 以字符串类型读值
     */
    public String getString(String key) {
        return (String)mTable.get(key);
    }

    /**
     * 以long类型读值
     */
    public long getLong(String key) {
        return (long)mTable.get(key);
    }

    /**
     * 以int类型读值
     */
    public int getInteger(String key) {
        return (int)mTable.get(key);
    }

    /**
     * 以float类型读值
     */
    public float getFloat(String key) {
        return (float)mTable.get(key);
    }

    /**
     * 以byte数组类型读值
     */
    public byte[] getByteArray(String key) {
        return (byte[])mTable.get(key);
    }

    /**
     * 放入字符串类型的值
     */
    public void putString(String key, String value) {
        mTable.put(key, value);
    }

    /**
     * 放入long类型的值
     */
    public void putLong(String key, long value) {
        mTable.put(key, value);
    }

    /**
     * 放入int类型的值
     */
    public void putInteger(String key, int value) {
        mTable.put(key, value);
    }

    /**
     * 放入float类型的值
     */
    public void putFloat(String key, float value) {
        mTable.put(key, value);
    }

    /**
     * 放入byte数组类型的值
     */
    public void putByteArray(String key, byte[] value) {
        mTable.put(key, value);
    }
}
