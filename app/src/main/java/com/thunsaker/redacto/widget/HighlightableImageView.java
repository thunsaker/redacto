package com.thunsaker.redacto.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.thunsaker.redacto.Highlight;
import com.thunsaker.redacto.R;

import java.util.ArrayList;
import java.util.List;

public class HighlightableImageView
        extends ImageView
        implements View.OnTouchListener {

    private final int blackPaintColor = R.color.black_transparent;
    private Paint blackPaint = new Paint(blackPaintColor);
    private Paint paintColor = new Paint();

    private List<Highlight> mHighlights;
    private String LOG_TAG = "HighlightableImageView";

    public HighlightableImageView(Context context) {
        super(context);
    }

    public HighlightableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHighlights = new ArrayList<>();
    }

    // TODO: Change this to draw rects to fill in the "words" that Tesseract sees
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Highlight h : mHighlights) {
            paintColor.setColor(h.getColor());
            for (Rect r : h.getRects()) {
                // Drawing "Lines" right now with rects
                canvas.drawRect(r, paintColor);
            }
        }
    }

    // TODO: Add Touchable elements for the highlights so that I can remove them later...
    public void AddHighlight(List<Rect> rects, int color) {
        mHighlights.add(new Highlight(rects, color));
        postInvalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
