package com.thunsaker.redacto.ocr;

import com.googlecode.leptonica.android.Pixa;

public class TesseractResult {
    // All of the text
    public String text;

    // Large Text Regions
    public Pixa regions;

    // Lines of Text
    public Pixa lines;

    // Individual Words
    public Pixa words;
}
