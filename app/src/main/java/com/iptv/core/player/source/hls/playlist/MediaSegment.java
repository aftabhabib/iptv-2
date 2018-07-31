package com.iptv.core.player.source.hls.playlist;

import android.net.Uri;

public final class MediaSegment {
    private float mDuration;
    private Uri mUri;

    private boolean mIsDiscontinuous;
    private ByteRange mRange;
    private Key mKey;

    private MediaSegment(float duration, Uri uri,
                         boolean isDiscontinuous, ByteRange range, Key key) {
        mDuration = duration;
        mUri = uri;

        mIsDiscontinuous = isDiscontinuous;
        mRange = range;
        mKey = key;
    }

    /**
     * 获取时长（单位：毫秒）
     */
    public int getDuration() {
        return (int)(mDuration * 1000);
    }

    /**
     * 获取媒体的uri
     */
    public Uri getUri() {
        return mUri;
    }

    /**
     * 是否是媒体数据的子范围
     */
    public boolean isSubRange() {
        return mRange != null;
    }

    /**
     * 获取子范围
     */
    public ByteRange getSubRange() {
        return mRange;
    }

    /**
     * 是否不连续
     */
    public boolean isDiscontinuous() {
        return mIsDiscontinuous;
    }

    /**
     * 是否加密
     */
    public boolean isEncrypted() {
        return ((mKey != null) && mKey.isEncrypted());
    }

    public static class Builder {
        private float mDuration;
        private Uri mUri;

        private boolean mIsDiscontinuous = false;
        private ByteRange mRange = null;
        private Key mKey = null;

        private long mRangeOffset = -1;

        public Builder() {
            /**
             * nothing
             */
        }

        public void setDuration(String duration) {
            mDuration = Float.parseFloat(duration);
        }

        public void setUri(Uri uri) {
            mUri = uri;
        }

        public void setDiscontinuity() {
            mIsDiscontinuous = true;
        }

        public void setRange(String range) {
            String[] result = range.split("@");

            long length = Long.parseLong(result[0]);
            long offset = (result.length > 1) ? Long.parseLong(result[1]) : mRangeOffset;

            mRange = new ByteRange(length, offset);
        }

        public void setKey(Key key) {
            mKey = key;
        }

        public MediaSegment build() {
            return new MediaSegment(mDuration, mUri, mIsDiscontinuous, mRange, mKey);
        }

        public Builder fork() {
            Builder builder = new Builder();

            if (mRange != null) {
                /**
                 * 下一个的Range定义可能是相对的
                 */
                builder.setRangeOffset(mRange.getOffset() + mRange.getLength());
            }

            if (mKey != null) {
                /**
                 * 直到下一个Key出现，都有效
                 */
                builder.setKey(mKey);
            }

            return builder;
        }

        private void setRangeOffset(long offset) {
            mRangeOffset = offset;
        }
    }
}
