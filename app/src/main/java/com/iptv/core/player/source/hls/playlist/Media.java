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

    public String getGroupId() {
        return mGroupId;
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

    public String getLanguage() {
        return mLanguage;
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

    private void setGroupId(String groupId) {
        mGroupId = groupId;
    }

    private void setUri(String uri) {
        mUri = uri;
    }

    private void setLanguage(String language) {
        mLanguage = language;
    }

    private void setName(String name) {
        mName = name;
    }

    private void setDefault() {
        mIsDefault = true;
    }

    private void setAutoSelect() {
        mIsAutoSelect = true;
    }

    public static class Builder {
        /**
         * required
         */
        private String mType;
        private String mGroupId;

        /**
         * optional
         */
        private String mUri;
        private String mLanguage;
        private String mName;
        private boolean mIsDefault;
        private boolean mIsAutoSelect;

        public Builder() {
            //
        }

        public void setType(String type) {
            mType = type;
        }

        public void setGroupId(String groupId) {
            mGroupId = groupId;
        }

        public void setUri(String uri) {
            mUri = uri;
        }

        public void setLanguage(String language) {
            mLanguage = language;
        }

        public void setName(String name) {
            mName = name;
        }

        public void setDefault() {
            mIsDefault = true;
        }

        public void setAutoSelect() {
            mIsAutoSelect = true;
        }

        public Media build() {
            Media media = new Media();

            media.setType(mType);
            media.setGroupId(mGroupId);

            if (mUri != null) {
                media.setUri(mUri);
            }

            if (mLanguage != null) {
                media.setLanguage(mLanguage);
            }

            if (mName != null) {
                media.setName(mName);
            }

            if (mIsDefault) {
                media.setDefault();
            }

            if (mIsAutoSelect) {
                media.setAutoSelect();
            }

            return media;
        }
    }
}
