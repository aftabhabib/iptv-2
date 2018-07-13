package com.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class GzipHelper {
    private static final String TAG = "GzipHelper";

    public static boolean extract(File srcFile, File dstFile) {
        boolean ret = false;

        GZIPInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new GZIPInputStream(new FileInputStream(srcFile));
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

    public static boolean extract(InputStream srcInput, File dstFile) {
        boolean ret = false;

        GZIPInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new GZIPInputStream(srcInput);
            output = new FileOutputStream(dstFile);

            byte[] buf = new byte[1024];
            while (true) {
                int bytesRead = input.read(buf);
                if (bytesRead == -1) {
                    break;
                }

                output.write(buf, 0, bytesRead);
            }

            input.close();
            output.close();

            ret = true;
        }
        catch (IOException e) {
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
