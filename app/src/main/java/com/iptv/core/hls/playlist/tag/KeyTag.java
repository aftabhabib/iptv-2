package com.iptv.core.hls.playlist.tag;

import com.iptv.core.hls.exception.MalformedPlaylistException;
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
     * 是否定义了属性
     */
    public boolean containsAttribute(String attributeName) {
        return mAttributeList.containsAttribute(attributeName);
    }

    /**
     * 获取加密方式
     */
    public String getMethod() {
        Attribute attribute = mAttributeList.get(Attribute.Name.METHOD);
        return attribute.getEnumeratedStringValue();
    }

    /**
     * 获取uri
     */
    public String getUri() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.URI);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取初始向量
     */
    public byte[] getInitVector() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.IV);
        return attribute.getHexadecimalSequenceValue().toByteArray();
    }

    /**
     * 获取密钥格式
     */
    public String getFormat() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.KEY_FORMAT);
        return attribute.getQuotedStringValue().getContent();
    }

    /**
     * 获取密钥格式符合的（各个）版本
     */
    public int[] getVersions() throws MalformedPlaylistException {
        Attribute attribute = mAttributeList.get(Attribute.Name.KEY_FORMAT_VERSIONS);

        String[] strVersions = attribute.getQuotedStringValue().splitContent("/");
        return toIntegerArray(strVersions);
    }

    /**
     * 字符串型数组转为整型数组
     */
    private static int[] toIntegerArray(String[] src) throws MalformedPlaylistException {
        int[] dst = new int[src.length];

        try {
            for (int i = 0; i < src.length; i++) {
                dst[i] = Integer.parseInt(src[i]);
            }
        }
        catch (NumberFormatException e) {
            throw new MalformedPlaylistException("should be decimal integer");
        }

        return dst;
    }

    @Override
    public String toString() {
        return mName + ":" + mAttributeList.toString();
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
