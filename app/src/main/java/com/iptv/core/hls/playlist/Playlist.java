package com.iptv.core.hls.playlist;

import com.iptv.core.player.MetaData;
import com.iptv.core.utils.IOUtils;
import com.iptv.core.utils.MalformedFormatException;

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
    private static final String TAG_DISCONTINUITY_SEQUENCE = "#EXT-X-DISCONTINUITY-SEQUENCE";
    private static final String TAG_KEY = "#EXT-X-KEY";
    private static final String TAG_BYTE_RANGE = "#EXT-X-BYTERANGE";
    private static final String TAG_MEDIA = "#EXT-X-MEDIA";
    private static final String TAG_STREAM_INF = "#EXT-X-STREAM-INF";

    private int mHashCode;

    private int mVersion = 1;
    private int mTargetDuration = 0;
    private MetaData mMetaData = new MetaData();

    private List<Media> mRenditionList = new ArrayList<Media>();

    private List<Stream> mStreamList = new ArrayList<Stream>();
    private Stream mPendingStream = null;

    private List<Segment> mSegmentList = new ArrayList<Segment>();
    private Segment mPendingSegment = null;

    private Key mKey = null;
    private long mRangeOffset = 0;
    private int mDiscontinuityCount = 0;

    /**
     * 构造函数
     */
    public Playlist(String content) throws MalformedFormatException {
        mHashCode = content.hashCode();

        String[] lines = IOUtils.lines(content);
        if (lines == null) {
            throw new MalformedFormatException("");
        }

        parse(lines);
    }

    /**
     * 解析
     */
    private void parse(String[] lines) throws MalformedFormatException {
        if (!lines[0].equals(TAG_M3U)) {
            throw new MalformedFormatException("not m3u8 format");
        }

        for (int i = 1; i < lines.length; i++) {
            if (!lines[i].isEmpty()) {
                parseLine(lines[i]);
            }
        }

        if (!mStreamList.isEmpty()) {
            /**
             * 按照流的带宽大小排序
             */
            Collections.sort(mStreamList, new Comparator<Stream>() {
                @Override
                public int compare(Stream stream1, Stream stream2) {
                    return stream1.getBandwidth() - stream2.getBandwidth();
                }
            });
        }
        else {
            if (mTargetDuration == 0) {
                throw new MalformedFormatException("EXT-X-TARGETDURATION is required");
            }
        }
    }

    private void parseLine(String line) throws MalformedFormatException {
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
                mMetaData.putBoolean("end-of-list", true);
            }
            else if (line.startsWith(TAG_INF)) {
                String value = line.substring(TAG_INF.length() + 1);
                parseInf(value);
            }
            else if (line.equals(TAG_DISCONTINUITY)) {
                getPendingSegment().setDiscontinuity();

                mDiscontinuityCount++;
            }
            else if (line.startsWith(TAG_DISCONTINUITY_SEQUENCE)) {
                String value = line.substring(TAG_DISCONTINUITY_SEQUENCE.length() + 1);
                parseDiscontinuitySequence(value);
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
            else {
                /**
                 * ignore
                 */
            }
        }
        else {
            /**
             * URI
             */
            String uri = line;

            if (mPendingSegment != null) {
                mPendingSegment.setUri(uri);
                mPendingSegment.setSequenceNumber(getSequenceNumber());
                mPendingSegment.setDiscontinuitySequenceNumber(getDiscontinuitySequenceNumber());

                if (mKey != null) {
                    mPendingSegment.setKey(mKey);
                }

                mSegmentList.add(mPendingSegment);
                mPendingSegment = null;
            }
            else if (mPendingStream != null) {
                mPendingStream.setUri(uri);

                if (!mRenditionList.isEmpty()) {
                    if (mPendingStream.containsAudioRenditions()) {
                        mPendingStream.setAudioRenditions(getRenditionGroup(
                                Media.TYPE_AUDIO, mPendingStream.getAudioGroupId()));
                    }

                    if (mPendingStream.containsVideoRenditions()) {
                        mPendingStream.setVideoRenditions(getRenditionGroup(
                                Media.TYPE_VIDEO, mPendingStream.getVideoGroupId()));
                    }

                    if (mPendingStream.containsSubtitleRenditions()) {
                        mPendingStream.setSubtitleRenditions(getRenditionGroup(
                                Media.TYPE_SUBTITLE, mPendingStream.getSubtitleGroupId()));
                    }
                }

                mStreamList.add(mPendingStream);
                mPendingStream = null;
            }
            else {
                /**
                 * ignore
                 */
            }
        }
    }

    /**
     * 解析播放列表的版本
     */
    private void parseVersion(String content) {
        mVersion = Integer.parseInt(content);
    }

    /**
     * 解析播放列表中片段的最大时长
     */
    private void parseTargetDuration(String content) {
        mTargetDuration = Integer.parseInt(content);
    }

    /**
     * 解析播放列表中片段的起始序号
     */
    private void parseMediaSequence(String content) {
        mMetaData.putInteger("media-sequence", Integer.parseInt(content));
    }

    /**
     * 解析播放列表中片段的定义
     */
    private void parseInf(String content) {
        float duration;
        if (content.contains(",")) {
            String[] result = content.split(",");

            duration = Float.parseFloat(result[0]);
        }
        else {
            duration = Float.parseFloat(content);
        }

        getPendingSegment().setDuration(duration);
    }

    /**
     * 解析媒体片段的数据范围
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

        getPendingSegment().setByteRange(offset, length);

        mRangeOffset = offset + length;
    }

    /**
     * 解析播放列表中片段的Discontinuity序号
     */
    private void parseDiscontinuitySequence(String content) {
        mMetaData.putInteger("discontinuity-sequence", Integer.parseInt(content));
    }

    /**
     * 解析播放列表中密钥的定义
     */
    private void parseKey(String content) throws MalformedFormatException {
        String[] attributes;
        if (content.contains(",")) {
            attributes = content.split(",");
        }
        else {
            attributes = new String[] { content };
        }

        mKey = new Key(attributes);
    }

    /**
     * 解析播放列表中媒体的定义
     */
    private void parseMedia(String content) throws MalformedFormatException {
        String[] attributes;
        if (content.contains(",")) {
            attributes = content.split(",");
        }
        else {
            attributes = new String[] { content };
        }

        mRenditionList.add(new Media(attributes));
    }

    /**
     * 解析播放列表中流的定义
     */
    private void parseStreamInf(String content) throws MalformedFormatException {
        String[] attributes;
        if (content.contains(",")) {
            attributes = content.split(",");
        }
        else {
            attributes = new String[] { content };
        }

        mPendingStream = new Stream(attributes);
    }

    /**
     * 获取指定的表现组
     */
    private Media[] getRenditionGroup(String groupId, String type) throws MalformedFormatException {
        List<Media> group = new ArrayList<Media>();

        for (Media rendition : mRenditionList) {
            if (rendition.getType().equals(type)
                    && rendition.getGroupId().equals(groupId)) {
                group.add(rendition);
            }
        }

        if (group.isEmpty()) {
            throw new MalformedFormatException("no " + type + "renditions in group " + groupId);
        }

        return group.toArray(new Media[group.size()]);
    }

    /**
     * 获取当前片段
     */
    private Segment getPendingSegment() {
        if (mPendingSegment == null) {
            mPendingSegment = new Segment();
        }

        return mPendingSegment;
    }

    /**
     * 获取当前片段的序号
     */
    private int getSequenceNumber() {
        int mediaSequence;
        if (mMetaData.containsKey("media-sequence")) {
            mediaSequence = mMetaData.getInteger("media-sequence");
        }
        else {
            mediaSequence = 0;
        }

        return mediaSequence + mSegmentList.size();
    }

    /**
     * 获取当前片段的discontinuity序号
     */
    private int getDiscontinuitySequenceNumber() {
        int discontinuitySequence;
        if (mMetaData.containsKey("discontinuity-sequence")) {
            discontinuitySequence = mMetaData.getInteger("discontinuity-sequence");
        }
        else {
            discontinuitySequence = 0;
        }

        return discontinuitySequence + mDiscontinuityCount;
    }

    /**
     * 是不是主播放列表
     */
    public boolean isMasterPlaylist() {
        return !mStreamList.isEmpty();
    }

    /**
     * 获取所有的流
     */
    public Stream[] getStreams() {
        if (!isMasterPlaylist()) {
            throw new IllegalStateException("only in MasterPlaylist");
        }

        return mStreamList.toArray(new Stream[mStreamList.size()]);
    }

    /**
     * 获取播放列表中片段的最大时长（毫秒）
     */
    public int getTargetDuration() {
        if (isMasterPlaylist()) {
            throw new IllegalStateException("only in MediaPlaylist");
        }

        return mTargetDuration * 1000;
    }

    /**
     * 获取所有的片段
     */
    public Segment[] getSegments() {
        if (isMasterPlaylist()) {
            throw new IllegalStateException("only in MediaPlaylist");
        }

        return mSegmentList.toArray(new Segment[mSegmentList.size()]);
    }

    /**
     * 是否还有更多的片段
     */
    public boolean endOfList() {
        if (isMasterPlaylist()) {
            throw new IllegalStateException("only in MediaPlaylist");
        }

        if (!mMetaData.containsKey("end-of-list")) {
            return false;
        }
        else {
            return mMetaData.getBoolean("end-of-list");
        }
    }

    /**
     * 播放列表的总时长
     */
    public float getTotalDuration() {
        if (isMasterPlaylist()) {
            throw new IllegalStateException("only in MediaPlaylist");
        }

        int totalDuration = 0;
        for (Segment segment : mSegmentList) {
            totalDuration += segment.getDuration();
        }

        return totalDuration;
    }

    @Override
    public int hashCode() {
        return mHashCode;
    }
}
