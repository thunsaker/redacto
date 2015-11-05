package com.thunsaker.redacto;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Highlight {
    private List<Rect> mRects;
    private int mColor;

    public Highlight() {
        this(new ArrayList<Rect>(), R.color.redacto_green_transparent);
    }

    public Highlight(List<Rect> rects, int color) {
        mRects = rects;
        mColor = color;
    }

    public List<Rect> getRects() {
        return mRects;
    }

    public void setRects(List<Rect> rects) {
        mRects = rects;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }
}