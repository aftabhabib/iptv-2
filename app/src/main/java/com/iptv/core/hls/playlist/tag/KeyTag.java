package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.datatype.HexadecimalSequence;
import com.iptv.core.hls.playlist.datatype.QuotedString;

/**
 * 密钥标签
 */
public final class KeyTag extends Tag {
    private AttributeList mAttributeList;

    /**
     * 构造函数
     */
    public KeyTag(AttributeList attributeList) {
        super(Name.KEY);

        mAttributeList = attributeList;
    }

    /**
     * 获取属性列表
     */
    public AttributeList getAttributeList() {
        return mAttributeList;
    }

    @Override
    protected boolean containsValue() {
        return true;
    }

    @Override
    protected String getStringValue() {
        return mAttributeList.toString();
    }

    /**
     * 建造者
     */
    public static class Builder {
        private AttributeList mAttributeList = new AttributeList();

        /**
         * 构造函数
         */
        public Builder() {
            /**
             * nothing
             */
        }

        /**
         * 设置加密方式
         */
        public void setMethod(String method) {
            Attribute attribute = Attribute.create(Attribute.Name.METHOD, method);
            mAttributeList.put(attribute);
        }

        /**
         * 设置uri
         */
        public void setUri(String uri) {
            Attribute attribute = Attribute.create(Attribute.Name.URI, new QuotedString(uri));
            mAttributeList.put(attribute);
        }

        /**
         * 设置初始向量
         */
        public void setInitVector(byte[] iv) {
            Attribute attribute = Attribute.create(
                    Attribute.Name.IV, new HexadecimalSequence(iv));
            mAttributeList.put(attribute);
        }

        /**
         * 设置密钥格式
         */
        public void setFormat(String format) {
            Attribute attribute = Attribute.create(
                    Attribute.Name.KEY_FORMAT, new QuotedString(format));
            mAttributeList.put(attribute);
        }

        /**
         * 设置密钥格式符合的（各个）版本
         */
        public void setVersions(int[] versions) {
            String[] strVersions = toStringArray(versions);

            Attribute attribute = Attribute.create(
                    Attribute.Name.KEY_FORMAT_VERSIONS, new QuotedString(strVersions, '/'));
            mAttributeList.put(attribute);
        }

        /**
         * 整型数组转为字符串型数组
         */
        private static String[] toStringArray(int[] src) {
            String[] dst = new String[src.length];

            for (int i = 0; i < src.length; i++) {
                dst[i] = String.valueOf(src[i]);
            }

            return dst;
        }

        /**
         * 创建
         */
        public KeyTag build() {
            return new KeyTag(mAttributeList);
        }
    }
}
