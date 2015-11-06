package com.thunsaker.redacto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.googlecode.leptonica.android.Pixa;
import com.larswerkman.lobsterpicker.OnColorListener;
import com.larswerkman.lobsterpicker.adapters.BitmapColorAdapter;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.thunsaker.redacto.app.RedactoApp;
import com.thunsaker.redacto.ocr.Loki;
import com.thunsaker.redacto.ocr.TesseractResult;
import com.thunsaker.redacto.ocr.TesseractUtils;
import com.thunsaker.redacto.util.DeviceDimensions;
import com.thunsaker.redacto.util.StorageUtils;
import com.thunsaker.redacto.widget.HighlightableImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PreviewActivity extends AppCompatActivity implements  HighlightableImageView.OnTouchListener {
    private String LOG_TAG = "PreviewActivity";

    @Inject Picasso mPicasso;

    @Bind(R.id.toolbarPreview) Toolbar mToolbar;
    @Bind(R.id.imageViewPreviewRedaction) HighlightableImageView mPreviewImage;
    @Bind(R.id.linearLayoutPreviewRedactionWrapper) LinearLayout mPreviewImageWrapper;
    @Bind(R.id.colorPickerPreview) LobsterShadeSlider mColorPicker;
    @Bind(R.id.scrollViewPreview) ScrollView mScrollViewWrapper;

    public static String EXTRA_CROPPED_IMAGE = "EXTRA_CROPPED_IMAGE";

    public String mText = "";
    public Pixa mTextLines;
    public Pixa mTextWords;
    public ArrayList<Rect> mTextLinesBoxRects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        RedactoApp.getComponent(this).inject(this);

        File cacheFile = getCacheDir();
        if (cacheFile != null && cacheFile.listFiles().length > 0) {
            Log.i(LOG_TAG, "Total Mem: " + Runtime.getRuntime().totalMemory());
            Log.i(LOG_TAG, "Free Mem: " + Runtime.getRuntime().freeMemory());
            File[] cacheFiles = cacheFile.listFiles(new StorageUtils.CacheFileFilter());

            int deviceWidth = DeviceDimensions.getDisplayWidth(getApplicationContext());
            int deviceHeight = DeviceDimensions.getDisplayHeight(getApplicationContext());

            // TODO: Consider putting a spinner here while the OCR does it's thing. I'm goingt o move the OCR into the background so maybe I don't need to do anything else here.
            mPicasso.load("file:" + cacheFiles[0])
                    .resize(deviceWidth, deviceHeight)
                    .centerInside()
//                    .transform(new MaskTransformation(
//                            getApplicationContext(), R.drawable.crop_mask_slant))
//                    .placeholder(R.drawable.redacto_placeholder_sm_light)
                    .into(mTarget);
        }

        mColorPicker.getColor();
        mColorPicker.setColorAdapter(
                new BitmapColorAdapter(this, R.drawable.redacto_palette_picker));
        mColorPicker.setShadePosition(5);
        mColorPicker.addOnColorListener(new OnColorListener() {
            @Override
            public void onColorChanged(int color) {
                mPreviewImageWrapper.setBackgroundColor(color);
                mScrollViewWrapper.setBackgroundColor(color);
            }

            @Override
            public void onColorSelected(int color) {
                mPreviewImageWrapper.setBackgroundColor(color);
                mScrollViewWrapper.setBackgroundColor(
                        getResources().getColor(R.color.gray_light));
            }
        });

        mPreviewImage.setOnTouchListener(this);
    }

    // TODO: Make this an async task so the user can continue selecting color, then highlight afterwards
    private void attemptImageOCR(Bitmap bitmap) {
        File tessdataDirectory = TesseractUtils.getTessdataDirectory();
        if(tessdataDirectory.listFiles(
                new TesseractUtils.TessdataFileFilter()).length != 1) {
            TesseractUtils.copyTessdataToDevice(getApplicationContext());
            tessdataDirectory = TesseractUtils.getTessdataDirectory();
        }

        if(tessdataDirectory.listFiles(
                new TesseractUtils.TessdataFileFilter()).length > 0) {
            Loki loki = new Loki(TesseractUtils.getTessdataParentDirectory());

            try {
                TesseractResult result =
                        loki.getResultsFromOCR(bitmap);

                if (result != null) {
                    Log.i(LOG_TAG, "OCR'd Text: " + result.text);
                    mText = result.text;
                    mTextLines = result.lines;
                    mTextWords = result.words;

                    Canvas tempCanvas = new Canvas(bitmap);
                    tempCanvas.drawBitmap(bitmap, 0, 0, null);

                    // Paint Redaction Outlines
                    Paint blackPaint = new Paint();

                    int offset = 20;
                    blackPaint.setColor(getResources().getColor(R.color.black_transparent));
                    blackPaint.setStyle(Paint.Style.STROKE);
                    for (Rect r : mTextWords.getBoxRects()) {
                        r.offsetTo(r.left - offset, r.top - offset);
                        r.right = r.right + (offset*2);
                        r.bottom = r.bottom + (offset*2);
                        tempCanvas.drawRect(r, blackPaint);
                    }

                    mPreviewImage.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
                }
            } catch (Exception e) {
                e.printStackTrace();

                Snackbar.make(mScrollViewWrapper,
                        "There was an error scanning the image for text",
                        Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(mScrollViewWrapper,
                    "There was an error initializing the tesseract, " +
                            "Contact S.H.I.E.L.D. immediately!",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return true;
    }

    private Target mTarget = new Target() {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mPreviewImage.setImageBitmap(bitmap);
            attemptImageOCR(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Snackbar.make(mScrollViewWrapper,
                    "There was an error scanning the image for text",
                    Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if(mTextLines != null)
            mTextLines.recycle();
        if(mTextWords != null)
            mTextWords.recycle();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        List<Rect> rects = new ArrayList<>();
        rects.add(new Rect((int) x, (int) y, (int) (x + 50), (int) (y + 100)));

        mPreviewImage.AddHighlight(rects,
                getResources().getColor(R.color.black_transparent));
        mPreviewImage.postInvalidate();
        return true;
    }
}