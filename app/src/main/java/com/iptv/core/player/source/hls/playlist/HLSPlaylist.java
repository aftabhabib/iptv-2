package com.iptv.core.player.source.hls.playlist;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HLSPlaylist {
    private static final String TAG = "HLSPlaylist";

    private int mVersion;
    private List<MediaSegment> mSegmentList;

    private HLSPlaylist() {
        mSegmentList = new LinkedList<MediaSegment>();
    }

    private void setVersion(int version) {
        mVersion = version;
    }

    public int getVersion() {
        return mVersion;
    }

    private void addSegment(MediaSegment segment) {
        mSegmentList.add(segment);
    }

    public static HLSPlaylist parse(String content) {
        HLSPlaylist playlist = null;

        BufferedReader reader = new BufferedReader(new StringReader(content));
        try {
            String line = reader.readLine();

            if (!line.equals(Tag.M3U)) {
                Log.e(TAG, "malformed, must start with " + Tag.M3U);
            }
            else {
                playlist = new HLSPlaylist();

                /**
                 * MediaSegment parameters
                 */
                float duration = 0.0f;
                ByteRange byteRange = null;
                boolean isDiscontinuous = false;
                Key key = null;

                while (true) {
                    line = reader.readLine();

                    /**
                     * Basic Tags
                     */
                    if (line.startsWith(Tag.VERSION)) {
                        String value = line.substring(Tag.VERSION.length() + 1);

                        playlist.setVersion(Integer.parseInt(value));
                    }
                    /**
                     * Media Segment Tags
                     */
                    else if (line.startsWith(Tag.INF)) {
                        String[] result = line.substring(Tag.INF.length() + 1).split(",");

                        duration = Float.parseFloat(result[0]);
                        /**
                         * title没有实际意义，忽略
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
                        String[] result = line.substring(Tag.KEY.length() + 1).split(",");

                        key = Key.create(result);
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
            }

            reader.close();
        }
        catch (IOException e) {
            //ignore
        }

        return playlist;
    }
}
