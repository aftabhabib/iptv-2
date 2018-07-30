package com.iptv.core.player.source.hls.playlist;

public class Key {
    private static final String METHOD_NONE = "NONE";
    private static final String METHOD_AES_128 = "AES-128";
    private static final String METHOD_SAMPLE_AES = "SAMPLE-AES";

    /**
     * required
     */
    private String mMethod;
    private String mUri;

    /**
     * optional
     */
    private String mInitVector;

    private Key() {
        //ignore
    }

    public boolean isEncrypted() {
        return mMethod.equals(METHOD_NONE);
    }

    private void setMethod(String method) {
        mMethod = method;
    }

    private void setUri(String uri) {
        mUri = uri;
    }

    private void setInitVector(String initVector) {
        mInitVector = initVector;
    }

    public static class Builder {
        /**
         * required
         */
        private String mMethod;

        /**
         * optional
         */
        private String mUri;
        private String mInitVector;

        public Builder() {
            //ignore
        }

        public void setMethod(String method) {
            mMethod = method;
        }

        public void setUri(String uri) {
            mUri = uri;
        }

        public void setInitVector(String initVector) {
            mInitVector = initVector;
        }

        public Key build() {
            Key key = new Key();

            key.setMethod(mMethod);

            if (mUri != null) {
                key.setUri(mUri);
            }

            if (mInitVector != null) {
                key.setInitVector(mInitVector);
            }

            return key;
        }
    }
}
