package com.iptv.core.hls.playlist;

import java.util.HashMap;
import java.util.Map;

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
