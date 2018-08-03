package com.iptv.core.hls.playlist;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

final class AttributeList {
    private Map<String, String> mTable;

    private AttributeList() {
        mTable = new HashMap<String, String>();
    }

    /**
     * 是否包含指定名称的Attribute
     */
    public boolean containsAttribute(String attributeName) {
        return mTable.containsKey(attributeName);
    }

    /**
     * 获取指定名称的Attribute的值
     */
    public String getAttributeValue(String attributeName) {
        return mTable.get(attributeName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            /**
             * 同一个对象
             */
            return true;
        }

        if (obj instanceof AttributeList) {
            AttributeList other = (AttributeList)obj;

            if (size() != other.size()) {
                /**
                 * attribute的个数不同
                 */
                return false;
            }

            /**
             * 再比较attribute是否一致
             */
            for (String name : nameSet()) {
                 if (!other.containsAttribute(name)
                         || !other.getAttributeValue(name).equals(getAttributeValue(name))) {
                     /**
                      * 没有这个名称的attribute或者值不相等
                      */
                     return false;
                 }
            }

            return true;
        }
        else {
            return false;
        }
    }

    private int size() {
        return mTable.size();
    }

    private Set<String> nameSet() {
        return mTable.keySet();
    }

    private void add(Attribute attribute) {
        mTable.put(attribute.getName(), attribute.getValue());
    }

    public static AttributeList parse(String attributes) {
        AttributeList attributeList = new AttributeList();

        String[] attributeArray = attributes.split(",");
        for (int i = 0; i < attributeArray.length; i++) {
            attributeList.add(Attribute.parse(attributeArray[i]));
        }

        return attributeList;
    }
}
