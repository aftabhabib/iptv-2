package com.iptv.core.hls.playlist.io;

import com.iptv.core.hls.playlist.IFrameStream;
import com.iptv.core.hls.playlist.MasterPlaylist;
import com.iptv.core.hls.playlist.MediaPlaylist;
import com.iptv.core.hls.playlist.Playlist;
import com.iptv.core.hls.playlist.Rendition;
import com.iptv.core.hls.playlist.Segment;
import com.iptv.core.hls.playlist.Stream;
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
import com.iptv.core.hls.utils.HttpHelper;
import com.iptv.core.hls.utils.UrlHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 播放列表读取器
 */
public final class PlaylistReader {
    private String mBaseUri;
    private BufferedReader mReader;

    private List<Tag> mBasicMeta = new ArrayList<>();
    private List<Tag> mMediaPlaylistMeta = new ArrayList<>();
    private List<Tag> mMasterPlaylistMeta = new ArrayList<>();
    private List<Tag> mSegmentMeta = new ArrayList<>();
    private List<Tag> mStreamMeta = new ArrayList<>();

    private List<Segment> mSegmentList = new LinkedList<>();
    private List<Stream> mStreamList = new ArrayList<>();
    private List<IFrameStream> mIFrameStreamList = new ArrayList<>();

    private static final int ST_PLAYLIST_HEADER = 0;
    private static final int ST_PLAYLIST_META = 1;
    private static final int ST_SEGMENT_META = 2;
    private static final int ST_STREAM_META = 3;
    private static final int ST_SEGMENT = 4;
    private static final int ST_STREAM = 5;
    private static final int ST_IFRAME_STREAM = 6;
    private static final int ST_PLAYLIST_END = 7;

    private int mState = ST_PLAYLIST_HEADER;

    /**
     * 构造函数
     */
    public PlaylistReader(String url, Map<String, String> properties) throws IOException {
        this(UrlHelper.getBaseUri(url), HttpHelper.get(url, properties));
    }

