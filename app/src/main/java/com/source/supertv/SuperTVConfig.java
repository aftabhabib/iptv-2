package com.source.supertv;

import org.json.JSONException;
import org.json.JSONObject;

final class SuperTVConfig {
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
        try {
            JSONObject rootObj = new JSONObject(content);

            JSONObject dbObj = rootObj.getJSONObject("db");
            String dbUrl = dbObj.getString("url");
            long dbUptime = dbObj.getLong("uptime");

            return new SuperTVConfig(dbUrl, dbUptime);
        }
        catch (JSONException e) {
            throw new IllegalArgumentException("malformed");
        }
    }
}
