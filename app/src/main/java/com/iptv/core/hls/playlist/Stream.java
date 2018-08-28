package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;
import com.iptv.core.utils.MalformedFormatException;

/**
 * 流
 */
public final class Stream {
    /**
     * 属性
     */
    private static final String ATTR_BANDWIDTH = "BANDWIDTH";
    private static final String ATTR_AUDIO = "AUDIO";
    private static final String ATTR_VIDEO = "VIDEO";
    private static final String ATTR_SUBTITLES = "SUBTITLES";

    private MetaData mMetaData = new MetaData();
    private String mUri;

    /**
     * 构造函数
     */
    public Stream(String attributeList) throws MalformedFormatException {
        String[] attributes = attributeList.split(",");
        for (int i = 0; i < attributes.length; i++) {
            String[] result = attributes[i].split("=");

            if (result[0].equals(ATTR_BANDWIDTH)) {
                int bandwidth = Integer.parseInt(result[1]);
                mMetaData.putInteger(MetaData.KEY_BANDWIDTH, bandwidth);
            }
            else if (result[0].equals(ATTR_AUDIO)) {
                mMetaData.putString(MetaData.KEY_AUDIO_GROUP_ID, result[1]);
            }
            else if (result[0].equals(ATTR_VIDEO)) {
                mMetaData.putString(MetaData.KEY_VIDEO_GROUP_ID, result[1]);
            }
            else if (result[0].equals(ATTR_SUBTITLES)) {
                mMetaData.putString(MetaData.KEY_SUBTITLE_GROUP_ID, result[1]);
            }
            else {
                /**
                 * not support yet
                 */
            }
        }

        /**
         * 检查必要参数
         */
        if (!mMetaData.containsKey(MetaData.KEY_BANDWIDTH)) {
            throw new MalformedFormatException("bandwidth is required");
        }
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        mUri = uri;
    }

    /**
     * 获取带宽
     */
    public int getBandwidth() {
        return mMetaData.getInteger(MetaData.KEY_BANDWIDTH);
    }

    /**
     * 获取AudioGroup的id
     */
    public String getAudioGroupId() {
        if (!mMetaData.containsKey(MetaData.KEY_AUDIO_GROUP_ID)) {
            return "";
        }
        else {
            return mMetaData.getString(MetaData.KEY_AUDIO_GROUP_ID);
        }
    }

    /**
     * 获取VideoGroup的id
     */
    public String getVideoGroupId() {
        if (!mMetaData.containsKey(MetaData.KEY_VIDEO_GROUP_ID)) {
            return "";
        }
        else {
            return mMetaData.getString(MetaData.KEY_VIDEO_GROUP_ID);
        }
    }

    /**
     * 获取SubtitleGroup的id
     */
    public String getSubtitleGroupId() {
        if (!mMetaData.containsKey(MetaData.KEY_SUBTITLE_GROUP_ID)) {
            return "";
        }
        else {
            return mMetaData.getString(MetaData.KEY_SUBTITLE_GROUP_ID);
        }
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mUri;
    }
}
