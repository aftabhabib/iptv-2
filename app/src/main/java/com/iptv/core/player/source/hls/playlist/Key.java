package com.iptv.core.player.source.hls.playlist;

public class Key {
    private static final String METHOD_NONE = "NONE";
    private static final String METHOD_AES_128 = "AES-128";
    private static final String METHOD_SAMPLE_AES = "SAMPLE-AES";

    private String mMethod;
    private String mUri;
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

    private static final String ATTR_METHOD = "METHOD";
    private static final String ATTR_URI = "URI";
    private static final String ATTR_IV = "IV";
    private static final String ATTR_FORMAT = "KEYFORMAT";
    private static final String ATTR_FORMAT_VERSIONS = "KEYFORMATVERSIONS";

    public static Key create(String[] attribute) {
        Key key = new Key();

        for (int i = 0; i < attribute.length; i++) {
            String[] result = attribute[i].split("=");

            String attrName = result[0];
            String attrValue = result[1];

            if (attrName.equals(ATTR_METHOD)) {
                key.setMethod(attrValue);
            }
            else if (attrName.equals(ATTR_URI)) {
                key.setUri(attrValue);
            }
            else if (attrName.equals(ATTR_IV)) {
                key.setInitVector(attrValue);
            }
            else if (attrName.equals(ATTR_FORMAT)) {
                //
            }
            else if (attrName.equals(ATTR_FORMAT_VERSIONS)) {
                //
            }
            else {
                //ignore
            }
        }

        return key;
    }
}
