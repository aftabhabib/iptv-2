package com.iptv.core.hls.playlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Playlist {
    private static final String TAG_M3U = "#EXTM3U";
    private static final String TAG_VERSION = "#EXT-X-VERSION";
    private static final String TAG_INF = "#EXTINF";
    private static final String TAG_BYTE_RANGE = "#EXT-X-BYTERANGE";
    private static final String TAG_DISCONTINUITY = "#EXT-X-DISCONTINUITY";
    private static final String TAG_KEY = "#EXT-X-KEY";
    private static final String TAG_TARGET_DURATION = "#EXT-X-TARGETDURATION";
    private static final String TAG_MEDIA_SEQUENCE = "#EXT-X-MEDIA-SEQUENCE";
    private static final String TAG_DISCONTINUITY_SEQUENCE = "#EXT-X-DISCONTINUITY-SEQUENCE";
    private static final String TAG_END_LIST = "#EXT-X-ENDLIST";
    private static final String TAG_MEDIA = "#EXT-X-MEDIA";
    private static final String TAG_STREAM_INF = "#EXT-X-STREAM-INF";

    private int mVersion;

    /**
     * Media Playlist
     */
    private int mMaxSegmentDuration;
    private int mMediaSequence;
    private List<MediaSegment> mSegmentList;
    private boolean mEndOfList;

    /**
     * Master Playlist
     */
    private Map<String, RenditionGroup> mRenditionGroupTable;
    private List<VariantStream> mStreamList;

    private Playlist() {
        mMaxSegmentDuration = 0;
        mMediaSequence = 0;
        mSegmentList = new LinkedList<MediaSegment>();
        mEndOfList = false;

        mRenditionGroupTable = new HashMap<String, RenditionGroup>();
        mStreamList = new ArrayList<VariantStream>();
    }

    /**
     * 获取版本号
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * 获取媒体序号
     */
    public int getMaxSegmentDuration() {
        return mMaxSegmentDuration;
    }

    /**
     * 获取媒体序号
     */
    public int getMediaSequence() {
        return mMediaSequence;
    }

    /**
     * 播放列表是否结束
     */
    public boolean isEndOfList() {
        return mEndOfList;
    }

    /**
     * 是否定义了RenditionGroup
     */
    public boolean containsRenditionGroup() {
        return !mRenditionGroupTable.isEmpty();
    }

    /**
     * 获取指定RenditionGroup中的默认Rendition
     */
    public Media getDefaultRenditionInGroup(String groupId) {
        RenditionGroup group = mRenditionGroupTable.get(groupId);
        if (group == null) {
            throw new IllegalStateException("rendition group " + groupId + " not found");
        }

        return group.getDefaultRendition();
    }

    /**
     * 是否定义了VariantStream
     */
    public boolean containsVariantStream() {
        return !mStreamList.isEmpty();
    }

    /**
     * 根据带宽选择合适的VariantStream
     */
    public VariantStream findStreamByBandwidth(int bandwidth) {
        int index;

        if (bandwidth > 0) {
            /**
             * 带宽已知，选择最接近的流
             */
            for (index = 0; index < mStreamList.size(); index++) {
                if (mStreamList.get(index).getBandwidth() > bandwidth) {
                    break;
                }
            }

            if (index > 0) {
                index -= 1;
            }
        }
        else {
            /**
             * 带宽未知，选择最大的流
             */
            index = mStreamList.size() - 1;
        }

        return mStreamList.get(index);
    }

    /**
     * 是否定义了媒体片段
     */
    public boolean containsMediaSegment() {
        return !mSegmentList.isEmpty();
    }

    /**
     * 移出媒体片段列表的头部
     */
    public MediaSegment removeMediaSegment() {
        if (mSegmentList.isEmpty()) {
            return null;
        }

        return mSegmentList.remove(0);
    }

    private void setVersion(int version) {
        mVersion = version;
    }

    private void setTargetDuration(int targetDuration) {
        mMaxSegmentDuration = targetDuration * 1000;
    }

    private void setMediaSequence(int mediaSequence) {
        mMediaSequence = mediaSequence;
    }

    private void setEndOfList() {
        mEndOfList = true;
    }

    private int getNextMediaSequenceNumber() {
        return mMediaSequence + mSegmentList.size();
    }

    private void addSegment(MediaSegment segment) {
        mSegmentList.add(segment);
    }

    private void addMedia(Media media) {
        if (!mRenditionGroupTable.containsKey(media.getGroupId())) {
            /**
             * this media belongs to a new rendition group
             */
            mRenditionGroupTable.put(media.getGroupId(), new RenditionGroup());
        }

        mRenditionGroupTable.get(media.getGroupId()).addMedia(media);
    }

    private void addStream(VariantStream stream) {
        /**
         * 按照带宽由小到大的次序
         */
        int index;
        for (index = 0; index < mStreamList.size(); index++) {
            if (stream.getBandwidth() < mStreamList.get(index).getBandwidth()) {
                break;
            }
        }

        mStreamList.add(index, stream);
    }

    public static Playlist parse(String content) {
        Playlist playlist = new Playlist();

        BufferedReader reader = new BufferedReader(new StringReader(content));
        try {
            MediaSegment.Builder segmentBuilder = null;
            VariantStream.Builder streamBuilder = null;

            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                /**
                 * Basic Tags
                 */
                if (line.equals(TAG_M3U)) {
                    /**
                     * must be the first line
                     */
                }
                else if (line.startsWith(TAG_VERSION)) {
                    String value = line.substring(TAG_VERSION.length() + 1);

                    playlist.setVersion(Integer.parseInt(value));
                }
                /**
                 * Media Segment Tags
                 */
                else if (line.startsWith(TAG_INF)) {
                    String value = line.substring(TAG_INF.length() + 1);

                    if (segmentBuilder == null) {
                        segmentBuilder = new MediaSegment.Builder();
                    }

                    segmentBuilder.setDuration(parseDuration(value));
                }
                else if (line.startsWith(TAG_BYTE_RANGE)) {
                    String value = line.substring(TAG_BYTE_RANGE.length() + 1);

                    if (segmentBuilder == null) {
                        segmentBuilder = new MediaSegment.Builder();
                    }

                    segmentBuilder.setRange(value);
                }
                else if (line.equals(TAG_DISCONTINUITY)) {
                    if (segmentBuilder == null) {
                        segmentBuilder = new MediaSegment.Builder();
                    }

                    segmentBuilder.setDiscontinuity();
                }
                else if (line.startsWith(TAG_KEY)) {
                    String value = line.substring(TAG_KEY.length() + 1);
                    Key key = createKey(Attribute.parseList(value));

                    if (segmentBuilder == null) {
                        segmentBuilder = new MediaSegment.Builder();
                    }

                    segmentBuilder.setKey(key);
                }
                /**
                 * Media Playlist Tags
                 */
                else if (line.startsWith(TAG_TARGET_DURATION)) {
                    String value = line.substring(TAG_TARGET_DURATION.length() + 1);

                    playlist.setTargetDuration(Integer.parseInt(value));
                }
                else if (line.startsWith(TAG_MEDIA_SEQUENCE)) {
                    String value = line.substring(TAG_MEDIA_SEQUENCE.length() + 1);

                    playlist.setMediaSequence(Integer.parseInt(value));
                }
                else if (line.startsWith(TAG_DISCONTINUITY_SEQUENCE)) {
                    String value = line.substring(TAG_DISCONTINUITY_SEQUENCE.length() + 1);

                    playlist.setMediaSequence(Integer.parseInt(value));
                }
                else if (line.equals(TAG_END_LIST)) {
                    playlist.setEndOfList();
                }
                /**
                 * Master Playlist Tags
                 */
                else if (line.startsWith(TAG_MEDIA)) {
                    String value = line.substring(TAG_MEDIA.length() + 1);

                    Media media = createMedia(Attribute.parseList(value));
                    playlist.addMedia(media);
                }
                else if (line.startsWith(TAG_STREAM_INF)) {
                    String value = line.substring(TAG_STREAM_INF.length() + 1);

                    streamBuilder = new VariantStream.Builder();
                    streamBuilder.setAttributeList(Attribute.parseList(value));
                }
                /**
                 * URI
                 */
                else if (!line.startsWith("#")) {
                    String uri = line;

                    if (streamBuilder != null) {
                        streamBuilder.setUri(uri);

                        playlist.addStream(streamBuilder.build());
                        streamBuilder = null;
                    }
                    else if (segmentBuilder != null) {
                        segmentBuilder.setUri(uri);
                        segmentBuilder.setSequenceNumber(playlist.getNextMediaSequenceNumber());

                        playlist.addSegment(segmentBuilder.build());
                        segmentBuilder = segmentBuilder.fork();
                    }
                }
                else {
                    /**
                     * not support yet
                     */
                }
            }

            reader.close();
        }
        catch (IOException e) {
            //ignore
        }

        return playlist;
    }

    private static String parseDuration(String value) {
        String[] result = value.split(",");

        return result[0];
    }

    private static Key createKey(List<Attribute> attributeList) {
        Key.Builder builder = new Key.Builder();

        builder.setAttributeList(attributeList);

        return builder.build();
    }

    private static Media createMedia(List<Attribute> attributeList) {
        Media.Builder builder = new Media.Builder();

        builder.setAttributeList(attributeList);

        return builder.build();
    }
}
