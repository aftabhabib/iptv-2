package com.iptv.core.source.supertv;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

final class SuperTVConfig {
    private static final String TAG = "SuperTVConfig";

    private String mDatabaseUrl;
    private long mDatabaseUptime;

    private SuperTVConfig(String dbUrl, long dbUptime) {
        mDatabaseUrl = dbUrl;
        mDatabaseUptime = dbUptime;
    }

    public String getDatabaseUrl() {
        return mDatabaseUrl;
    }

    public long getDatabaseUptime() {
        return mDatabaseUptime;
    }

    public static SuperTVConfig parse(String content) {
        SuperTVConfig config = null;

        try {
            JSONObject rootObj = new JSONObject(content);

            JSONObject dbObj = rootObj.getJSONObject("db");
            String dbUrl = dbObj.getString("url");
            long dbUptime = dbObj.getLong("uptime");

            config = new SuperTVConfig(dbUrl, dbUptime);
        }
        catch (JSONException e) {
            Log.e(TAG, "parse fail");
        }

        return config;
    }
}
