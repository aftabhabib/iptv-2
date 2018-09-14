package com.iptv.core.hls.playlist.attribute;

import com.iptv.core.hls.playlist.ByteRange;
import com.iptv.core.hls.playlist.datatype.Resolution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 属性列表
 */
public final class AttributeList {
    private Map<String, Object> mTable = new HashMap<>();

    /**
     * 构造函数
     */
    public AttributeList() {
        /**
         * nothing
         */
    }

    /**
     * 是否有属性
     */
    public boolean isEmpty() {
        return mTable.isEmpty();
    }

    /**
     * 获取属性个数
     */
    public int size() {
        return mTable.size();
    }

    /**
     * 是否包含指定名称的属性
     */
    public boolean containsName(String name) {
        return mTable.containsKey(name);
    }

    /**
     * 读（布尔型）属性值
     */
    public Boolean getBoolean(String name) {
        return (Boolean)get(name);
    }

    /**
     * 读（整型）属性值
     */
    public Integer getInteger(String name) {
        return (Integer)get(name);
    }

    /**
     * 读（长整型）属性值
     */
    public Long getLong(String name) {
        return (Long)get(name);
    }

    /**
     * 读（字节数组类型）属性值
     */
    public byte[] getByteArray(String name) {
        return (byte[])get(name);
    }

    /**
     * 读（整型数组类型）属性值
     */
    public int[] getIntArray(String name) {
        return (int[])get(name);
    }

    /**
     * 读（字符串数组类型）属性值
     */
    public String[] getStringArray(String name) {
        return (String[])get(name);
    }

    /**
     * 读（浮点型）属性值
     */
    public Float getFloat(String name) {
        return (Float)get(name);
    }

    /**
     * 读（字符串类型）属性值
     */
    public String getString(String name) {
        return (String)get(name);
    }

    /**
     * 读（分辨率类型）属性值
     */
    public Resolution getResolution(String name) {
        return (Resolution)get(name);
    }

    /**
     * 读（字节范围类型）属性值
     */
    public ByteRange getByteRange(String name) {
        return (ByteRange)get(name);
    }

    /**
     * 写（布尔型）属性值
     */
    public void putBoolean(String name, Boolean value) {
        mTable.put(name, value);
    }

    /**
     * 写（整型）属性值
     */
    public void putInteger(String name, Integer value) {
        mTable.put(name, value);
    }

    /**
     * 写（长整型）属性值
     */
    public void putLong(String name, Long value) {
        mTable.put(name, value);
    }

    /**
     * 写（字节数组类型）属性值
     */
    public void putByteArray(String name, byte[] value) {
        mTable.put(name, value);
    }

    /**
     * 写（整型数组类型）属性值
     */
    public void putIntArray(String name, int[] value) {
        mTable.put(name, value);
    }

    /**
     * 写（字符串数组类型）属性值
     */
    public void putStringArray(String name, String[] value) {
        mTable.put(name, value);
    }

    /**
     * 写（浮点型）属性值
     */
    public void putFloat(String name, Float value) {
        mTable.put(name, value);
    }

    /**
     * 写（字符串类型）属性值
     */
    public void putString(String name, String value) {
        mTable.put(name, value);
    }

    /**
     * 写（分辨率类型）属性值
     */
    public void putResolution(String name, Resolution value) {
        mTable.put(name, value);
    }

    /**
     * 写（字节范围类型）属性值
     */
    public void putByteRange(String name, ByteRange value) {
        mTable.put(name, value);
    }

    /**
     * 获取属性名集合
     */
    public Set<String> nameSet() {
        return mTable.keySet();
    }

    /**
     * 读属性值
     */
    public Object get(String name) {
        if (!containsName(name)) {
            throw new IllegalStateException("no " + name + " attribute");
        }

        return mTable.get(name);
    }
}
