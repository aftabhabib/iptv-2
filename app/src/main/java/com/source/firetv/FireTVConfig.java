package com.source.firetv;

final class FireTVConfig {
    private String mTvlistDate;
    private String mYzkey;

    private FireTVConfig(String tvlistDate, String yzKey) {
        mTvlistDate = tvlistDate;
        mYzkey = yzKey;
    }

    public String getTvlistDate() {
        return mTvlistDate;
    }

    public String getYzkey() {
        return mYzkey;
    }

    public static FireTVConfig parse(String content) {
        String[] arrPart = content.split("|");
        if (arrPart.length < 4) {
            throw new IllegalArgumentException("malformed");
        }

        return new FireTVConfig(arrPart[0], arrPart[3]);
    }
}
