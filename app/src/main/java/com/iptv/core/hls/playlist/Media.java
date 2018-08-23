package com.iptv.core.hls.playlist;

public final class Media {
    private static final String TYPE_VIDEO = "VIDEO";
    private static final String TYPE_AUDIO = "AUDIO";
    private static final String TYPE_SUBTITLES = "SUBTITLES";

    private AttributeList mAttributeList;

    /**
     * 构造函数
     */
    public Media(String attributeList) {
        mAttributeList = AttributeList.parse(attributeList);
    }

    /**
     * 是不是音频
     */
    public boolean isAudio() {
        String type = mAttributeList.getAttributeValue(Attribute.ATTR_TYPE);
        return type.equals(TYPE_AUDIO);
    }

    /**
     * 是不是视频
     */
    public boolean isVideo() {
        String type = mAttributeList.getAttributeValue(Attribute.ATTR_TYPE);
        return type.equals(TYPE_VIDEO);
    }

    /**
     * 是不是字幕
     */
    public boolean isSubtitle() {
        String type = mAttributeList.getAttributeValue(Attribute.ATTR_TYPE);
        return type.equals(TYPE_SUBTITLES);
    }

    /**
     * 获取所属组的ID
     */
    public String getGroupId() {
        return mAttributeList.getAttributeValue(Attribute.ATTR_GROUP_ID);
    }

    /**
     * 是否定义了媒体的URI
     */
    public boolean containsUri() {
        return mAttributeList.containsAttribute(Attribute.ATTR_URI);
    }

    /**
     * 获取媒体的URI
     */
    public String getUri() {
        return mAttributeList.getAttributeValue(Attribute.ATTR_URI);
    }

    /**
     * 是不是默认的选择
     */
    public boolean isDefault() {
        if (!mAttributeList.containsAttribute(Attribute.ATTR_DEFAULT)) {
            return false;
        }
        else {
            String isDefault = mAttributeList.getAttributeValue(Attribute.ATTR_DEFAULT);

            if (isDefault.equals("YES")) {
                return true;
            }
            else if (isDefault.equals("NO")) {
                return false;
            }
            else {
                throw new IllegalStateException("the value shall be YES or NO");
            }
        }
    }

    /**
     * 是不是自动选择
     */
    public boolean isAutoSelect() {
        if (!mAttributeList.containsAttribute(Attribute.ATTR_AUTO_SELECT)) {
            return false;
        }
        else {
            String autoSelect = mAttributeList.getAttributeValue(Attribute.ATTR_AUTO_SELECT);

            if (autoSelect.equals("YES")) {
                return true;
            }
            else if (autoSelect.equals("NO")) {
                return false;
            }
            else {
                throw new IllegalStateException("the value shall be YES or NO");
            }
        }
    }

    /**
     * 是否定义了语言
     */
    public boolean containsLanguage() {
        return mAttributeList.containsAttribute(Attribute.ATTR_LANGUAGE);
    }

    /**
     * 获取语言
     */
    public String getLanguage() {
        return mAttributeList.getAttributeValue(Attribute.ATTR_LANGUAGE);
    }
}
