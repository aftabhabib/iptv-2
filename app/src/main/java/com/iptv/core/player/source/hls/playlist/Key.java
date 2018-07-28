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

    public static Key parse(String attributeList) {
        Key key = new Key();

        String[] attributeArray = attributeList.split(",");
        for (int i = 0; i < attributeArray.length; i++) {
            Attribute attribute = Attribute.parse(attributeArray[i]);

            if (attribute.getKey().equals(Attribute.ATTR_METHOD)) {
                key.setMethod(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_URI)) {
                key.setUri(attribute.getValue());
            }
            else if (attribute.getKey().equals(Attribute.ATTR_IV)) {
                key.setInitVector(attribute.getValue());
            }
        }

        return key;
    }
}
