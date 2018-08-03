package com.iptv.core.hls.playlist;

import java.math.BigInteger;

public final class MediaSegment {
    private float mDuration;
    private String mUri;
    private int mSequenceNumber;

    private boolean mIsDiscontinuous;
    private ByteRange mRange;
    private Key mKey;

    private MediaSegment(float duration, String uri, int sequenceNumber,
                         boolean isDiscontinuous, ByteRange range, Key key) {
        mDuration = duration;
        mUri = uri;
        mSequenceNumber = sequenceNumber;

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
    public String getUri() {
        return mUri;
    }

    /**
     * 获取媒体序号
     */
    public int getSequenceNumber() {
        return mSequenceNumber;
    }

    /**
     * 是否定义了范围
     */
    public boolean containsRange() {
        return mRange != null;
    }

    /**
     * 获取Range的值
     */
    public String getRangeValue() {
        return mRange.toString();
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
        return (mKey != null) && !mKey.getMethod().equals(Key.METHOD_NONE);
    }

    /**
     * 是不是MediaSample加密
     */
    public boolean isMediaSampleEncrypted() {
        return mKey.getMethod().equals(Key.METHOD_SAMPLE_AES);
    }

    /**
     * 获取密钥的uri
     */
    public String getKeyUri() {
        return mKey.getUri();
    }

    /**
     * 获取密钥的IV
     */
    public byte[] getKeyInitVector() {
        String initVector;

        if (mKey.containsInitVector()) {
            initVector = mKey.getInitVector();

            if (!initVector.startsWith("0x") && !initVector.startsWith("0X")) {
                throw new IllegalArgumentException("must be hexadecimal-sequence");
            }

            initVector = initVector.substring(2);
        }
        else {
            /**
             * sequence number is to be used as the IV
             */
            initVector = Long.toHexString(mSequenceNumber);
        }

        return new BigInteger(initVector, 16).toByteArray();
    }

    public static class Builder {
        private float mDuration;
        private String mUri;
        private int mSequenceNumber;

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

        public void setUri(String uri) {
            mUri = uri;
        }

        public void setSequenceNumber(int sequenceNumber) {
            mSequenceNumber = sequenceNumber;
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
            return new MediaSegment(mDuration, mUri, mSequenceNumber,
                    mIsDiscontinuous, mRange, mKey);
        }

        public Builder fork() {
            Builder builder = new Builder();

            if (mRange != null) {
                /**
                 * 下一个片段的Range定义可能是相对的
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
