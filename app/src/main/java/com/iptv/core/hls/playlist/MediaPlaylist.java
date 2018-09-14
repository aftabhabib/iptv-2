package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.datatype.EnumeratedString;

import java.util.ArrayList;
import java.util.List;

/**
 * 媒体播放列表
 */
public final class MediaPlaylist {
    private int mVersion = 3;
    private int mTargetDuration = 10;
    private int mMediaSequence = 0;
    private int mDiscontinuitySequence = 0;
    private boolean mEndOfList = false;
    private String mType = null;
    private boolean mIFrameOnly = false;

    private List<Segment> mSegmentList = new ArrayList<>();

    /**
     * 构造函数
     */
    public MediaPlaylist() {
        /**
         * nothing
         */
    }

    /**
     * 设置版本
     */
    public void setVersion(int version) {
        if (version <= 0) {
            throw new IllegalArgumentException("invalid version");
        }

        mVersion = version;
    }

    /**
     * 设置片段的最大时长
     */
    public void setTargetDuration(int targetDuration) {
        if (targetDuration <= 0) {
            throw new IllegalArgumentException("invalid target duration");
        }

        mTargetDuration = targetDuration;
    }

    /**
     * 设置起始片段的序号
     */
    public void setMediaSequence(int sequenceNumber) {
        if (sequenceNumber < 0) {
            throw new IllegalArgumentException("invalid sequence number");
        }

        mMediaSequence = sequenceNumber;
    }

    /**
     * 设置起始片段的不连续序号
     */
    public void setDiscontinuitySequence(int sequenceNumber) {
        if (sequenceNumber < 0) {
            throw new IllegalArgumentException("invalid sequence number");
        }

        mDiscontinuitySequence = sequenceNumber;
    }

    /**
     * 设置列表结束
     */
    public void setEndOfList() {
        mEndOfList = true;
    }

    /**
     * 设置类型
     */
    public void setType(String type) {
        if ((type == null) || !isValidType(type)) {
            throw new IllegalArgumentException("invalid type");
        }

        mType = type;
    }

    /**
     * 是不是有效的类型
     */
    private static boolean isValidType(String type) {
        return type.equals(EnumeratedString.EVENT) || type.equals(EnumeratedString.VOD);
    }

    /**
     * 设置（片段）只有I帧（快速浏览）
     */
    public void setIFrameOnly() {
        mIFrameOnly = true;
    }

    /**
     * 加入片段
     */
    public void addSegment(Segment segment) {
        if ((segment == null) || !isValidSegment(segment)) {
            throw new IllegalArgumentException("invalid segment");
        }

        mSegmentList.add(segment);
    }

    /**
     * 是不是有效的片段
     */
    private boolean isValidSegment(Segment segment) {
        return true;
    }

    /**
     * 是否定义了类型
     */
    public boolean containsType() {
        return mType != null;
    }

    /**
     * 是否定义了片段
     */
    public boolean containsSegment() {
        return !mSegmentList.isEmpty();
    }

    /**
     * 获取版本号
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * 获取片段的最大时长
     */
    public int getTargetDuration() {
        return mTargetDuration;
    }

    /**
     * 获取起始片段的序号
     */
    public int getMediaSequence() {
        return mMediaSequence;
    }

    /**
     * 获取起始片段的不连续序号
     */
    public int getDiscontinuitySequence() {
        return mDiscontinuitySequence;
    }

    /**
     * 播放列表是否结束
     */
    public boolean endOfList() {
        return mEndOfList;
    }

    /**
     * 获取类型
     */
    public String getType() {
        if (!containsType()) {
            throw new IllegalStateException("no type");
        }

        return mType;
    }

    /**
     * 是不是只有I帧
     */
    public boolean isIFrameOnly() {
        return mIFrameOnly;
    }

    /**
     * 获取所有片段
     */
    public Segment[] getSegments() {
        if (!containsSegment()) {
            throw new IllegalStateException("no segment");
        }

        return mSegmentList.toArray(new Segment[mSegmentList.size()]);
    }
}
