package com.iptv.core.hls.playlist;

import com.iptv.core.hls.datatype.DecimalFloatingPoint;
import com.iptv.core.hls.datatype.DecimalInteger;
import com.iptv.core.hls.datatype.DecimalResolution;
import com.iptv.core.hls.datatype.EnumeratedString;
import com.iptv.core.hls.datatype.HexadecimalSequence;
import com.iptv.core.hls.datatype.QuotedString;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 属性
 */
public class Attribute {
    /**
     * 属性名
     */
    public static final String METHOD = "METHOD";
    public static final String IV = "IV";
    public static final String URI = "URI";
    public static final String KEY_FORMAT = "KEYFORMAT";
    public static final String KEY_FORMAT_VERSIONS = "KEYFORMATVERSIONS";
    public static final String TYPE = "TYPE";
    public static final String GROUP_ID = "GROUP-ID";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String ASSOCIATED_LANGUAGE = "ASSOC-LANGUAGE";
    public static final String NAME = "NAME";
    public static final String DEFAULT = "DEFAULT";
    public static final String AUTO_SELECT = "AUTOSELECT";
    public static final String FORCED = "FORCED";
    public static final String IN_STREAM_ID = "INSTREAM-ID";
    public static final String CHARACTERISTICS = "CHARACTERISTICS";
    public static final String CHANNELS = "CHANNELS";
    public static final String BANDWIDTH = "BANDWIDTH";
    public static final String AVG_BANDWIDTH = "AVERAGE-BANDWIDTH";
    public static final String CODECS = "CODECS";
    public static final String RESOLUTION = "RESOLUTION";
    public static final String FRAME_RATE = "FRAME-RATE";
    public static final String HDCP_LEVEL = "HDCP-LEVEL";
    public static final String AUDIO = "AUDIO";
    public static final String VIDEO = "VIDEO";
    public static final String SUBTITLES = "SUBTITLES";
    public static final String CLOSED_CAPTIONS = "CLOSED-CAPTIONS";

    /**
     * 加密方式
     */
    public static final String METHOD_AES_128 = "AES-128";
    public static final String METHOD_SAMPLE_AES = "SAMPLE-AES";

    /**
     * 密钥格式
     */
    public static final String FORMAT_IDENTITY = "identity";

    /**
     * 媒体类型
     */
    public static final String TYPE_VIDEO = "VIDEO";
    public static final String TYPE_AUDIO = "AUDIO";
    public static final String TYPE_SUBTITLE = "SUBTITLES";
    public static final String TYPE_CLOSED_CAPTIONS = "CLOSED-CAPTIONS";

    /**
     * HDCP类型
     */
    public static final String HDCP_TYPE0 = "TYPE-0";

    /**
     * ”没有“
     */
    public static final String VALUE_NONE = "NONE";

    /**
     * “是/否”
     */
    public static final String VALUE_YES = "YES";
    public static final String VALUE_NO = "NO";

    private Map<String, Object> mTable = new HashMap<String, Object>();

    /**
     * 构造函数
     */
    public Attribute() {
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
     * 读DecimalInteger类型的属性值
     */
    public DecimalInteger getDecimalInteger(String name) {
        return (DecimalInteger)get(name);
    }

    /**
     * 读HexadecimalSequence类型的属性值
     */
    public HexadecimalSequence getHexadecimalSequence(String name) {
        return (HexadecimalSequence)get(name);
    }

    /**
     * 读DecimalFloatingPoint类型的属性值
     */
    public DecimalFloatingPoint getDecimalFloatingPoint(String name) {
        return (DecimalFloatingPoint)get(name);
    }

    /**
     * 读QuotedString类型的属性值
     */
    public QuotedString getQuotedString(String name) {
        return (QuotedString)get(name);
    }

    /**
     * 读EnumeratedString类型的属性值
     */
    public EnumeratedString getEnumeratedString(String name) {
        return (EnumeratedString)get(name);
    }

    /**
     * 读DecimalResolution类型的属性值
     */
    public DecimalResolution getDecimalResolution(String name) {
        return (DecimalResolution)get(name);
    }

    /**
     * 读属性值
     */
    private Object get(String name) {
        if (!containsName(name)) {
            throw new IllegalStateException("no " + name + " attribute");
        }

        return mTable.get(name);
    }

    /**
     * 写DecimalInteger类型的属性值
     */
    public void putDecimalInteger(String name, DecimalInteger value) {
        mTable.put(name, value);
    }

    /**
     * 写HexadecimalSequence类型的属性值
     */
    public void putHexadecimalSequence(String name, HexadecimalSequence value) {
        mTable.put(name, value);
    }

    /**
     * 写DecimalFloatingPoint类型的属性值
     */
    public void putDecimalFloatingPoint(String name, DecimalFloatingPoint value) {
        mTable.put(name, value);
    }

    /**
     * 写QuotedString类型的属性值
     */
    public void putQuotedString(String name, QuotedString value) {
        mTable.put(name, value);
    }

    /**
     * 写EnumeratedString类型的属性值
     */
    public void putEnumeratedString(String name, EnumeratedString value) {
        mTable.put(name, value);
    }

    /**
     * 写DecimalResolution类型的属性值
     */
    public void putDecimalResolution(String name, DecimalResolution value) {
        mTable.put(name, value);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        int attributeCnt = 0;
        for (String name : nameSet()) {
            /**
             * 多个属性之间使用","分隔
             */
            if (attributeCnt > 0) {
                buffer.append(",");
            }

            buffer.append(name);
            buffer.append("=");
            buffer.append(get(name).toString());

            attributeCnt++;
        }

        return buffer.toString();
    }

    /**
     * 获取属性名集合
     */
    private Set<String> nameSet() {
        return mTable.keySet();
    }
}
