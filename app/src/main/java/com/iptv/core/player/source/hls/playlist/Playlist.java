package com.iptv.core.player.source.hls.playlist;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

public class Playlist {
    private static final String TAG = "Playlist";

    /**
     * required
     */
    private int mVersion;
    private int mTargetDuration;

    /**
     * option
     */
    private int mMediaSequence = 0;
    private List<MediaSegment> mSegmentList;
    private boolean mEndOfList = false;

    private Playlist() {
        mSegmentList = new LinkedList<MediaSegment>();
    }

    public int getVersion() {
        return mVersion;
    }

    public boolean isEndOfList() {
        return mEndOfList;
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

    private void addSegment(MediaSegment segment) {
        if (segment.getDuration() > mTargetDuration) {
            Log.w(TAG, "segment duration must be less than or equal to the target duration");
        }

        int sequenceNumber = mMediaSequence + mSegmentList.size();
        segment.setSequenceNumber(sequenceNumber);

        mSegmentList.add(segment);
    }

    private void setEndOfList() {
        mEndOfList = true;
    }

    public static Playlist parse(String content) {
        Playlist playlist = new Playlist();

        BufferedReader reader = new BufferedReader(new StringReader(content));
        try {
            /**
             * MediaSegment parameters
             */
            float duration = 0.0f;
            ByteRange byteRange = null;
            boolean isDiscontinuous = false;
            Key key = null;

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

                    String[] result = value.split(",");
                    duration = Float.parseFloat(result[0]);
                    /**
                     * title一般不出现，也没有实际意义
                     */
                }
                else if (line.startsWith(Tag.RANGE)) {
                    String value = line.substring(Tag.RANGE.length() + 1);

                    byteRange = ByteRange.parse(value);
                }
                else if (line.equals(Tag.DISCONTINUITY)) {
                    isDiscontinuous = true;
                }
                else if (line.startsWith(Tag.KEY)) {
                    String value = line.substring(Tag.KEY.length() + 1);

                    key = Key.parse(value);
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
                }
                /**
                 * Media Segment
                 */
                else if (!line.startsWith("#")) {
                    MediaSegment segment = new MediaSegment(
                            duration, byteRange, isDiscontinuous, key, line);
                    playlist.addSegment(segment);

                    /**
                     * 重置部分参数
                     */
                    byteRange = null;
                    isDiscontinuous = false;
                }
            }

            reader.close();
        }
        catch (IOException e) {
            //ignore
        }

        return playlist;
    }
}
