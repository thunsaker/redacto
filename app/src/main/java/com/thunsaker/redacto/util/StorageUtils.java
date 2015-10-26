package com.thunsaker.redacto.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

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

    public static File[] sortFilesByDateDescending(File[] files) {
        if(files != null && files.length > 1) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    return (int) ((file1.lastModified() > file2.lastModified())
                            ? file1.lastModified()
                            : file2.lastModified());
                }
            });
        }
        return files;
    }
}
