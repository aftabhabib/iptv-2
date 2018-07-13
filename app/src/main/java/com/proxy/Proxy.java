package com.proxy;

public interface Proxy {
    String start(String url);

    void stop();

    void release();
}
