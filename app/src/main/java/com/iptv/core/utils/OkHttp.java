package com.iptv.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp {
    private static final int CONNECT_TIMEOUT = 5;
    private static final int READ_TIMEOUT = 5;
    private static final int WRITE_TIMEOUT = 5;

    private static OkHttpClient sClient = null;

    /**
     * GET
     */
    public static Response get(String url, Map<String, String> properties) throws IOException {
        Request request = OkHttp.createGetRequest(url, properties);

        return OkHttp.getClient().newCall(request).execute();
    }

    /**
     * 获取Client（参考文档中的介绍：Client是可复用的，每个请求视为一个Call）
     */
    private static OkHttpClient getClient() {
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
     * 创建GET请求
     */
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

    /**
     * 创建POST请求
     */
    private static Request createPostRequest(String url, Map<String, String> property, RequestBody body) {
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
     * 创建POST请求的文本负载
     */
    private static RequestBody createTextBody(String text, String mime) {
        return RequestBody.create(MediaType.parse(mime), text);
    }

    /**
     * 创建POST请求的表格负载
     */
    private static RequestBody createFormBody(Map<String, String> form) {
        FormBody.Builder builder = new FormBody.Builder();

        for (Map.Entry<String, String> entry : form.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }

        return builder.build();
    }

    /**
     * 构造函数（私有属性，不允许创建实例）
     */
    private OkHttp() {
        /**
         * nothing
         */
    }
}
