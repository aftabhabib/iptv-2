package com.iptv.core.resource.firetv;

import com.iptv.core.utils.IOUtils;
import com.iptv.core.utils.StringOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

final class Key {
    private String mContent;

    /**
     * 构造函数
     */
    public Key() {
        mContent = "";
    }

    /**
     * 写入内容
     */
    public boolean write(InputStream input) {
        StringOutputStream output = new StringOutputStream();

        try {
            IOUtils.save(input, output);

            mContent = output.toString();
        }
        catch (IOException e) {
            /**
             * ignore
             */
        }

        return mContent.isEmpty();
    }

    /**
     * 解析
     */
    public JSONObject parse() {
        JSONObject rootObj = null;

        try {
            rootObj = new JSONObject(mContent);
        }
        catch (JSONException e) {
            /**
             * ignore
             */
        }

        return rootObj;
    }
}
