package com.thunsaker.redacto.ocr;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.thunsaker.redacto.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TesseractUtils {
    public static String LOG_TAG = "TesseractUtils";

    private static String TESS_DATA_DIR = "tessdata";
    private static String TESS_DATA_FILE_ENG = "eng.traineddata";
    private static String TESS_DATA_FILE = "traineddata";

    public static File getTessdataDirectory() {
        File file = new File(getTessdataParentDirectory() + TESS_DATA_DIR);
        if (!file.exists()) {
            Log.i(LOG_TAG, "Directory does not exist");
            if (!file.mkdir()) {
                // TODO: There was a problem creating the directory
                Log.i(LOG_TAG, "Error Creating directory");
            }
        }
        Log.i(LOG_TAG, "Directory created or already exists");
        return file;
    }

    public static boolean copyTessdataToDevice(Context context) {
        File file = new File(getTessdataDirectory(), TESS_DATA_FILE_ENG);
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = context.getResources().openRawResource(R.raw.eng);
            outputStream = new FileOutputStream(file);
            copyFile(inputStream, outputStream);
            return true;
        } catch (IOException e) {
            Log.e(LOG_TAG, "FAILURE!!!");
            e.printStackTrace();
            return true;
        } finally {
            try {
                if(inputStream != null)
                    inputStream.close();
                if(outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void copyFile(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
        input.close();
        output.close();
    }

    public static String getTessdataParentDirectory() {
        return Environment.getExternalStorageDirectory().getPath() + "/";
    }

    public static class TessdataFileFilter implements FilenameFilter {
        @Override
        public boolean accept(File file, String fileName) {
            // TODO: In the future allow filtering by language
            return fileName.endsWith(TESS_DATA_FILE);
        }
    }
}