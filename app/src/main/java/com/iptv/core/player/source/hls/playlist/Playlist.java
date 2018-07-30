package com.iptv.core.player.source.hls.playlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Playlist {
    /**
     * required
     */
    private int mVersion;
    private int mTargetDuration;

    /**
     * option
     */
    private int mMediaSequence = 0;
    private boolean mEndOfList = false;
    private List<MediaSegment> mSegmentList;

    private Map<String, RenditionGroup> mRenditionGroupTable;
    private List<VariantStream> mStreamList;

    private Playlist() {
        //ignore
    }

    public int getVersion() {
        return mVersion;
    }

    public int getMediaSequence() {
        return mMediaSequence;
    }

    public boolean isEndOfList() {
        return mEndOfList;
    }

    public boolean isVariantPlaylist() {
        return mStreamList != null;
    }

    public VariantStream findStreamByBandwidth(int bandwidth) {
        int index;

        if (bandwidth > 0) {
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
            index = mStreamList.size() - 1;
        }

        return mStreamList.get(index);
    }

    private void setVersion(int version) {
        mVersion = version;
    }

    private void setTargetDuration(int targetDuration) {
        mTargetDuration = targetDuration * 1000;
    }

    private void setMediaSequence(int mediaSequence) {
        mMediaSequence = mediaSequence;
    }

    private void setEndOfList() {
        mEndOfList = true;
    }

    private void addSegment(MediaSegment segment) {
        if (mSegmentList == null) {
            mSegmentList = new LinkedList<MediaSegment>();
        }

        mSegmentList.add(segment);
    }

    private void addMedia(Media media) {
        if (mRenditionGroupTable == null) {
            mRenditionGroupTable = new HashMap<String, RenditionGroup>();
        }

        if (!mRenditionGroupTable.containsKey(media.getGroupId())) {
            /**
             * this media belongs to a new rendition group
             */
            mRenditionGroupTable.put(media.getGroupId(), new RenditionGroup());
        }

        mRenditionGroupTable.get(media.getGroupId()).addMedia(media);
    }

    private void addStream(VariantStream stream) {
        if (mStreamList == null) {
            mStreamList = new ArrayList<VariantStream>(5);
        }

        mStreamList.add(stream);
    }

    private void sortStreamByBandwidth() {
        mStreamList.sort(new Comparator<VariantStream>() {
            @Override
            public int compare(VariantStream stream1, VariantStream stream2) {
                return stream1.getBandwidth() - stream2.getBandwidth();
            }
        });
    }

    public static Playlist parse(String content) {
        Playlist playlist = new Playlist();

        BufferedReader reader = new BufferedReader(new StringReader(content));
        try {
            MediaSegment.Builder segmentBuilder = new MediaSegment.Builder();
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

                    segmentBuilder.setDuration(parseSegmentDuration(value));
                }
                else if (line.startsWith(Tag.BYTE_RANGE)) {
                    String value = line.substring(Tag.BYTE_RANGE.length() + 1);

                    segmentBuilder.setRange(parseSegmentRange(value));
                }
                else if (line.equals(Tag.DISCONTINUITY)) {

                    segmentBuilder.setDiscontinuity();
                }
                else if (line.startsWith(Tag.KEY)) {
                    String value = line.substring(Tag.KEY.length() + 1);

                    segmentBuilder.setKey(createSegmentKey(value));
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

                    playlist.addMedia(createMedia(value));
                }
                else if (line.startsWith(Tag.STREAM_INF)) {
                    String value = line.substring(Tag.STREAM_INF.length() + 1);

                    streamBuilder = createStreamBuilder(value);
                }
                /**
                 * Uri
                 */
                else if (!line.startsWith("#")) {
                    String uri = line;

                    if (streamBuilder != null) {
                        streamBuilder.setUri(uri);

                        playlist.addStream(streamBuilder.build());
                    }
                    else if (segmentBuilder != null) {
                        segmentBuilder.setUri(uri);

                        playlist.addSegment(segmentBuilder.build());

                        segmentBuilder = new MediaSegment.Builder(segmentBuilder);
                    }
                }
            }

            reader.close();
        }
        catch (IOException e) {
            //ignore
        }

        if (playlist.isVariantPlaylist()) {
            /**
             * 按照带宽排序
             */
            playlist.sortStreamByBandwidth();
        }

        return playlist;
    }

    private static String parseSegmentDuration(String value) {
        String[] result = value.split(",");

        return result[0];
    }

    private static ByteRange parseSegmentRange(String byteRange) {
        String[] result = byteRange.split("@");

        if (result.length == 1) {
            return new ByteRange(result[0]);
        }
        else {
            return new ByteRange(result[0], result[1]);
        }
    }

    public static Key createSegmentKey(String attributeList) {
        Key.Builder builder = new Key.Builder();

        String[] attributeArray = attributeList.split(",");
        for (int i = 0; i < attributeArray.length; i++) {
            Attribute attribute = Attribute.parse(attributeArray[i]);

            if (attribute.getKey().equals(Attribute.ATTR_METHOD)) {
                builder.setMethod(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_URI)) {
                builder.setUri(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_IV)) {
                builder.setInitVector(attribute.getValue());
            }
        }

        return builder.build();
    }

    private static Media createMedia(String attributeList) {
        Media.Builder builder = new Media.Builder();

        String[] attributeArray = attributeList.split(",");
        for (int i = 0; i < attributeArray.length; i++) {
            Attribute attribute = Attribute.parse(attributeArray[i]);

            if (attribute.getKey().equals(Attribute.ATTR_TYPE)) {
                builder.setType(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_URI)) {
                builder.setUri(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_GROUP_ID)) {
                builder.setGroupId(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_LANGUAGE)) {
                builder.setLanguage(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_NAME)) {
                builder.setName(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_DEFAULT)) {
                if (attribute.getValue().equals("YES")) {
                    builder.setDefault();
                }
            }
            else if (attribute.getKey().equals(Attribute.ATTR_AUTO_SELECT)) {
                if (attribute.getValue().equals("YES")) {
                    builder.setAutoSelect();
                }
            }
        }

        return builder.build();
    }

    private static VariantStream.Builder createStreamBuilder(String attributeList) {
        VariantStream.Builder builder = new VariantStream.Builder();

        String[] attributeArray = attributeList.split(",");
        for (int i = 0; i < attributeArray.length; i++) {
            Attribute attribute = Attribute.parse(attributeArray[i]);

            if (attribute.getKey().equals(Attribute.ATTR_BANDWIDTH)) {
                builder.setBandwidth(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_CODECS)) {
                builder.setCodecs(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_RESOLUTION)) {
                builder.setResolution(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_AUDIO)) {
                builder.setAudioGroup(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_VIDEO)) {
                builder.setVideoGroup(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_SUBTITLE)) {
                builder.setSubtitleGroup(attribute.getValue());
            }
        }

        return builder;
    }
}
