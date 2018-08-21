package com.iptv.core.resource.firetv;

import com.iptv.core.utils.IOUtils;
import com.iptv.core.utils.StringOutputStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * 参数
 */
final class SoftParameters {
    private String mContent;

    /**
     * 构造函数
     */
    public SoftParameters() {
        mContent = "";
    }

    /**
     * 写入内容
     */
    public boolean write(InputStream input) {
        StringOutputStream output = new StringOutputStream();

        try {
            IOUtils.save(input, output);

            /**
             * 参数与参数之间通过“|”分隔
             */
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
    public String[] parse() {
        if (mContent.contains("|")) {
            return mContent.split("|");
        }
        else {
            return new String[] { mContent };
        }
    }
}
