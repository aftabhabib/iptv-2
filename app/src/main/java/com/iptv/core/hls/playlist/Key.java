package com.iptv.core.hls.playlist;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Key {
    public static final String METHOD_NONE = "NONE";
    public static final String METHOD_AES_128 = "AES-128";
    public static final String METHOD_SAMPLE_AES = "SAMPLE-AES";

    private Map<String, String> mAttributeTable;

    private Key(Map<String, String> attributeTable) {
        mAttributeTable = attributeTable;
    }

    /**
     * 是否加密
     */
    public boolean isEncrypted() {
        String method = mAttributeTable.get(Attribute.ATTR_METHOD);

        return method.equals(METHOD_NONE);
    }

    /**
     * 获取加密方式
     */
    public String getMethod() {
        return mAttributeTable.get(Attribute.ATTR_METHOD);
    }

    /**
     * 获取密钥的URI
     */
    public String getUri() {
        return mAttributeTable.get(Attribute.ATTR_URI);
    }

    /**
     * 是否定义了IV
     */
    public boolean containsInitVector() {
        return mAttributeTable.containsKey(Attribute.ATTR_IV);
    }

    /**
     * 是否相同
     */
    public boolean isSame(Key other) {
        if (mAttributeTable.entrySet().size() != other.mAttributeTable.entrySet().size()) {
            return false;
        }

        for (Map.Entry<String, String> entry: mAttributeTable.entrySet()) {
            String attrName = entry.getKey();
            String attrValue = entry.getValue();

            if (!other.mAttributeTable.containsKey(attrName)) {
                return false;
            }

            if (!attrValue.equals(other.mAttributeTable.get(attrName))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取IV
     */
    public byte[] getInitVector() {
        String initVector = mAttributeTable.get(Attribute.ATTR_IV);
        if (initVector.startsWith("0x")) {
            initVector.substring(2);
        }

        return new BigInteger(initVector, 16).toByteArray();
    }

    public static class Builder {
        private Map<String, String> mAttributeTable;

        public Builder() {
            mAttributeTable = new HashMap<String, String>();
        }

        public void setAttributeList(List<Attribute> attributeList) {
            for (int i = 0; i < attributeList.size(); i++) {
                Attribute attribute = attributeList.get(i);

                mAttributeTable.put(attribute.getKey(), attribute.getValue());
            }
        }

        public Key build() {
            return new Key(mAttributeTable);
        }
    }
}
