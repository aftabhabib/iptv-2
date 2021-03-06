package com.iptv.core.hls.playlist;

import com.iptv.core.hls.cache.Cache;
import com.iptv.core.hls.cache.CacheManager;
import com.iptv.core.hls.playlist.attribute.Attribute;
import com.iptv.core.hls.playlist.datatype.ByteRange;
import com.iptv.core.hls.playlist.datatype.EnumeratedString;
import com.iptv.core.hls.playlist.tag.ByteRangeTag;
import com.iptv.core.hls.playlist.tag.DiscontinuityTag;
import com.iptv.core.hls.playlist.tag.InfTag;
import com.iptv.core.hls.playlist.tag.KeyTag;
import com.iptv.core.hls.playlist.tag.MapTag;
import com.iptv.core.hls.utils.HttpHelper;
import com.iptv.core.hls.utils.UrlHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 片段
 */
public final class Segment {
    private MapTag mMapTag;
    private KeyTag mKeyTag;
    private ByteRangeTag mRangeTag;
    private DiscontinuityTag mDiscontinuityTag;
    private InfTag mInfTag;
    private String mUri;

    /**
     * 构造函数
     */
    public Segment(MapTag mapTag,
                   KeyTag keyTag,
                   ByteRangeTag rangeTag,
                   DiscontinuityTag discontinuityTag,
                   InfTag infTag,
                   String uri) {
        mMapTag = mapTag;
        mKeyTag = keyTag;
        mRangeTag = rangeTag;
        mDiscontinuityTag = discontinuityTag;
        mInfTag = infTag;
        mUri = uri;
    }

    /**
     * 获取时长
     */
    public float getDuration() {
        return mInfTag.getDuration();
    }

    /**
     * 是否不连续
     */
    public boolean isDiscontinuous() {
        return mDiscontinuityTag != null;
    }

    /**
     * 是否加密
     */
    public boolean isEncrypted() {
        if (mKeyTag == null) {
            return false;
        }
        else {
            return !mKeyTag.getMethod().equals(EnumeratedString.NONE);
        }
    }

    /**
     * 获取加密方式
     */
    public String getEncryptMethod() {
        return mKeyTag.getMethod();
    }

    /**
     * 获取解密密钥
     */
    public Cipher getDecryptCipher(String baseUri, long sequenceNumber)
            throws GeneralSecurityException, IOException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

        cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(getKey(baseUri), "AES"),
                new IvParameterSpec(getInitVector(sequenceNumber)));

        return cipher;
    }

    /**
     * 获取密钥数据
     */
    private byte[] getKey(String baseUri) throws IOException {
        Cache cache = CacheManager.getInstance().getCache("key_data");

        String url = UrlHelper.makeUrl(baseUri, mKeyTag.getUri());
        if (!cache.contains(url)) {
            cache.put(url, fetchKey(url));
        }

        return cache.get(url);
    }

    /**
     * 抓取密钥数据
     */
    private byte[] fetchKey(String url) throws IOException {
        if (mKeyTag.containsAttribute(Attribute.Name.KEY_FORMAT)) {
            String format = mKeyTag.getFormat();
            if (!format.equals("identity")) {
                throw new IllegalStateException("unsupported key format");
            }
        }

        return readByteArray(HttpHelper.get(url, null));
    }

    /**
     * 获取密钥初始化向量
     */
    private byte[] getInitVector(long sequenceNumber) {
        byte[] iv;

        if (mKeyTag.containsAttribute(Attribute.Name.IV)) {
            iv = mKeyTag.getInitVector();
        }
        else {
            /**
             * 根据序号生成
             */
            iv = new byte[16];

            iv[15] = (byte)(sequenceNumber & 0xff);
            iv[14] = (byte)((sequenceNumber >> 8) & 0xff);
            iv[13] = (byte)((sequenceNumber >> 16) & 0xff);
            iv[12] = (byte)((sequenceNumber >> 24) & 0xff);
        }

        return iv;
    }

    /**
     * 是否包含格式特殊数据
     */
    public boolean containsFormatSpecificData() {
        return mMapTag != null;
    }

    /**
     * 获取格式特殊数据
     */
    public byte[] getFormatSpecificData(String baseUri) throws IOException {
        Cache cache = CacheManager.getInstance().getCache("format_specific_data");

        String url = UrlHelper.makeUrl(baseUri, mMapTag.getUri());
        if (!cache.contains(url)) {
            cache.put(url, fetchFormatSpecificData(url));
        }

        return cache.get(url);
    }

    /**
     * 抓取初始化片段数据
     */
    private byte[] fetchFormatSpecificData(String url) throws IOException {
        Map<String, String> properties = null;
        if (mMapTag.containsAttribute(Attribute.Name.BYTE_RANGE)) {
            properties = setRangeHeader(mMapTag.getByteRange());
        }

        return readByteArray(HttpHelper.get(url, properties));
    }

    /**
     * 读数据
     */
    private static byte[] readByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            byte[] buf = new byte[1024];
            while (true) {
                int bytesRead = input.read(buf);
                if (bytesRead < 0) {
                    break;
                }

                output.write(buf, 0, bytesRead);
            }
        }
        catch (IOException e) {
            /**
             * 网络异常, 清空未完成的数据
             */
            output.reset();
        }
        finally {
            try {
                input.close();
            }
            catch (IOException e) {
                /**
                 * ignore
                 */
            }
        }

        if (output.size() == 0) {
            throw new IOException("read content error");
        }

        return output.toByteArray();
    }

    /**
     * 获取片段数据
     */
    public InputStream getContent(String baseUri) throws IOException {
        Map<String, String> properties = null;
        if (mRangeTag != null) {
            properties = setRangeHeader(mRangeTag.getRange());
        }

        return HttpHelper.get(UrlHelper.makeUrl(baseUri, mUri), properties);
    }

    /**
     * 设置Range头部
     */
    private static Map<String, String> setRangeHeader(ByteRange range) {
        Map<String, String> properties = new HashMap<>();

        long startPos = range.getOffset();
        long endPos = range.getOffset() + range.getLength() - 1;
        properties.put("Range", "byte=" + startPos + "-" + endPos);

        return properties;
    }
}
