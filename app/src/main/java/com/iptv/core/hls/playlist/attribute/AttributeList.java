package com.iptv.core.hls.playlist.attribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 属性列表
 */
public final class AttributeList {
    private Map<Integer, Attribute> mTable = new HashMap<>();

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
    public boolean containsAttribute(String name) {
        int key = name.hashCode();
        return mTable.containsKey(key);
    }

    /**
     * 获取指定名称的属性
     */
    public Attribute get(String name) {
        int key = name.hashCode();
        return mTable.get(key);
    }

    /**
     * 放入属性
     */
    public void put(Attribute attribute) {
        int key = attribute.getName().hashCode();
        mTable.put(key, attribute);
    }

    /**
     * 获取（属性）迭代器
     */
    public Iterator<Attribute> iterator() {
        return mTable.values().iterator();
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        boolean isFirstAttribute = true;
        for (Attribute attribute : mTable.values()) {
            if (!isFirstAttribute) {
                buffer.append(',');
            }

            buffer.append(attribute.toString());

            if (isFirstAttribute) {
                isFirstAttribute = false;
            }
        }

        return buffer.toString();
    }
}
