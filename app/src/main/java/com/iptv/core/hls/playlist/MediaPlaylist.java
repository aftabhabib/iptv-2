package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.datatype.EnumeratedString;
import com.iptv.core.hls.playlist.tag.DiscontinuitySequenceTag;
import com.iptv.core.hls.playlist.tag.EndListTag;
import com.iptv.core.hls.playlist.tag.IFrameOnlyTag;
import com.iptv.core.hls.playlist.tag.MediaSequenceTag;
import com.iptv.core.hls.playlist.tag.PlaylistTypeTag;
import com.iptv.core.hls.playlist.tag.TargetDurationTag;

import java.util.List;

/**
 * 媒体播放列表
 */
public final class MediaPlaylist extends Playlist {
    private TargetDurationTag mTargetDurationTag;
    private MediaSequenceTag mMediaSequenceTag;
    private EndListTag mEndListTag;
    private PlaylistTypeTag mPlaylistTypeTag;
    private IFrameOnlyTag mIFrameOnlyTag;
    private DiscontinuitySequenceTag mDiscontinuitySequenceTag;

    private List<Segment> mSegmentList;

    /**
     * 构造函数
     */
    public MediaPlaylist(TargetDurationTag targetDurationTag,
                         MediaSequenceTag mediaSequenceTag,
                         EndListTag endListTag,
                         PlaylistTypeTag playlistTypeTag,
                         IFrameOnlyTag iFrameOnlyTag,
                         DiscontinuitySequenceTag discontinuitySequenceTag,
                         List<Segment> segmentList) {
        super();

        mTargetDurationTag = targetDurationTag;
        mMediaSequenceTag = mediaSequenceTag;
        mEndListTag = endListTag;
        mPlaylistTypeTag = playlistTypeTag;
        mIFrameOnlyTag = iFrameOnlyTag;
        mDiscontinuitySequenceTag = discontinuitySequenceTag;

        mSegmentList = segmentList;
    }

    /**
     * 获取最大的片段时长
     */
    public int getMaxSegmentDuration() {
        return mTargetDurationTag.getDuration();
    }

    /**
     * 是否不再有片段
     */
    public boolean noMoreSegments() {
        return mEndListTag != null;
    }

    /**
     * 获取类型
     */
    public String getType() {
        if (mPlaylistTypeTag == null) {
            return "undefined";
        }
        else {
            return mPlaylistTypeTag.getType();
        }
    }

    /**
     * 是不是只有I帧
     */
    public boolean isIFrameOnly() {
        return mIFrameOnlyTag != null;
    }

    /**
     * 获取起始序号
     */
    private long getFirstSequenceNumber() {
        if (mMediaSequenceTag == null) {
            return 0;
        }
        else {
            return mMediaSequenceTag.getSequenceNumber();
        }
    }

    /**
     * 获取起始不连续序号
     */
    private long getFirstDiscontinuitySequenceNumber() {
        if (mDiscontinuitySequenceTag == null) {
            return 0;
        }
        else {
            return mDiscontinuitySequenceTag.getSequenceNumber();
        }
    }

    /**
     * 获取片段总数
     */
    public int getSegmentCount() {
        return mSegmentList.size();
    }

    /**
     * 获取片段总数
     */
    public Segment getSegment(int index) {
        if (index < 0 || index >= mSegmentList.size()) {
            throw new IllegalArgumentException("invalid index");
        }

        return mSegmentList.get(index);
    }

    /**
     * 获取播放时长
     */
    public float getPlayDuration() {
        float duration = 0;

        for (int i = 0; i < mSegmentList.size(); i++) {
            duration += mSegmentList.get(i).getDuration();
        }

        return duration;
    }

    /**
     * 与另一媒体播放列表相比是否更新、追加了片段
     */
    public boolean isNewerThan(MediaPlaylist other) {
        if (getType().equals(EnumeratedString.VOD)
                || (getType().equals(EnumeratedString.EVENT) && noMoreSegments())) {
            throw new IllegalStateException("playlist should not change");
        }

        if ((getFirstSequenceNumber() > other.getFirstSequenceNumber())
                || (getSegmentCount() > other.getSegmentCount())) {
            return true;
        }

        return false;
    }
}
