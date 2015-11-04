package com.thunsaker.redacto;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PreviewActivity extends AppCompatActivity {
    private String LOG_TAG = "PreviewActivity";

    @Inject Picasso mPicasso;

    @Bind(R.id.toolbarPreview) Toolbar mToolbar;
    @Bind(R.id.imageViewPreviewRedaction) ImageView mPreviewImage;
    @Bind(R.id.linearLayoutPreviewRedactionWrapper) LinearLayout mPreviewImageWrapper;
    @Bind(R.id.colorPickerPreview) LobsterShadeSlider mColorPicker;
    @Bind(R.id.scrollViewPreview) ScrollView mScrollViewWrapper;

    public static String EXTRA_CROPPED_IMAGE = "EXTRA_CROPPED_IMAGE";

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
    }

    private void attemptImageOCR(Bitmap bitmap) {
        // TODO: Init the OCR
        File tessdataDirectory = TesseractUtils.getTessdataDirectory();
        if(tessdataDirectory.listFiles(
                new TesseractUtils.TessdataFileFilter()).length != 1) {
            TesseractUtils.copyTessdataToDevice(getApplicationContext());
            tessdataDirectory = TesseractUtils.getTessdataDirectory();
        }

        // TODO: Start the OCR here
        if(tessdataDirectory.listFiles(
                new TesseractUtils.TessdataFileFilter()).length > 0) {
            Loki loki = new Loki(TesseractUtils.getTessdataParentDirectory());

            try {
                TesseractResult result =
                        loki.getResultsFromOCR(bitmap);

                if (result != null) {
                    Log.i(LOG_TAG, "OCR'd Text: " + result.text);
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
}
