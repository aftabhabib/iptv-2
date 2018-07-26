package com.iptv.core.source.firetv;

import android.util.Log;

final class FireTVConfig {
    private static final String TAG = "FireTVConfig";

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
        String[] results = content.split("|");
        if (results.length < 4) {
            Log.e(TAG, "parse fail");
            return null;
        }

        return new FireTVConfig(results[0], results[3]);
    }
}
