package com.thunsaker.redacto.ocr;

import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

public class Loki {
    public static final String LANG_ENG = "eng";
    private TessBaseAPI mTessBaseApi;

    public Loki(String datapath) {
        this(datapath, LANG_ENG);
    }

    public Loki(String datapath, String lang) {
        mTessBaseApi = new TessBaseAPI();
        mTessBaseApi.init(datapath, lang);
    }

    public TesseractResult getResultsFromOCR(Bitmap bitmap) {
        if (mTessBaseApi != null) {
            mTessBaseApi.setImage(bitmap);

            TesseractResult tessResult = new TesseractResult();
            tessResult.text = mTessBaseApi.getUTF8Text();
            tessResult.regions = mTessBaseApi.getRegions();
            tessResult.lines = mTessBaseApi.getTextlines();
            tessResult.words = mTessBaseApi.getWords();
            mTessBaseApi.end();

            return tessResult;
        } else {
            return null;
        }
    }

    public void Destroy() {
        if(mTessBaseApi != null)
            mTessBaseApi.end();
    }
}
