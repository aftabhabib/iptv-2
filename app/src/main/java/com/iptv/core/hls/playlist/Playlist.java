package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;
import com.iptv.core.utils.MalformedFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 播放列表
 */
public final class Playlist {
    /**
     * 标签
     */
    private static final String TAG_M3U = "#EXTM3U";
    private static final String TAG_VERSION = "#EXT-X-VERSION";
    private static final String TAG_TARGET_DURATION = "#EXT-X-TARGETDURATION";
    private static final String TAG_INF = "#EXTINF";
    private static final String TAG_END_LIST = "#EXT-X-ENDLIST";

    private static final String TAG_MEDIA_SEQUENCE = "#EXT-X-MEDIA-SEQUENCE";
    private static final String TAG_DISCONTINUITY = "#EXT-X-DISCONTINUITY";
    private static final String TAG_KEY = "#EXT-X-KEY";
    private static final String TAG_BYTE_RANGE = "#EXT-X-BYTERANGE";

    private static final String TAG_MEDIA = "#EXT-X-MEDIA";
    private static final String TAG_STREAM_INF = "#EXT-X-STREAM-INF";

    /**
     * 属性
     */
    private static final String ATTR_TYPE = "TYPE";
    private static final String ATTR_GROUP_ID = "GROUP-ID";
    private static final String ATTR_LANGUAGE = "LANGUAGE";
    private static final String ATTR_DEFAULT = "DEFAULT";
    private static final String ATTR_URI = "URI";

    private static final String ATTR_BANDWIDTH = "BANDWIDTH";
    private static final String ATTR_AUDIO = "AUDIO";
    private static final String ATTR_VIDEO = "VIDEO";
    private static final String ATTR_SUBTITLES = "SUBTITLES";

    private static final String ATTR_METHOD = "METHOD";
    private static final String ATTR_IV = "IV";

    private String mUrl;
    private MetaData mMetaData = new MetaData();

    private List<Media> mMediaList = new ArrayList<Media>();

    private List<Stream> mStreamList = new ArrayList<Stream>();
    private Stream mPendingStream = null;

    private Key mKey = null;
    private long mRangeOffset = 0;

    private List<Segment> mSegmentList = new ArrayList<Segment>();
    private Segment mPendingSegment = null;

    /**
     * 构造函数
     */
    public Playlist(String url, String content) {
        mUrl = url;

        parse(content);
    }

    /**
     * 解析内容
     */
    private void parse(String content) {
        BufferedReader reader = new BufferedReader(new StringReader(content));

        try {
            String line = reader.readLine();
            if ((line == null) || !line.equals(TAG_M3U)) {
                throw new MalformedFormatException("not m3u8 format");
            }

            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }

                if (!line.isEmpty()) {
                    parseLine(line);
                }
            }

