package com.iptv.core.hls.playlist;

import java.util.List;

public final class VariantStream {
    private AttributeList mAttributeList;
    private String mUri;

    /**
     * 构造函数
     */
    public VariantStream(String attributeList, String uri) {
        mAttributeList = AttributeList.parse(attributeList);
        mUri = uri;
    }

    /**
     * 获取带宽
     */
    public int getBandwidth() {
        String bandwidth = mAttributeList.getAttributeValue(Attribute.ATTR_BANDWIDTH);

        return Integer.parseInt(bandwidth);
    }

    /**
     * 获取URI
     */
    public String getUri() {
        return mUri;
    }

    /**
     * 是否定义了VideoCodec
     */
    public boolean containsVideoCodec() {
        List<Codec> codecList = Codec.parseList(mAttributeList.getAttributeValue(Attribute.ATTR_CODECS));

        for (Codec codec : codecList) {
            if (codec.isVideoType()) {
                return true;
            }
        }

        return false;
    }

    /**
     * 是否定义了AudioCodec
     */
    public boolean containsAudioCodec() {
        List<Codec> codecList = Codec.parseList(mAttributeList.getAttributeValue(Attribute.ATTR_CODECS));

        for (Codec codec : codecList) {
            if (codec.isAudioType()) {
                return true;
            }
        }

        return false;
    }

    /**
     * 是否定义了VideoRendition
     */
    public boolean containsVideoRendition() {
        return mAttributeList.containsAttribute(Attribute.ATTR_VIDEO);
    }

    /**
     * 获取VideoRendition的GroupId
     */
    public String getVideoGroupId() {
        return mAttributeList.getAttributeValue(Attribute.ATTR_VIDEO);
    }

    /**
     * 是否定义了AudioRendition
     */
    public boolean containsAudioRendition() {
        return mAttributeList.containsAttribute(Attribute.ATTR_AUDIO);
    }

    /**
     * 获取AudioRendition的GroupId
     */
    public String getAudioGroupId() {
        return mAttributeList.getAttributeValue(Attribute.ATTR_AUDIO);
    }

    /**
     * 是否定义了SubtitleRendition
     */
    public boolean containsSubtitleRendition() {
        return mAttributeList.containsAttribute(Attribute.ATTR_SUBTITLES);
    }

    /**
     * 获取SubtitleRendition的GroupId
     */
    public String getSubtitleGroupId() {
        return mAttributeList.getAttributeValue(Attribute.ATTR_SUBTITLES);
    }
}
