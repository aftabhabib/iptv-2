package com.iptv.core.hls.playlist;

import com.iptv.core.hls.playlist.io.PlaylistReader;
import com.iptv.core.hls.playlist.tag.VersionTag;

import java.io.IOException;
import java.util.Map;

/**
 * 播放列表
 */
public abstract class Playlist {
    public static final int TYPE_MEDIA = 0;
    public static final int TYPE_MASTER = 1;

    protected String mBaseUri;
    private VersionTag mVersionTag;

    /**
     * 构造函数
     */
    public Playlist(String baseUri, VersionTag versionTag) {
        mBaseUri = baseUri;
        mVersionTag = versionTag;
    }

    /**
     * 获取类型
     */
    public abstract int getType();

    /**
     * 获取协议版本
     */
    public int getProtocolVersion() {
        if (mVersionTag == null) {
            return 1;
        }
        else {
            return mVersionTag.getVersion();
        }
    }

    /**
     * 加载播放列表
     */
    public static Playlist load(String url, Map<String, String> properties) throws IOException {
        Playlist playlist;

        PlaylistReader reader = new PlaylistReader(url, properties);
        try {
            playlist = reader.read();
        }
        catch (IOException e) {
            playlist = null;
        }
        reader.close();

        if (playlist == null) {
            throw new IOException("load playlist fail");
        }

        return playlist;
    }
}