            /**
             * 如果有定义流的话，按照带宽大小排序
             */
            if (!mStreamList.isEmpty()) {
                Collections.sort(mStreamList, new Comparator<Stream>() {
                    @Override
                    public int compare(Stream stream1, Stream stream2) {
                        return stream1.getBandwidth() - stream2.getBandwidth();
                    }
                });
            }
        }
        catch (IOException e) {
            /**
             * 清除
             */
            if (!mMetaData.isEmpty()) {
                mMetaData.clear();
            }

            if (!mMediaList.isEmpty()) {
                mMediaList.clear();
            }

            if (!mStreamList.isEmpty()) {
                mStreamList.clear();
            }

            if (!mSegmentList.isEmpty()) {
                mSegmentList.clear();
            }

            if (mPendingStream != null) {
                mPendingStream = null;
            }

            if (mPendingSegment != null) {
                mPendingSegment = null;
            }

            if (mKey != null) {
                mKey = null;
            }
        }
        finally {
            try {
                reader.close();
            }
            catch (IOException e) {
                /**
                 * ignore
                 */
            }
        }
    }

    private void parseLine(String line) {
        if (line.startsWith("#")) {
            /**
             * TAG
             */
            if (line.startsWith(TAG_VERSION)) {
                String value = line.substring(TAG_VERSION.length() + 1);
                parseVersion(value);
            }
            else if (line.startsWith(TAG_TARGET_DURATION)) {
                String value = line.substring(TAG_TARGET_DURATION.length() + 1);
                parseTargetDuration(value);
            }
            else if (line.startsWith(TAG_MEDIA_SEQUENCE)) {
                String value = line.substring(TAG_MEDIA_SEQUENCE.length() + 1);
                parseMediaSequence(value);
            }
            else if (line.equals(TAG_END_LIST)) {
                mMetaData.putBoolean(MetaData.KEY_END_LIST, true);
            }
            else if (line.startsWith(TAG_INF)) {
                String value = line.substring(TAG_INF.length() + 1);
                parseInf(value);
            }
            else if (line.equals(TAG_DISCONTINUITY)) {
                if (mPendingSegment == null) {
                    mPendingSegment = new Segment();
                }

                mPendingSegment.getMetaData().putBoolean(MetaData.KEY_DISCONTINUITY, true);
            }
            else if (line.startsWith(TAG_KEY)) {
                String value = line.substring(TAG_KEY.length() + 1);
                parseKey(value);
            }
            else if (line.startsWith(TAG_BYTE_RANGE)) {
                String value = line.substring(TAG_BYTE_RANGE.length() + 1);
                parseByteRange(value);
            }
            else if (line.startsWith(TAG_MEDIA)) {
                String value = line.substring(TAG_MEDIA.length() + 1);
                parseMedia(value);
            }
            else if (line.startsWith(TAG_STREAM_INF)) {
                String value = line.substring(TAG_STREAM_INF.length() + 1);
                parseStreamInf(value);
            }
        }
        else {
            /**
             * URI
             */
            String url = toUrl(line);

            if (mPendingSegment != null) {
                mPendingSegment.setUrl(url);

                if (mKey != null) {
                    mPendingSegment.setKey(mKey);
                }

                mSegmentList.add(mPendingSegment);
                mPendingSegment = null;
            }
            else if (mPendingStream != null) {
                mPendingStream.setUrl(url);

                mStreamList.add(mPendingStream);
                mPendingStream = null;
            }
        }
    }

    /**
     * 解析播放列表的版本
     */
    private void parseVersion(String content) {
        int version = Integer.parseInt(content);

        mMetaData.putInteger(MetaData.KEY_VERSION, version);
    }

    /**
     * 解析播放列表中媒体片段的最大时长
     */
    private void parseTargetDuration(String content) {
        int targetDuration = Integer.parseInt(content);

        mMetaData.putInteger(MetaData.KEY_TARGET_DURATION, targetDuration);
    }

    /**
     * 解析播放列表中第一个媒体片段的序号
     */
    private void parseMediaSequence(String content) {
        int sequenceNum = Integer.parseInt(content);

        mMetaData.putInteger(MetaData.KEY_MEDIA_SEQUENCE, sequenceNum);
    }

    /**
     * 解析播放列表中媒体片段的定义
     */
    private void parseInf(String content) {
        float duration;
        String title;

        if (content.contains(",")) {
            String[] result = content.split(",");

            duration = Float.parseFloat(result[0]);
            title = result[1];
        }
        else {
            duration = Float.parseFloat(content);
            title = null;
        }

        if (mPendingSegment == null) {
            mPendingSegment = new Segment();
        }

        mPendingSegment.getMetaData().putFloat(MetaData.KEY_SEGMENT_DURATION, duration);

        if (title != null && !title.isEmpty()) {
            mPendingSegment.getMetaData().putString(MetaData.KEY_SEGMENT_TITLE, title);
        }
    }

    /**
     * 解析片段范围
     */
    private void parseByteRange(String content) {
        long length;
        long offset;

        if (content.contains("@")) {
            String[] result = content.split("@");

            length = Long.parseLong(result[0]);
            offset = Long.parseLong(result[1]);
        }
        else {
            length = Long.parseLong(content);
            offset = mRangeOffset;
        }

        if (mPendingSegment == null) {
            mPendingSegment = new Segment();
        }

        mPendingSegment.getMetaData().putLong(MetaData.KEY_RANGE_LENGTH, length);

        if (offset >= 0) {
            mPendingSegment.getMetaData().putLong(MetaData.KEY_RANGE_OFFSET, offset);
        }

        mRangeOffset = offset + length;
    }

    /**
     * 解析播放列表中密钥的定义
     */
    private void parseKey(String content) {
        mKey = new Key();

        String[] attributes = content.split(",");
        for (int i = 0; i < attributes.length; i++) {
            String[] result = attributes[i].split("=");

            if (result[0].equals(ATTR_METHOD)) {
                mKey.getMetaData().putString(MetaData.KEY_CIPHER_METHOD, result[1]);
            }
            else if (result[0].equals(ATTR_IV)) {
                String value;
                if (result[1].startsWith("0x") || result[1].startsWith("0X")) {
                    value = result[1].substring(2);
                }
                else {
                    value = result[1];
                }

                if (value.length() != 32) {
                    throw new IllegalArgumentException("iv must be 128-bit");
                }

                byte[] iv = new BigInteger(value, 16).toByteArray();
                mKey.getMetaData().putByteArray(MetaData.KEY_CIPHER_IV, iv);
            }
            else if (result[0].equals(ATTR_URI)) {
                String url = toUrl(result[1]);
                mKey.setUrl(url);
            }
        }
    }

    /**
     * 解析播放列表中媒体的定义
     */
    private void parseMedia(String content) {
        Media media = new Media();

        String[] attributes = content.split(",");
        for (int i = 0; i < attributes.length; i++) {
            String[] result = attributes[i].split("=");

            if (result[0].equals(ATTR_TYPE)) {
                media.getMetaData().putString(MetaData.KEY_MEDIA_TYPE, result[1]);
            }
            else if (result[0].equals(ATTR_GROUP_ID)) {
                media.getMetaData().putString(MetaData.KEY_GROUP_ID, result[1]);
            }
            else if (result[0].equals(ATTR_LANGUAGE)) {
                media.getMetaData().putString(MetaData.KEY_LANGUAGE, result[1]);
            }
            else if (result[0].equals(ATTR_DEFAULT)) {
                boolean defaultSelect;
                if (result[1].equals("YES")) {
                    defaultSelect = true;
                }
                else if (result[1].equals("NO")) {
                    defaultSelect = false;
                }
                else {
                    throw new IllegalStateException("the value shall be YES or NO");
                }

                media.getMetaData().putBoolean(MetaData.KEY_DEFAULT_SELECT, defaultSelect);
            }
            else if (result[0].equals(ATTR_URI)) {
                String url = toUrl(result[1]);
                media.setUrl(url);
            }
        }

        mMediaList.add(media);
    }

    /**
     * 解析播放列表中流的定义
     */
    private void parseStreamInf(String content) {
        mPendingStream = new Stream();

        String[] attributes = content.split(",");
        for (int i = 0; i < attributes.length; i++) {
            String[] result = attributes[i].split("=");

            if (result[0].equals(ATTR_BANDWIDTH)) {
                int bandwidth = Integer.parseInt(result[1]);
                mPendingStream.getMetaData().putInteger(MetaData.KEY_BANDWIDTH, bandwidth);
            }
            else if (result[0].equals(ATTR_AUDIO)) {
                mPendingStream.getMetaData().putString(MetaData.KEY_AUDIO_GROUP_ID, result[1]);
            }
            else if (result[0].equals(ATTR_VIDEO)) {
                mPendingStream.getMetaData().putString(MetaData.KEY_VIDEO_GROUP_ID, result[1]);
            }
            else if (result[0].equals(ATTR_SUBTITLES)) {
                mPendingStream.getMetaData().putString(MetaData.KEY_SUBTITLE_GROUP_ID, result[1]);
            }
        }
    }

    /**
     * 基于uri生成url
     */
    private String toUrl(String uri) {
        String url;

        if (uri.startsWith("http://") || uri.startsWith("https://")) {
            /**
             * 不是相对的
             */
            url = uri;
        }
        else {
            String relativePath = uri;

            if (relativePath.startsWith("/")) {
                /**
                 * 容错：相对路径的起始不应该是路径分隔符，删除
                 */
                relativePath = relativePath.substring(1);
            }

            url = getDomain() + "/" + relativePath;
        }

        return url;
    }

    /**
     * 获取url的域
     */
    private String getDomain() {
        /**
         * scheme://authority/path?query#fragment
         */
        int schemeEnd = mUrl.indexOf("://");
        if (schemeEnd < 0) {
            throw new IllegalStateException("must be a absolute uri");
        }

        int pathStart = mUrl.indexOf("/", schemeEnd + 3);
        if (pathStart < 0) {
            /**
             * 没有path部分
             */
            int queryStart = mUrl.indexOf("?", schemeEnd + 3);
            if (queryStart < 0) {
                /**
                 * 也没有query部分
                 */
                int fragmentStart = mUrl.indexOf("#", schemeEnd + 3);
                if (fragmentStart < 0) {
                    /**
                     * 还没有fragment部分
                     */
                    return mUrl;
                }
                else {
                    return mUrl.substring(0, fragmentStart);
                }
            }
            else {
                return mUrl.substring(0, queryStart);
            }
        }
        else {
            return mUrl.substring(0, pathStart);
        }
    }

    /**
     * 是否有效
     */
    public boolean isValid() {
        return !mStreamList.isEmpty() || !mSegmentList.isEmpty();
    }

    /**
     * 是否包含流
     */
    public boolean containsStream() {
        return !mStreamList.isEmpty();
    }

    /**
     * 获取所有的媒体流
     */
    public Stream[] getStreams() {
        return mStreamList.toArray(new Stream[mStreamList.size()]);
    }

    /**
     * 获取指定的媒体组
     */
    public Media[] getMediaGroupById(String groupId) {
        List<Media> mediaGroup = new ArrayList<Media>();

        if (mMediaList.isEmpty()) {
            throw new IllegalStateException("no media in playlist");
        }

        for (Media media : mMediaList) {
            if (media.getGroupId().equals(groupId)) {
                mediaGroup.add(media);
            }
        }

        if (mediaGroup.isEmpty()) {
            throw new IllegalStateException("no media with the group_id " + groupId);
        }

        return mediaGroup.toArray(new Media[mediaGroup.size()]);
    }

    /**
     * 获取指定媒体组内的默认选择
     */
    public Media getDefaultMediaInGroup(String groupId) {
        if (mMediaList.isEmpty()) {
            throw new IllegalStateException("no media groups");
        }

        for (Media media : mMediaList) {
            if (media.getGroupId().equals(groupId) && media.defaultSelect()) {
                return media;
            }
        }

        return null;
    }

    /**
     * 是否包含片段
     */
    public boolean containsSegment() {
        return !mSegmentList.isEmpty();
    }

    /**
     * 获取片段的起始序号
     */
    public int getMediaSequence() {
        if (!mMetaData.containsKey(MetaData.KEY_MEDIA_SEQUENCE)) {
            return 0;
        }

        return mMetaData.getInteger(MetaData.KEY_MEDIA_SEQUENCE);
    }

    /**
     * 获取所有的媒体片段
     */
    public Segment[] getSegments() {
        if (!containsSegment()) {
            throw new IllegalStateException("not media playlist");
        }

        return mSegmentList.toArray(new Segment[mSegmentList.size()]);
    }
}
