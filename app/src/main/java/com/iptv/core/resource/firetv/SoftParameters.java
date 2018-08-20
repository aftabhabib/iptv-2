package com.iptv.core.resource.firetv;

import com.iptv.core.utils.IOUtils;
import com.iptv.core.utils.StringOutputStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * 参数
 */
final class SoftParameters {
    private String[] mParameters = null;

    /**
     * 构造函数
     */
    public SoftParameters() {
        /**
         * nothing
         */
    }

    /**
     * 是否有数据
     */
    public boolean isEmpty() {
        return mParameters == null;
    }

    /**
     * 写入数据
     */
    public boolean write(InputStream input) {
        StringOutputStream output = new StringOutputStream();

        try {
            IOUtils.save(input, output);

            /**
             * 参数与参数之间通过“|”分隔
             */
            String content = output.toString();
            if (!content.isEmpty() && content.contains("|")) {
                mParameters = content.split("|");
            }
        }
        catch (IOException e) {
            /**
             * ignore
             */
        }

        return mParameters != null;
    }

    /**
     * 获取频道列表文件的日期
     */
    public String getTVListDate() {
        return mParameters[0];
    }

    /**
     * 获取Key（意义不明）
     */
    public String getKey() {
        return mParameters[3];
    }
}
