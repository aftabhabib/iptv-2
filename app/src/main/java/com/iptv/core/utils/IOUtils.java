package com.iptv.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {
    /**
     * 另存为
     */
    public static void save(InputStream input, OutputStream output) throws IOException {
        byte[] buf = new byte[1024];

        while (true) {
            int bytesRead = input.read(buf);
            if (bytesRead == -1) {
                break;
            }

            output.write(buf, 0, bytesRead);
        }
    }

    /**
     * 构造函数（私有属性，不允许创建实例）
     */
    private IOUtils() {
        /**
         * nothing
         */
    }
}
