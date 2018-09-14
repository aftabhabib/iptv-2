package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.rendition.RenditionList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 主播放列表
 */
public final class MasterPlaylist {
    private List<Stream> mStreamList = new ArrayList<>();

    private RenditionList mRenditionList = new RenditionList();
    private List<IFrameStream> mIFrameStreamList = new ArrayList<>();

    /**
     * 构造函数
     */
    public MasterPlaylist() {
        /**
         * nothing
         */
    }

    /**
     * 添加一个流
     */
    public void addStream(Stream stream) {
        if (!isValidStream(stream)) {
            throw new IllegalArgumentException("invalid stream");
        }

        mStreamList.add(stream);
    }

    /**
     * 是不是有效的流
     */
    private static boolean isValidStream(Stream stream) {
        return stream.containsBandwidth();
    }

    /**
     * 添加一个展示
     */
    public void addRendition(Media rendition) {
        mRenditionList.addRendition(rendition);
    }

    /**
     * 添加一个I帧流
     */
    public void addIFrameStream(IFrameStream stream) {
        if (!isValidIFrameStream(stream)) {
            throw new IllegalArgumentException("invalid I frame stream");
        }

        mIFrameStreamList.add(stream);
    }

    /**
     * 是不是有效的I帧流
     */
    private static boolean isValidIFrameStream(IFrameStream stream) {
        return stream.containsBandwidth() && stream.containsUri();
    }

    /**
     * 是否定义了流
     */
    public boolean containsStream() {
        return !mStreamList.isEmpty();
    }

    /**
     * 是否定义了展示
     */
    public boolean containsRendition() {
        return !mRenditionList.isEmpty();
    }

    /**
     * 是否定义了I帧流
     */
    public boolean containsIFrameStream() {
        return !mIFrameStreamList.isEmpty();
    }

    /**
     * 流按照带宽大小排序
     */
    public void sortStreamByBandwidth() {
        if (!containsStream()) {
            throw new IllegalStateException("no stream");
        }

        if (mStreamList.size() > 1) {
            Collections.sort(mStreamList, new Comparator<Stream>() {
                @Override
                public int compare(Stream stream1, Stream stream2) {
                    return stream1.getBandwidth() - stream2.getBandwidth();
                }
            });
        }
    }

    /**
     * I帧流按照带宽大小排序
     */
    public void sortIFrameStreamByBandwidth() {
        if (!containsIFrameStream()) {
            throw new IllegalStateException("no I frame stream");
        }

        if (mIFrameStreamList.size() > 1) {
            Collections.sort(mIFrameStreamList, new Comparator<IFrameStream>() {
                @Override
                public int compare(IFrameStream stream1, IFrameStream stream2) {
                    return stream1.getBandwidth() - stream2.getBandwidth();
                }
            });
        }
    }

    /**
     * 获取所有流
     */
    public Stream[] getStreams() {
        if (!containsStream()) {
            throw new IllegalStateException("no stream");
        }

        return mStreamList.toArray(new Stream[mStreamList.size()]);
    }

    /**
     * 获取所有I帧流
     */
    public IFrameStream[] getIFrameStreams() {
        if (!containsIFrameStream()) {
            throw new IllegalStateException("no I frame stream");
        }

        return mIFrameStreamList.toArray(new IFrameStream[mIFrameStreamList.size()]);
    }

    /**
     * 获取指定（展示）组内的所有展示
     */
    public Media[] getRenditionsInGroup(String type, String groupId) {
        if (!containsRendition()) {
            throw new IllegalStateException("no rendition");
        }

        return mRenditionList.getRenditionsInGroup(type, groupId);
    }
}
