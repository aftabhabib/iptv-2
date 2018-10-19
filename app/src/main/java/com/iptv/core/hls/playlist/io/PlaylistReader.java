package com.iptv.core.hls.playlist.io;

import com.iptv.core.hls.playlist.Playlist;
import com.iptv.core.hls.playlist.attribute.AttributeList;
import com.iptv.core.hls.playlist.datatype.ByteRange;
import com.iptv.core.hls.playlist.tag.ByteRangeTag;
import com.iptv.core.hls.playlist.tag.DiscontinuitySequenceTag;
import com.iptv.core.hls.playlist.tag.DiscontinuityTag;
import com.iptv.core.hls.playlist.tag.EndListTag;
import com.iptv.core.hls.playlist.tag.IFrameOnlyTag;
import com.iptv.core.hls.playlist.tag.IFrameStreamInfTag;
import com.iptv.core.hls.playlist.tag.InfTag;
import com.iptv.core.hls.playlist.tag.KeyTag;
import com.iptv.core.hls.playlist.tag.M3UTag;
import com.iptv.core.hls.playlist.tag.MapTag;
import com.iptv.core.hls.playlist.tag.MediaSequenceTag;
import com.iptv.core.hls.playlist.tag.MediaTag;
import com.iptv.core.hls.playlist.tag.PlaylistTypeTag;
import com.iptv.core.hls.playlist.tag.StreamInfTag;
import com.iptv.core.hls.playlist.tag.Tag;
import com.iptv.core.hls.playlist.tag.TargetDurationTag;
import com.iptv.core.hls.playlist.tag.VersionTag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public final class PlaylistReader {
    private static final int ST_PLAYLIST_HEADER = 0;
    private static final int ST_PLAYLIST_META = 1;
    private static final int ST_SEGMENT_META = 2;
    private static final int ST_STREAM_META = 3;
    private static final int ST_SEGMENT = 4;
    private static final int ST_STREAM = 5;
    private static final int ST_IFRAME_STREAM = 6;
    private static final int ST_PLAYLIST_END = 7;

    private BufferedReader mReader;
    private int mState = ST_PLAYLIST_HEADER;

    /**
     * 构造函数
     */
    public PlaylistReader(Reader reader) {
        mReader = new BufferedReader(reader);
    }

    /**
     * 读播放列表
     */
    public Playlist read() throws IOException {
        while (true) {
            String line = mReader.readLine();
            if (line == null) {
                break;
            }

            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("#")) {
                if (line.startsWith("#EXT")) {
                    Tag tag = parseTag(line);
                    if (tag != null) {
                        processTag(tag);
                    }
                    else {
                        /**
                         * unknown tag, ignore
                         */
                    }
                }
                else {
                    /**
                     * comment, skip
                     */
                }
            }
            else {
                processUri(line);
            }
        }

        if ((mState != ST_SEGMENT && mState != ST_PLAYLIST_END)
                && (mState != ST_STREAM && mState != ST_IFRAME_STREAM)) {
            throw new IOException("malformed format");
        }

        return null;
    }

    /**
     * 解析标签
     */
    private static Tag parseTag(String line) {
        if (!line.contains(":")) {
            return createTag(line);
        }
        else {
            String[] result = line.split(":");
            if (result.length != 2) {
                throw new IllegalArgumentException("should be <name>:<value>");
            }

            return createTag(result[0], result[1]);
        }
    }

    /**
     * 创建标签
     */
    private static Tag createTag(String name) {
        if (name.equals(Tag.Name.M3U)) {
            return new M3UTag();
        }
        else if (name.equals(Tag.Name.I_FRAMES_ONLY)) {
            return new IFrameOnlyTag();
        }
        else if (name.equals(Tag.Name.DISCONTINUITY)) {
            return new DiscontinuityTag();
        }
        else if (name.equals(Tag.Name.END_LIST)) {
            return new EndListTag();
        }
        else {
            /**
             * not supported
             */
            return null;
        }
    }

    /**
     * 创建标签
     */
    private static Tag createTag(String name, String value) {
        if (name.equals(Tag.Name.VERSION)) {
            int version = Integer.parseInt(value);
            return new VersionTag(version);
        }
        else if (name.equals(Tag.Name.PLAYLIST_TYPE)) {
            return new PlaylistTypeTag(value);
        }
        else if (name.equals(Tag.Name.TARGET_DURATION)) {
            int duration = Integer.parseInt(value);
            return new TargetDurationTag(duration);
        }
        else if (name.equals(Tag.Name.MEDIA_SEQUENCE)) {
            long sequenceNumber = Long.parseLong(value);
            return new MediaSequenceTag(sequenceNumber);
        }
        else if (name.equals(Tag.Name.DISCONTINUITY_SEQUENCE)) {
            long sequenceNumber = Long.parseLong(value);
            return new DiscontinuitySequenceTag(sequenceNumber);
        }
        else if (name.equals(Tag.Name.MAP)) {
            AttributeList attributeList = AttributeList.parse(value);
            return new MapTag(attributeList);
        }
        else if (name.equals(Tag.Name.KEY)) {
            AttributeList attributeList = AttributeList.parse(value);
            return new MapTag(attributeList);
        }
        else if (name.equals(Tag.Name.BYTE_RANGE)) {
            ByteRange range = ByteRange.valueOf(value);
            return new ByteRangeTag(range);
        }
        else if (name.equals(Tag.Name.INF)) {
            String[] result = value.split(",");
            if (result.length != 2) {
                throw new IllegalArgumentException("should be <duration>,[<title>]");
            }

            if (!result[0].contains(".")) {
                int duration = Integer.parseInt(result[0]);
                return new InfTag(duration, result[1]);
            }
            else {
                float duration = Float.parseFloat(result[0]);
                return new InfTag(duration, result[1]);
            }
        }
        else if (name.equals(Tag.Name.MEDIA)) {
            AttributeList attributeList = AttributeList.parse(value);
            return new MediaTag(attributeList);
        }
        else if (name.equals(Tag.Name.STREAM_INF)) {
            AttributeList attributeList = AttributeList.parse(value);
            return new StreamInfTag(attributeList);
        }
        else if (name.equals(Tag.Name.I_FRAME_STREAM_INF)) {
            AttributeList attributeList = AttributeList.parse(value);
            return new IFrameStreamInfTag(attributeList);
        }
        else {
            /**
             * not supported
             */
            return null;
        }
    }

    /**
     * 处理标签
     */
    private void processTag(Tag tag) throws IOException {
        String tagName = tag.getName();

        switch (mState) {
            case ST_PLAYLIST_HEADER: {
                if (tagName.equals(Tag.Name.M3U)) {
                    mState = ST_PLAYLIST_META;
                }
                else {
                    throw new IOException("malformed format");
                }

                break;
            }
            case ST_PLAYLIST_META: {
                if (tagName.equals(Tag.Name.VERSION)) {
                    /**
                     * Basic meta
                     */
                }
                else if (tagName.equals(Tag.Name.TARGET_DURATION)
                        || tagName.equals(Tag.Name.MEDIA_SEQUENCE)
                        || tagName.equals(Tag.Name.PLAYLIST_TYPE)
                        || tagName.equals(Tag.Name.I_FRAMES_ONLY)
                        || tagName.equals(Tag.Name.DISCONTINUITY_SEQUENCE)) {
                    /**
                     * MediaPlaylist's meta
                     */
                }
                else if (tagName.equals(Tag.Name.MEDIA)) {
                    /**
                     * MasterPlaylist's meta
                     */
                }
                else if (tagName.equals(Tag.Name.MAP)
                        || tagName.equals(Tag.Name.KEY)
                        || tagName.equals(Tag.Name.BYTE_RANGE)
                        || tagName.equals(Tag.Name.DISCONTINUITY)
                        || tagName.equals(Tag.Name.INF)) {
                    /**
                     * Segment meta
                     */
                    mState = ST_SEGMENT_META;
                }
                else if (tagName.equals(Tag.Name.STREAM_INF)) {
                    /**
                     * Stream meta
                     */
                    mState = ST_STREAM_META;
                }
                else if (tagName.equals(Tag.Name.I_FRAME_STREAM_INF)) {
                    /**
                     * I-Frame stream
                     */
                    mState = ST_IFRAME_STREAM;
                }
                else {
                    throw new IOException("malformed format");
                }

                break;
            }
            case ST_SEGMENT_META: {
                if (tagName.equals(Tag.Name.MAP)
                        || tagName.equals(Tag.Name.KEY)
                        || tagName.equals(Tag.Name.BYTE_RANGE)
                        || tagName.equals(Tag.Name.DISCONTINUITY)
                        || tagName.equals(Tag.Name.INF)) {
                    /**
                     * Segment meta
                     */
                }
                else {
                    throw new IOException("malformed format");
                }

                break;
            }
            case ST_SEGMENT: {
                if (tagName.equals(Tag.Name.MAP)
                        || tagName.equals(Tag.Name.KEY)
                        || tagName.equals(Tag.Name.BYTE_RANGE)
                        || tagName.equals(Tag.Name.DISCONTINUITY)
                        || tagName.equals(Tag.Name.INF)) {
                    /**
                     * Segment meta
                     */
                    mState = ST_SEGMENT_META;
                }
                else if (tagName.equals(Tag.Name.END_LIST)) {
                    mState = ST_PLAYLIST_END;
                }
                else {
                    throw new IOException("malformed format");
                }
            }
            case ST_STREAM:
            case ST_IFRAME_STREAM: {
                if (tagName.equals(Tag.Name.STREAM_INF)) {
                    /**
                     * Stream meta
                     */
                    mState = ST_SEGMENT_META;
                }
                else if (tagName.equals(Tag.Name.I_FRAME_STREAM_INF)) {
                    /**
                     * I-Frame stream
                     */
                    mState = ST_IFRAME_STREAM;
                }
                else {
                    throw new IOException("malformed format");
                }
            }
            default: {
                throw new IOException("malformed format");
            }
        }
    }

    /**
     * 创建媒体片段
     */
    private void processUri(String uri) throws IOException {
        switch (mState) {
            case ST_SEGMENT_META: {
                /**
                 * add new segment
                 */
                mState = ST_SEGMENT;

                break;
            }
            case ST_STREAM_META: {
                /**
                 * add new stream
                 */
                mState = ST_STREAM;

                break;
            }
            default: {
                throw new IOException("malformed format");
            }
        }
    }

    /**
     * 安静地关闭
     */
    public void closeQuitly() {
        try {
            mReader.close();
        }
        catch (IOException e) {
            /**
             * ignore
             */
        }
    }
}
