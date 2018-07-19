package com.utils;

import android.util.Log;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;

public class ZipHelper {
    private static final String TAG = "ZipHelper";

    public static boolean extract(File srcFile, String dstDir, String password) {
        try {
            ZipFile zipFile = new ZipFile(srcFile);

            if (!zipFile.isValidZipFile()) {
                Log.e(TAG, srcFile.getName() + " is not zip format");
                return false;
            }

            zipFile.setFileNameCharset("GBK"); //防止解压之后的中文文件名乱码

            if (zipFile.isEncrypted()) {
                if (password == null || password.isEmpty()) {
                    Log.e(TAG, srcFile.getName() + "is encrypted, need password");
                    return false;
                }

                zipFile.setPassword(password);
            }

            zipFile.extractAll(dstDir);
        }
        catch (ZipException e) {
            Log.e(TAG, "extract fail, " + e.getMessage());
            return false;
        }

        return true;
    }
}
