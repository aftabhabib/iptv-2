package com.iptv.core.utils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttp {
    private static final int CONNECT_TIMEOUT = 5;
    private static final int READ_TIMEOUT = 5;
    private static final int WRITE_TIMEOUT = 5;

    private static OkHttpClient sClient = null;

    /**
     * Client
     */
    public static OkHttpClient getClient() {
        if (sClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.followRedirects(true);
            builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);

            sClient = builder.build();
        }

        return sClient;
    }

    /**
     * POST request
     */
    public static Request createPostRequest(String url, Map<String, String> property, RequestBody body) {
        Request.Builder builder = new Request.Builder();

        builder.url(url);

        if (property != null && !property.isEmpty()) {
            for (Map.Entry<String, String> entry : property.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        builder.post(body);

        return builder.build();
    }

    /**
     * GET request
     */
    public static Request createGetRequest(String url, Map<String, String> property) {
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

    /**
     * Text body for POST request
     */
    public static RequestBody createTextBody(String text, String mime) {
        return RequestBody.create(MediaType.parse(mime), text);
    }

    /**
     * Form body for POST request
     */
    public static RequestBody createFormBody(Map<String, String> form) {
        FormBody.Builder builder = new FormBody.Builder();

        for (Map.Entry<String, String> entry : form.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }

        return builder.build();
    }

    /**
     * 不允许创建实例
     */
    private OkHttp() {
        //ignore
    }
}
