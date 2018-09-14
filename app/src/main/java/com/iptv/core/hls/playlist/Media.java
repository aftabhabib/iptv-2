package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.attribute.AttributeName;
import com.iptv.core.hls.playlist.datatype.EnumeratedString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 媒体
 */
public final class Media {
    private AttributeList mAttributeList = new AttributeList();

    /**
     * 构造函数
     */
    public Media() {
        /**
         * nothing
         */
    }

    /**
     * 设置类型
     */
    public void setType(String type) {
        if ((type == null) || !isValidType(type)) {
            throw new IllegalArgumentException("invalid type");
        }

        mAttributeList.putString(AttributeName.TYPE, type);
    }

    /**
     * 是否有效的类型
     */
    private static boolean isValidType(String type) {
        return type.equals(EnumeratedString.AUDIO)
                || type.equals(EnumeratedString.VIDEO)
                || type.equals(EnumeratedString.SUBTITLE)
                || type.equals(EnumeratedString.CLOSED_CAPTIONS);
    }

    /**
     * 设置uri
     */
    public void setUri(String uri) {
        if ((uri == null) || uri.isEmpty()) {
            throw new IllegalArgumentException("invalid uri");
        }

        mAttributeList.putString(AttributeName.URI, uri);
    }

    /**
     * 设置媒体所属（展示）组的id
     */
    public void setGroupId(String groupId) {
        if ((groupId == null) || groupId.isEmpty()) {
            throw new IllegalArgumentException("invalid group-id");
        }

        mAttributeList.putString(AttributeName.GROUP_ID, groupId);
    }

    /**
     * 设置语言
     */
    public void setLanguage(String language) {
        if ((language == null) || language.isEmpty()) {
            throw new IllegalArgumentException("invalid language");
        }

        mAttributeList.putString(AttributeName.LANGUAGE, language);
    }

    /**
     * 设置连带的语言
     */
    public void setAssociatedLanguage(String language) {
        if ((language == null) || language.isEmpty()) {
            throw new IllegalArgumentException("invalid language");
        }

        mAttributeList.putString(AttributeName.ASSOCIATED_LANGUAGE, language);
    }

    /**
     * 设置名称
     */
    public void setName(String name) {
        if ((name == null) || name.isEmpty()) {
            throw new IllegalArgumentException("invalid name");
        }

        mAttributeList.putString(AttributeName.NAME, name);
    }

    /**
     * 设置（该表现）是默认的选择
     */
    public void setDefaultSelect() {
        mAttributeList.putBoolean(AttributeName.DEFAULT, true);
    }

    /**
     * 设置（该表现）是自动的选择
     */
    public void setAutoSelect() {
        mAttributeList.putBoolean(AttributeName.AUTO_SELECT, true);
    }

    /**
     * 设置（该表现）是强制的选择
     */
    public void setForcedSelect() {
        mAttributeList.putBoolean(AttributeName.FORCED, true);
    }

    /**
     * 设置流内（CC字幕轨道）的id
     */
    public void setInStreamId(String streamId) {
        if ((streamId == null) || !isValidStreamId(streamId)) {
            throw new IllegalArgumentException("invalid stream id");
        }

        mAttributeList.putString(AttributeName.IN_STREAM_ID, streamId);
    }

    /**
     * 是否有效的流内（CC字幕轨道）的id
     */
    private static boolean isValidStreamId(String streamId) {
        Pattern regex = Pattern.compile("^(CC[1-4])|(SERVICE([1-9]|[1-5][0-9]|6[1-3]))$");

        Matcher matcher = regex.matcher(streamId);
        return matcher.find();
    }

    /**
     * 设置特性
     */
    public void setCharacteristics(String[] characteristics) {
        if ((characteristics == null) || (characteristics.length == 0)) {
            throw new IllegalArgumentException("invalid characteristics");
        }

        mAttributeList.putStringArray(AttributeName.CHARACTERISTICS, characteristics);
    }

