package com.iptv.core.hls.playlist;

final class Key {
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
     * 获取密钥的URI
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
    public String getInitVector() {
        return mAttributeList.getAttributeValue(Attribute.ATTR_IV);
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
