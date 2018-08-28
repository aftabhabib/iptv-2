package com.iptv.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

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
     * 文本内容以行为单位
     */
    public static String[] lines(String content) {
        List<String> lineList = new LinkedList<String>();

        BufferedReader reader = new BufferedReader(new StringReader(content));
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                lineList.add(line);
            }
        }
        catch (IOException e) {
            /**
             * 出错，清除中间结果
             */
            if (!lineList.isEmpty()) {
                lineList.clear();
            }
        }
        finally {
            try {
                reader.close();
            }
            catch (IOException e) {
                /**
                 * ignore
                 */
            }
        }

        return lineList.isEmpty() ? null : lineList.toArray(new String[lineList.size()]);
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