    /**
     * 设置音频声道数
     */
    public void setAudioChannels(int channels) {
        if (channels <= 0 || channels > 8) {
            throw new IllegalArgumentException("invalid channels");
        }

        mAttributeList.putInteger(AttributeName.CHANNELS, channels);
    }

    /**
     * 是否定义了类型
     */
    public boolean containsType() {
        return mAttributeList.containsName(AttributeName.TYPE);
    }

    /**
     * 是否定义了uri
     */
    public boolean containsUri() {
        return mAttributeList.containsName(AttributeName.URI);
    }

    /**
     * 是否定义了媒体所属（展示）组的id
     */
    public boolean containsGroupId() {
        return mAttributeList.containsName(AttributeName.GROUP_ID);
    }

    /**
     * 是否定义了语言
     */
    public boolean containsLanguage() {
        return mAttributeList.containsName(AttributeName.LANGUAGE);
    }

    /**
     * 是否定义了连带的语言
     */
    public boolean containsAssociatedLanguage() {
        return mAttributeList.containsName(AttributeName.ASSOCIATED_LANGUAGE);
    }

    /**
     * 是否定义了名称
     */
    public boolean containsName() {
        return mAttributeList.containsName(AttributeName.NAME);
    }

    /**
     * 是否定义了流内（CC字幕轨道）的id
     */
    public boolean containsInStreamId() {
        return mAttributeList.containsName(AttributeName.IN_STREAM_ID);
    }

    /**
     * 是否定义了特性
     */
    public boolean containsCharacteristics() {
        return mAttributeList.containsName(AttributeName.CHARACTERISTICS);
    }

    /**
     * 是否定义了音频声道数
     */
    public boolean containsAudioChannels() {
        return mAttributeList.containsName(AttributeName.CHANNELS);
    }

    /**
     * 获取类型
     */
    public String getType() {
        return mAttributeList.getString(AttributeName.TYPE);
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mAttributeList.getString(AttributeName.URI);
    }

    /**
     * 获取媒体所属（展示）组的id
     */
    public String getGroupId() {
        return mAttributeList.getString(AttributeName.GROUP_ID);
    }

    /**
     * 获取语言
     */
    public String getLanguage() {
        return mAttributeList.getString(AttributeName.LANGUAGE);
    }

    /**
     * 获取与该表现连带的语言
     */
    public String getAssociatedLanguage() {
        return mAttributeList.getString(AttributeName.ASSOCIATED_LANGUAGE);
    }

    /**
     * 获取名称
     */
    public String getName() {
        return mAttributeList.getString(AttributeName.NAME);
    }

    /**
     * 是不是默认的选择
     */
    public boolean isDefaultSelect() {
        boolean state;
        if (!mAttributeList.containsName(AttributeName.DEFAULT)) {
            /**
             * 默认值：否
             */
            state = false;
        }
        else {
            state = mAttributeList.getBoolean(AttributeName.DEFAULT);
        }

        return state;
    }

    /**
     * 是不是自动的选择
     */
    public boolean isAutoSelect() {
        boolean state;
        if (!mAttributeList.containsName(AttributeName.AUTO_SELECT)) {
            /**
             * 默认值：否
             */
            state = false;
        }
        else {
            state = mAttributeList.getBoolean(AttributeName.AUTO_SELECT);
        }

        return state;
    }

    /**
     * 是不是强制的选择
     */
    public boolean isForcedSelect() {
        boolean state;
        if (!mAttributeList.containsName(AttributeName.FORCED)) {
            /**
             * 默认值：否
             */
            state = false;
        }
        else {
            state = mAttributeList.getBoolean(AttributeName.FORCED);
        }

        return state;
    }

    /**
     * 获取流内（CC字幕轨道）的id
     */
    public String getInStreamId() {
        return mAttributeList.getString(AttributeName.IN_STREAM_ID);
    }

    /**
     * 获取特性
     */
    public String[] getCharacteristics() {
        return mAttributeList.getStringArray(AttributeName.CHARACTERISTICS);
    }

    /**
     * 获取音频声道数
     */
    public int getAudioChannels() {
        return mAttributeList.getInteger(AttributeName.CHANNELS);
    }
}
