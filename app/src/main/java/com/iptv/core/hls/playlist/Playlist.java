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
    private static final String TAG_KEY = "#EXT-X-KEY";
    private static final String TAG_BYTE_RANGE = "#EXT-X-BYTERANGE";
    private static final String TAG_MEDIA = "#EXT-X-MEDIA";
    private static final String TAG_STREAM_INF = "#EXT-X-STREAM-INF";

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
    public Playlist(String content) throws MalformedFormatException {
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
            else {
                /**
                 * not support yet
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

                if (mKey != null) {
                    mPendingSegment.setKey(mKey);
                }

                mSegmentList.add(mPendingSegment);
                mPendingSegment = null;
            }
            else if (mPendingStream != null) {
                mPendingStream.setUri(uri);

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
    private void parseKey(String content) throws MalformedFormatException {
        mKey = new Key(content);
    }

    /**
     * 解析播放列表中媒体的定义
     */
    private void parseMedia(String content) throws MalformedFormatException {
        Media media = new Media(content);
        mMediaList.add(media);
    }

    /**
     * 解析播放列表中流的定义
     */
    private void parseStreamInf(String content) throws MalformedFormatException {
        mPendingStream = new Stream(content, mMediaList);
    }

    /**
     * 是否包含流
     */
    public boolean containsStream() {
        return !mStreamList.isEmpty();
    }

    /**
     * 获取适应指定带宽的流
     */
    public Stream getStreamByBandwidth(int bandwidth) {
        int index;

        if (bandwidth > 0) {
            /**
             * 带宽已知
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
             * 带宽未知
             */
            index = mStreamList.size() - 1;
        }

        return mStreamList.get(index);
    }

    /**
     * 是否包含片段
     */
    public boolean containsSegment() {
        return !mSegmentList.isEmpty();
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

    /**
     * 获取片段的起始序号
     */
    public int getMediaSequence() {
        if (!mMetaData.containsKey(MetaData.KEY_MEDIA_SEQUENCE)) {
            return 0;
        }

        return mMetaData.getInteger(MetaData.KEY_MEDIA_SEQUENCE);
    }
}
