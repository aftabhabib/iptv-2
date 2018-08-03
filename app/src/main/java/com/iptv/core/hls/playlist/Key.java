package com.iptv.core.hls.playlist;

import java.math.BigInteger;

public final class Key {
    public static final String METHOD_NONE = "NONE";
    public static final String METHOD_AES_128 = "AES-128";
    public static final String METHOD_SAMPLE_AES = "SAMPLE-AES";

    private AttributeList mAttributeList;

    private Key(AttributeList attributeList) {
        mAttributeList = attributeList;
    }

    /**
     * 获取加密方式
     */
    public String getMethod() {
        return mAttributeList.getAttributeValue(Attribute.ATTR_METHOD);
    }

    /**
     * 获取密钥的URI（如果METHOD的值不是NONE）
     */
    public String getUri() {
        return mAttributeList.getAttributeValue(Attribute.ATTR_URI);
    }

    /**
     * 是否定义了IV
     */
    public boolean containsInitVector() {
        return mAttributeList.containsAttribute(Attribute.ATTR_IV);
    }

    /**
     * 获取IV
     */
    public byte[] getInitVector() {
        String initVector = mAttributeList.getAttributeValue(Attribute.ATTR_IV);

        /**
         * 去除16进制表示前缀
         */
        if (initVector.startsWith("0x") || initVector.startsWith("0X")) {
            initVector.substring(2);
        }

        return new BigInteger(initVector, 16).toByteArray();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            /**
             * 同一个对象
             */
            return true;
        }

        if (obj instanceof Key) {
            Key other = (Key)obj;

            /**
             * attribute list是否有相同的内容
             */
            return mAttributeList.equals(other.mAttributeList);
        }
        else {
            return false;
        }
    }

    public static class Builder {
        private AttributeList mAttributeList;

        public Builder() {
            /**
             * nothing
             */
        }

        public void setAttributeList(String attributeList) {
            mAttributeList = AttributeList.parse(attributeList);
        }

        public Key build() {
            return new Key(mAttributeList);
        }
    }
}