    /**
     * 构造函数
     */
    private PlaylistReader(String baseUri, InputStream input) {
        mBaseUri = baseUri;
        mReader = new BufferedReader(new InputStreamReader(input));
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

        if (!(mState == ST_SEGMENT
                || mState == ST_PLAYLIST_END
                || mState == ST_STREAM
                || mState == ST_IFRAME_STREAM)) {
            throw new IllegalStateException("malformed format");
        }

        if (!mMediaPlaylistMeta.isEmpty() && !mSegmentList.isEmpty()) {
            return createMediaPlaylist();
        }
        else if (!mMasterPlaylistMeta.isEmpty()
                && (!mStreamList.isEmpty() || !mIFrameStreamList.isEmpty())) {
            return createMasterPlaylist();
        }
        else {
            throw new IllegalStateException("malformed format");
        }
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
    private void processTag(Tag tag) {
        String tagName = tag.getName();

        switch (mState) {
            case ST_PLAYLIST_HEADER: {
                if (tagName.equals(Tag.Name.M3U)) {
                    mState = ST_PLAYLIST_META;
                }
                else {
                    throw new IllegalStateException("malformed format");
                }

                break;
            }
            case ST_PLAYLIST_META: {
                if (tagName.equals(Tag.Name.VERSION)) {
                    mBasicMeta.add(tag);
                }
                else if (tagName.equals(Tag.Name.TARGET_DURATION)
                        || tagName.equals(Tag.Name.MEDIA_SEQUENCE)
                        || tagName.equals(Tag.Name.PLAYLIST_TYPE)
                        || tagName.equals(Tag.Name.I_FRAMES_ONLY)
                        || tagName.equals(Tag.Name.DISCONTINUITY_SEQUENCE)) {
                    mMediaPlaylistMeta.add(tag);
                }
                else if (tagName.equals(Tag.Name.MEDIA)) {
                    mMasterPlaylistMeta.add(tag);
                }
                else if (tagName.equals(Tag.Name.MAP)
                        || tagName.equals(Tag.Name.KEY)
                        || tagName.equals(Tag.Name.BYTE_RANGE)
                        || tagName.equals(Tag.Name.DISCONTINUITY)
                        || tagName.equals(Tag.Name.INF)) {
                    mSegmentMeta.add(tag);
                    mState = ST_SEGMENT_META;
                }
                else if (tagName.equals(Tag.Name.STREAM_INF)) {
                    mStreamMeta.add(tag);
                    mState = ST_STREAM_META;
                }
                else if (tagName.equals(Tag.Name.I_FRAME_STREAM_INF)) {
                    mIFrameStreamList.add(createIFrameStream((IFrameStreamInfTag)tag));
                    mState = ST_IFRAME_STREAM;
                }
                else {
                    throw new IllegalStateException("malformed format");
                }

                break;
            }
            case ST_SEGMENT_META: {
                if (tagName.equals(Tag.Name.MAP)
                        || tagName.equals(Tag.Name.KEY)
                        || tagName.equals(Tag.Name.BYTE_RANGE)
                        || tagName.equals(Tag.Name.DISCONTINUITY)
                        || tagName.equals(Tag.Name.INF)) {
                    mSegmentMeta.add(tag);
                }
                else {
                    throw new IllegalStateException("malformed format");
                }

                break;
            }
            case ST_SEGMENT: {
                if (tagName.equals(Tag.Name.MAP)
                        || tagName.equals(Tag.Name.KEY)
                        || tagName.equals(Tag.Name.BYTE_RANGE)
                        || tagName.equals(Tag.Name.DISCONTINUITY)
                        || tagName.equals(Tag.Name.INF)) {
                    mSegmentMeta.add(tag);
                    mState = ST_SEGMENT_META;
                }
                else if (tagName.equals(Tag.Name.END_LIST)) {
                    mMediaPlaylistMeta.add(tag);
                    mState = ST_PLAYLIST_END;
                }
                else {
                    throw new IllegalStateException("malformed format");
                }

                break;
            }
            case ST_STREAM:
            case ST_IFRAME_STREAM: {
                if (tagName.equals(Tag.Name.STREAM_INF)) {
                    mStreamMeta.add(tag);
                    mState = ST_SEGMENT_META;
                }
                else if (tagName.equals(Tag.Name.I_FRAME_STREAM_INF)) {
                    mIFrameStreamList.add(createIFrameStream((IFrameStreamInfTag)tag));
                    mState = ST_IFRAME_STREAM;
                }
                else {
                    throw new IllegalStateException("malformed format");
                }

                break;
            }
            default: {
                throw new IllegalStateException("malformed format");
            }
        }
    }

    /**
     * 处理uri
     */
    private void processUri(String uri) {
        switch (mState) {
            case ST_SEGMENT_META: {
                mSegmentList.add(createSegment(uri));
                mState = ST_SEGMENT;

                break;
            }
            case ST_STREAM_META: {
                mStreamList.add(createStream(uri));
                mState = ST_STREAM;

                break;
            }
            default: {
                throw new IllegalStateException("malformed format");
            }
        }
    }

    /**
     * 创建媒体片段
     */
    private Segment createSegment(String uri) {
        MapTag mapTag = null;
        KeyTag keyTag = null;
        ByteRangeTag rangeTag = null;
        DiscontinuityTag discontinuityTag = null;
        InfTag infTag = null;

        while (!mSegmentMeta.isEmpty()) {
            Tag tag = mSegmentMeta.remove(0);

            String tagName = tag.getName();
            if (tagName.equals(Tag.Name.MAP)) {
                mapTag = (MapTag)tag;
            }
            else if (tagName.equals(Tag.Name.KEY)) {
                keyTag = (KeyTag)tag;
            }
            else if (tagName.equals(Tag.Name.BYTE_RANGE)) {
                rangeTag = (ByteRangeTag)tag;
            }
            else if (tagName.equals(Tag.Name.DISCONTINUITY)) {
                discontinuityTag = (DiscontinuityTag)tag;
            }
            else if (tagName.equals(Tag.Name.INF)) {
                infTag = (InfTag)tag;
            }
        }

        return new Segment(mapTag, keyTag, rangeTag, discontinuityTag, infTag, uri);
    }

    /**
     * 创建流
     */
    private Stream createStream(String uri) {
        StreamInfTag streamInfTag = null;

        while (!mStreamMeta.isEmpty()) {
            Tag tag = mStreamMeta.remove(0);

            String tagName = tag.getName();
            if (tagName.equals(Tag.Name.STREAM_INF)) {
                streamInfTag = (StreamInfTag)tag;
            }
        }

        return new Stream(streamInfTag, uri);
    }

    /**
     * 创建I帧流
     */
    private IFrameStream createIFrameStream(IFrameStreamInfTag iFrameStreamInfTag) {
        return new IFrameStream(iFrameStreamInfTag);
    }

    /**
     * 创建媒体播放列表
     */
    private Playlist createMediaPlaylist() {
        VersionTag versionTag = null;
        TargetDurationTag targetDurationTag = null;
        MediaSequenceTag mediaSequenceTag = null;
        EndListTag endListTag = null;
        PlaylistTypeTag playlistTypeTag = null;
        IFrameOnlyTag iFrameOnlyTag = null;
        DiscontinuitySequenceTag discontinuitySequenceTag = null;

        while (!mBasicMeta.isEmpty()) {
            Tag tag = mBasicMeta.remove(0);

            String tagName = tag.getName();
            if (tagName.equals(Tag.Name.VERSION)) {
                versionTag = (VersionTag)tag;
            }
        }

        while (!mMediaPlaylistMeta.isEmpty()) {
            Tag tag = mMediaPlaylistMeta.remove(0);

            String tagName = tag.getName();
            if (tagName.equals(Tag.Name.TARGET_DURATION)) {
                targetDurationTag = (TargetDurationTag)tag;
            }
            else if (tagName.equals(Tag.Name.MEDIA_SEQUENCE)) {
                mediaSequenceTag = (MediaSequenceTag)tag;
            }
            else if (tagName.equals(Tag.Name.END_LIST)) {
                endListTag = (EndListTag)tag;
            }
            else if (tagName.equals(Tag.Name.PLAYLIST_TYPE)) {
                playlistTypeTag = (PlaylistTypeTag)tag;
            }
            else if (tagName.equals(Tag.Name.I_FRAMES_ONLY)) {
                iFrameOnlyTag = (IFrameOnlyTag)tag;
            }
            else if (tagName.equals(Tag.Name.DISCONTINUITY_SEQUENCE)) {
                discontinuitySequenceTag = (DiscontinuitySequenceTag)tag;
            }
        }

        return new MediaPlaylist(mBaseUri, versionTag, targetDurationTag,
                mediaSequenceTag, endListTag, playlistTypeTag,
                iFrameOnlyTag, discontinuitySequenceTag, mSegmentList);
    }

    /**
     * 创建主播放列表
     */
    private Playlist createMasterPlaylist() {
        VersionTag versionTag = null;
        List<Rendition> renditionList = new LinkedList<>();

        while (!mBasicMeta.isEmpty()) {
            Tag tag = mBasicMeta.remove(0);

            String tagName = tag.getName();
            if (tagName.equals(Tag.Name.VERSION)) {
                versionTag = (VersionTag)tag;
            }
        }

        while (!mMasterPlaylistMeta.isEmpty()) {
            Tag tag = mMediaPlaylistMeta.remove(0);

            String tagName = tag.getName();
            if (tagName.equals(Tag.Name.MEDIA)) {
                renditionList.add(new Rendition((MediaTag)tag));
            }
        }

        return new MasterPlaylist(mBaseUri, versionTag,
                renditionList, mStreamList, mIFrameStreamList);
    }

    /**
     * 安静地关闭
     */
    public void close() {
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
