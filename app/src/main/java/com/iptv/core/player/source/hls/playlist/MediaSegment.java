package com.iptv.core.player.source.hls.playlist;

public class MediaSegment {
    private float mDuration;
    private String mUri;

    private boolean mIsDiscontinuous;
    private ByteRange mRange;
    private Key mKey;

    public MediaSegment() {
        //ignore
    }

    /**
     * 获取时长（单位：毫秒）
     */
    public int getDuration() {
        return (int)(mDuration * 1000);
    }

    /**
     * 获取uri
     */
    public String getUri() {
        return mUri;
    }

    /**
     * 获取范围
     */
    public ByteRange getRange() {
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

    private void setDuration(float duration) {
        mDuration = duration;
    }

    private void setUri(String uri) {
        mUri = uri;
    }

    private void setDiscontinuity() {
        mIsDiscontinuous = true;
    }

    private void setRange(ByteRange range) {
        mRange = range;
    }

    private void setKey(Key key) {
        mKey = key;
    }

    public static class Builder {
        /**
         * required
         */
        private float mDuration;
        private String mUri;

        /**
         * optional
         */
        private boolean mIsDiscontinuous;
        private ByteRange mRange;
        private Key mKey;

        public Builder() {
            //ignore
        }

        public Builder(Builder other) {
            if (other.mKey != null) {
                mKey = other.mKey;
            }
        }

        public void setDuration(String duration) {
            mDuration = Float.parseFloat(duration);
        }

        public void setUri(String uri) {
            mUri = uri;
        }

        public void setDiscontinuity() {
            mIsDiscontinuous = true;
        }

        public void setRange(ByteRange range) {
            mRange = range;
        }

        public void setKey(Key key) {
            mKey = key;
        }

        public MediaSegment build() {
            MediaSegment segment = new MediaSegment();

            segment.setDuration(mDuration);
            segment.setUri(mUri);

            if (mIsDiscontinuous) {
                segment.setDiscontinuity();
            }

            if (mRange != null) {
                segment.setRange(mRange);
            }

            if (mKey != null) {
                segment.setKey(mKey);
            }

            return segment;
        }
    }
}
