package com.iptv.core.player.source.hls.playlist;

public class VariantStream {
    private int mBandwidth;
    private String mUri;

    private int mVideoWidth;
    private int mVideoHeight;

    private String mAudioGroup;
    private String mVideoGroup;
    private String mSubtitleGroup;

    private VariantStream() {
        //ignore
    }

    public int getBandwidth() {
        return mBandwidth;
    }

    public String getUri(String uri) {
        return mUri;
    }

    private void setBandwidth(int bandwidth) {
        mBandwidth = bandwidth;
    }

    private void setUri(String uri) {
        mUri = uri;
    }

    private void setVideoWidth(int videoWidth) {
        mVideoWidth = videoWidth;
    }

    private void setVideoHeight(int videoHeight) {
        mVideoHeight = videoHeight;
    }

    private void setAudioGroup(String audioGroup) {
        mAudioGroup = audioGroup;
    }

    private void setVideoGroup(String videoGroup) {
        mVideoGroup = videoGroup;
    }

    private void setSubtitleGroup(String subtitleGroup) {
        mSubtitleGroup = subtitleGroup;
    }

    public static class Builder {
        /**
         * required
         */
        private int mBandwidth;
        private String mUri;

        /**
         * optional
         */
        private int mVideoWidth;
        private int mVideoHeight;

        private String mAudioGroup;
        private String mVideoGroup;
        private String mSubtitleGroup;

        public Builder() {
            //ignore
        }

        public void setBandwidth(String bandwidth) {
            mBandwidth = Integer.parseInt(bandwidth);
        }

        public void setCodecs(String codecs) {
            String[] codecArray = codecs.split(",");

            for (int i = 0; i < codecArray.length; i++) {
                /**
                 * TODO: codec type, profile and level
                 */
            }
        }

        public void setResolution(String resolution) {
            String[] result = resolution.split("x");

            mVideoWidth = Integer.parseInt(result[0]);
            mVideoHeight = Integer.parseInt(result[1]);
        }

        public void setAudioGroup(String audioGroup) {
            mAudioGroup = audioGroup;
        }

        public void setVideoGroup(String videoGroup) {
            mVideoGroup = videoGroup;
        }

        public void setSubtitleGroup(String subtitleGroup) {
            mSubtitleGroup = subtitleGroup;
        }

        public void setUri(String uri) {
            mUri = uri;
        }

        public VariantStream build() {
            VariantStream stream = new VariantStream();

            stream.setBandwidth(mBandwidth);
            stream.setUri(mUri);

            if (mVideoWidth > 0 && mVideoHeight > 0) {
                stream.setVideoWidth(mVideoWidth);
                stream.setVideoHeight(mVideoHeight);
            }

            if (mAudioGroup != null) {
                stream.setAudioGroup(mAudioGroup);
            }

            if (mVideoGroup != null) {
                stream.setVideoGroup(mVideoGroup);
            }

            if (mSubtitleGroup != null) {
                stream.setSubtitleGroup(mSubtitleGroup);
            }

            return stream;
        }
    }
}
