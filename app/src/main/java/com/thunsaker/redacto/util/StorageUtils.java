package com.thunsaker.redacto.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;

public class StorageUtils {
    private static String LOG_TAG = "StorageUtils";

    public static String DIRECTORY_SCREENSHOTS = "Screenshots";
    public static String DIRECTORY_REDACTIONS = "Redacto";

    public static final String CACHE_IMAGE_FILE = "redacto-cache-preview.jpg";

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static File getFileDirectory(String name) {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                name);

        // Try the pictures directory (this might be platform/manufacture specific)
        if(file.listFiles() == null) {
            file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    name);
        }

        if(!file.exists()) {
            Log.e(LOG_TAG, "Directory does not exist");
        }

        return file;
    }

    public static class CacheFileFilter implements FilenameFilter {
        @Override
        public boolean accept(File file, String fileName) {
            return fileName.equals(CACHE_IMAGE_FILE);
        }
    }
}