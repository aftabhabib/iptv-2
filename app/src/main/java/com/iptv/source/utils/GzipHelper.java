package com.iptv.source.utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

public class GzipHelper {
    private static final String TAG = "GzipHelper";

    public static boolean extract(File srcFile, File dstFile) {
        boolean ret = false;

        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(srcFile);
            output = new FileOutputStream(dstFile);

            extract(input, output);
            ret = true;
        }
        catch (IOException e) {
            Log.e(TAG, "extract fail, " + e.getMessage());
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

    public static boolean extract(byte[] data, File file) {
        return extract(new ByteArrayInputStream(data), file);
    }

    public static boolean extract(InputStream input, File file) {
        boolean ret = false;

        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);

            extract(input, output);
            ret = true;
        }
        catch (IOException e) {
            Log.e(TAG, "extract fail, " + e.getMessage());
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

    public static boolean extract(byte[] data, OutputStream output) {
        try {
            extract(new ByteArrayInputStream(data), output);
        }
        catch (IOException e) {
            Log.e(TAG, "extract fail, " + e.getMessage());
            return false;
        }

        return true;
    }

    private static void extract(InputStream input, OutputStream output) throws IOException {
        GZIPInputStream gzInput = new GZIPInputStream(input);

        byte[] buf = new byte[1024];
        while (true) {
            int bytesRead = gzInput.read(buf);
            if (bytesRead == -1) {
                break;
            }

            output.write(buf, 0, bytesRead);
        }
    }
}
