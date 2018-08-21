package com.iptv.core.resource.firetv;

import com.iptv.core.utils.IOUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 频道包
 */
final class TVListZip {
    private File mFile;

    /**
     * 构造函数
     */
    public TVListZip(File dir) {
        mFile = new File(dir, "tvlist.zip");
    }

    /**
     * 写入数据
     */
    public boolean write(InputStream input) {
        FileOutputStream output = null;

        try {
            output = new FileOutputStream(mFile);
            IOUtils.save(input, output);
        }
        catch (IOException e) {
            /**
             * 下载失败，删除未完成的文件
             */
            if (mFile.exists()) {
                mFile.delete();
            }
        }
        finally {
            if (output != null) {
                try {
                    output.close();
                }
                catch (IOException e) {
                    /**
                     * ignore
                     */
                }
            }
        }

        return mFile.exists();
    }

    /**
     * 是否存在
     */
    public boolean exists() {
        return mFile.exists();
    }

    /**
     * 提取
     */
    public ZipInputStream extract() throws ZipException {
        ZipFile zipFile = new ZipFile(mFile);
        if (!zipFile.isValidZipFile()) {
            return null;
        }

        if (zipFile.isEncrypted()) {
            zipFile.setPassword("FiReTvtEst@Qq.cOm2");
        }

        FileHeader fileHeader = zipFile.getFileHeader("tvlist.xml");
        if (fileHeader == null) {
            return null;
        }

        return zipFile.getInputStream(fileHeader);
    }
}
