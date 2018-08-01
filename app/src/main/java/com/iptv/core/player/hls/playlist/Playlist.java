package com.iptv.core.player.hls.playlist;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Playlist {
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
     * 是否定义了MediaSegment
     */
    public boolean containsMediaSegment() {
        return !mSegmentList.isEmpty();
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
                if (line.equals(Tag.M3U)) {
                    /**
                     * must be the first line
                     */
                }
                else if (line.startsWith(Tag.VERSION)) {
                    String value = line.substring(Tag.VERSION.length() + 1);

                    playlist.setVersion(Integer.parseInt(value));
                }
                /**
                 * Media Segment Tags
                 */
                else if (line.startsWith(Tag.INF)) {
                    String value = line.substring(Tag.INF.length() + 1);

                    if (segmentBuilder == null) {
                        segmentBuilder = new MediaSegment.Builder();
                    }

                    segmentBuilder.setDuration(parseDuration(value));
                }
                else if (line.startsWith(Tag.BYTE_RANGE)) {
                    String value = line.substring(Tag.BYTE_RANGE.length() + 1);

                    if (segmentBuilder == null) {
                        segmentBuilder = new MediaSegment.Builder();
                    }

                    segmentBuilder.setRange(value);
                }
                else if (line.equals(Tag.DISCONTINUITY)) {
                    if (segmentBuilder == null) {
                        segmentBuilder = new MediaSegment.Builder();
                    }

                    segmentBuilder.setDiscontinuity();
                }
                else if (line.startsWith(Tag.KEY)) {
                    String value = line.substring(Tag.KEY.length() + 1);
                    Key key = createKey(Attribute.parseList(value));

                    if (segmentBuilder == null) {
                        segmentBuilder = new MediaSegment.Builder();
                    }

                    segmentBuilder.setKey(key);
                }
                /**
                 * Media Playlist Tags
                 */
                else if (line.startsWith(Tag.TARGET_DURATION)) {
                    String value = line.substring(Tag.TARGET_DURATION.length() + 1);

                    playlist.setTargetDuration(Integer.parseInt(value));
                }
                else if (line.startsWith(Tag.MEDIA_SEQUENCE)) {
                    String value = line.substring(Tag.MEDIA_SEQUENCE.length() + 1);

                    playlist.setMediaSequence(Integer.parseInt(value));
                }
                else if (line.startsWith(Tag.DISCONTINUITY_SEQUENCE)) {
                    String value = line.substring(Tag.DISCONTINUITY_SEQUENCE.length() + 1);

                    playlist.setMediaSequence(Integer.parseInt(value));
                }
                else if (line.equals(Tag.END_LIST)) {
                    playlist.setEndOfList();
                }
                /**
                 * Master Playlist Tags
                 */
                else if (line.startsWith(Tag.MEDIA)) {
                    String value = line.substring(Tag.MEDIA.length() + 1);

                    Media media = createMedia(Attribute.parseList(value));
                    playlist.addMedia(media);
                }
                else if (line.startsWith(Tag.STREAM_INF)) {
                    String value = line.substring(Tag.STREAM_INF.length() + 1);

                    streamBuilder = new VariantStream.Builder();
                    streamBuilder.setAttributeList(Attribute.parseList(value));
                }
                /**
                 * URI
                 */
                else if (!line.startsWith("#")) {
                    Uri uri = Uri.parse(line);

                    if (streamBuilder != null) {
                        streamBuilder.setUri(uri);

                        playlist.addStream(streamBuilder.build());
                        streamBuilder = null;
                    }
                    else if (segmentBuilder != null) {
                        segmentBuilder.setUri(uri);

                        playlist.addSegment(segmentBuilder.build());
                        segmentBuilder = segmentBuilder.fork();
                    }
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
