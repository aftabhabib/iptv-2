package com.iptv.core.hls.playlist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Media {
    public static final String TYPE_VIDEO = "VIDEO";
    public static final String TYPE_AUDIO = "AUDIO";
    public static final String TYPE_SUBTITLE = "SUBTITLES";

    private Map<String, String> mAttributeTable;

    private Media(Map<String, String> attributeTable) {
        mAttributeTable = attributeTable;
    }

    /**
     * 获取类型
     */
    public String getType() {
        return mAttributeTable.get(Attribute.ATTR_TYPE);
    }

    /**
     * 获取所属组的ID
     */
    public String getGroupId() {
        return mAttributeTable.get(Attribute.ATTR_GROUP_ID);
    }

    /**
     * 是否定义了媒体的URI
     */
    public boolean containsUri() {
        return mAttributeTable.containsKey(Attribute.ATTR_URI);
    }

    /**
     * 获取媒体的URI
     */
    public String getUri() {
        return mAttributeTable.get(Attribute.ATTR_URI);
    }

    /**
     * 是不是默认的选择
     */
    public boolean isDefault() {
        if (!mAttributeTable.containsKey(Attribute.ATTR_DEFAULT)) {
            return false;
        }
        else {
            String isDefault = mAttributeTable.get(Attribute.ATTR_DEFAULT);

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
     * 是否定义了语言
     */
    public boolean containsLanguage() {
        return mAttributeTable.containsKey(Attribute.ATTR_LANGUAGE);
    }

    /**
     * 获取语言
     */
    public String getLanguage() {
        return mAttributeTable.get(Attribute.ATTR_LANGUAGE);
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

        public Media build() {
            return new Media(mAttributeTable);
        }
    }
}
