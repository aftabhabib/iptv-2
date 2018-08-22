package com.iptv.core.player;

import java.util.ArrayList;
import java.util.List;

/**
 * 清单（列出媒体信息）
 */
public class Manifest {
    private MetaData mMetaData;
    private List<TrackInfo> mTrackList;

    /**
     * 构造函数
     */
    public Manifest() {
        mMetaData = new MetaData();
        mTrackList = new ArrayList<TrackInfo>();
    }

    /**
     * 获取元信息
     */
    public MetaData getMetaData() {
        return mMetaData;
    }

    /**
     * 增加轨道
     */
    public void addTrack(TrackInfo track) {
        mTrackList.add(track);
    }

    /**
     * 获取媒体中的轨道信息
     */
    public TrackInfo[] getTracks() {
        return mTrackList.toArray(new TrackInfo[mTrackList.size()]);
    }
}
