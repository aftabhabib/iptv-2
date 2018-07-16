package com.utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class GzipHelper {
    private static final String TAG = "GzipHelper";

    public static boolean extract(File srcFile, File dstFile) {
        boolean ret = false;

        FileInputStream input = null;
        try {
            input = new FileInputStream(srcFile);
            ret = extract(input, dstFile);
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "extract gzip fail, " + e.getMessage());
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                }
                catch (IOException e) {
                    //ignore
                }
            }
        }

        return ret;
    }

    public static boolean extract(byte[] data, File dstFile) {
        return extract(new ByteArrayInputStream(data), dstFile);
    }

    public static boolean extract(InputStream srcInput, File dstFile) {
        boolean ret = false;

        FileOutputStream output = null;
        try {
            GZIPInputStream input = new GZIPInputStream(srcInput);
            output = new FileOutputStream(dstFile);

            byte[] buf = new byte[1024];
            while (true) {
                int bytesRead = input.read(buf);
                if (bytesRead == -1) {
                    break;
                }

                output.write(buf, 0, bytesRead);
            }

            ret = true;
        }
        catch (IOException e) {
            Log.e(TAG, "extract gzip fail, " + e.getMessage());
        }
        finally {
            if (output != null) {
                try {
                    output.close();
                }
                catch (IOException e) {
                    //ignore
                }
            }
        }

        return ret;
    }
}
