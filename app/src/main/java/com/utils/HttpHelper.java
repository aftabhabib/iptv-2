package com.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpHelper {
    private static final String TAG = "HttpHelper";

    public static byte[] opGet(String url, Map<String, String> property) {
        byte[] content = null;

        try {
            Request request = createGetRequest(url, property);

            Response response = new OkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                content = response.body().bytes();
            }
            else {
                Log.e(TAG, "HTTP GET fail, " + response.message());
                response.close();
            }
        }
        catch (IOException e) {
            Log.e(TAG, "HTTP GET error, " + e.getMessage());
        }

        return content;
    }

    public static boolean opDownload(String url, Map<String, String> property, File dstFile) {
        boolean ret = false;

        /**
         * 如果已经存在，则先删除
         */
        if (dstFile.exists()) {
            dstFile.delete();
        }

        try {
            Request request = createGetRequest(url, property);

            Response response = new OkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream input = response.body().byteStream();
                OutputStream output = new FileOutputStream(dstFile);

                byte[] buf = new byte[1024];
                while (true) {
                    int bytesRead = input.read(buf);
                    if (bytesRead == -1) {
                        break;
                    }

                    output.write(buf, 0, bytesRead);
                }

                input.close();
                output.close();

                ret = true;
            }
            else {
                Log.e(TAG, "HTTP GET fail, " + response.message());
                response.close();
            }
        }
        catch (IOException e) {
            Log.e(TAG, "HTTP GET error, " + e.getMessage());

            /**
             * 下载过程中出错，删除未完成的文件
             */
            if (dstFile.exists()) {
                dstFile.delete();
            }
        }

        return ret;
    }

    public static byte[] opPost(String url, String xml, Map<String, String> property) {
        byte[] content = null;

        try {
            Request request = createPostRequest(url, createXmlBody(xml), property);

            Response response = new OkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                content = response.body().bytes();
            }
            else {
                Log.e(TAG, "HTTP POST fail, " + response.message());
                response.close();
            }
        }
        catch (IOException e) {
            Log.e(TAG, "HTTP POST error, " + e.getMessage());
        }

        return content;
    }

    private static Request createPostRequest(String url, RequestBody body, Map<String, String> property) {
        Request.Builder builder = new Request.Builder();

        builder.url(url);
        builder.post(body);

        if (property != null && !property.isEmpty()) {
            for (Map.Entry<String, String> entry : property.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return builder.build();
    }

    private static RequestBody createXmlBody(String xml) {
        return RequestBody.create(MediaType.parse("application/xml"), xml);
    }

    private static RequestBody createFormBody(Map<String, String> form) {
        FormBody.Builder builder = new FormBody.Builder();

        for (String key : form.keySet()) {
            builder.add(key, form.get(key));
        }

        return builder.build();
    }

    private static Request createGetRequest(String url, Map<String, String> property) {
        Request.Builder builder = new Request.Builder();

        builder.url(url);
        builder.get();

        if (property != null && !property.isEmpty()) {
            for (Map.Entry<String, String> entry : property.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return builder.build();
    }

    private HttpHelper() {
        //ignore
    }
}
