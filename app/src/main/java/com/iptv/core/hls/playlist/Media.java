package com.iptv.core.hls.playlist;

public final class Media {
    public static final String TYPE_VIDEO = "VIDEO";
    public static final String TYPE_AUDIO = "AUDIO";
    public static final String TYPE_SUBTITLES = "SUBTITLES";

    private AttributeList mAttributeList;

    private Media(AttributeList attributeList) {
        mAttributeList = attributeList;
    }

    /**
     * 获取类型
     */
    public String getType() {
        return mAttributeList.getAttributeValue(Attribute.ATTR_TYPE);
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
                throw new IllegalArgumentException("the value of DEFAULT attribute are YES or NO");
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
                throw new IllegalArgumentException("the value of AUTOSELECT attribute are YES or NO");
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

        public Media build() {
            return new Media(mAttributeList);
        }
    }
}
