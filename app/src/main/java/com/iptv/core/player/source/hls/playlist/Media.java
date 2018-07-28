package com.iptv.core.player.source.hls.playlist;

public class Media {
    private static final String TYPE_VIDEO = "VIDEO";
    private static final String TYPE_AUDIO = "AUDIO";
    private static final String TYPE_SUBTITLE = "SUBTITLES";

    /**
     * required
     */
    private String mType;
    private String mUri;
    private String mGroupId;

    /**
     * optional
     */
    private String mLanguage;
    private String mName;
    private boolean mIsDefault;
    private boolean mIsAutoSelect;

    private Media() {
        //
    }

    public boolean isVideo() {
        return mType.equals(TYPE_VIDEO);
    }

    public boolean isAudio() {
        return mType.equals(TYPE_AUDIO);
    }

    public boolean isSubtitle() {
        return mType.equals(TYPE_SUBTITLE);
    }

    public String getUri() {
        return mUri;
    }

    public boolean isDefault() {
        return mIsDefault;
    }

    public boolean isAutoSelect() {
        return mIsAutoSelect;
    }

    private void setType(String type) {
        mType = type;
    }

    private void setUri(String uri) {
        mUri = uri;
    }

    private void setGroupId(String groupId) {
        mGroupId = groupId;
    }

    private void setLanguage(String language) {
        mLanguage = language;
    }

    private void setName(String name) {
        mName = name;
    }

    private void setDefault(boolean isDefault) {
        mIsDefault = isDefault;
    }

    private void setAutoSelect(boolean isAutoSelect) {
        mIsAutoSelect = isAutoSelect;
    }

    public static Media parse(String attributeList) {
        Media media = new Media();

        String[] attributeArray = attributeList.split(",");
        for (int i = 0; i < attributeArray.length; i++) {
            Attribute attribute = Attribute.parse(attributeArray[i]);

            if (attribute.getKey().equals(Attribute.ATTR_TYPE)) {
                media.setType(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_URI)) {
                media.setUri(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_GROUP_ID)) {
                media.setGroupId(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_LANGUAGE)) {
                media.setLanguage(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_NAME)) {
                media.setName(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_DEFAULT)) {
                if (attribute.getValue().equals("YES")) {
                    media.setDefault(true);
                }
                else if (attribute.getValue().equals("NO")) {
                    media.setDefault(false);
                }
                else {
                    throw new IllegalArgumentException("must be YES or NO");
                }
            }
            else if (attribute.getKey().equals(Attribute.ATTR_AUTO_SELECT)) {
                if (attribute.getValue().equals("YES")) {
                    media.setAutoSelect(true);
                }
                else if (attribute.getValue().equals("NO")) {
                    media.setAutoSelect(false);
                }
                else {
                    throw new IllegalArgumentException("must be YES or NO");
                }
            }
        }

        return media;
    }
}
