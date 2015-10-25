package com.thunsaker.redacto.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class StorageUtils {
    private static String LOG_TAG = "StorageUtils";

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static File getScreenshotDirectory() {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "Screenshots");

        if(!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }

        return file;
    }
}
