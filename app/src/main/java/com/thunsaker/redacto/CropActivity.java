package com.thunsaker.redacto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.isseiaoki.simplecropview.CropImageView;
import com.squareup.picasso.Picasso;
import com.thunsaker.redacto.app.RedactoApp;
import com.thunsaker.redacto.util.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CropActivity extends AppCompatActivity {

    private static final String LOG_TAG = "CropActivity";
    @Inject Picasso mPicasso;

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.cropImageView) CropImageView mCropImageView;
    @Bind(R.id.imageButtonCropFree) ImageButton mButtonCropFree;
    @Bind(R.id.imageButtonCropSquare) ImageButton mButtonCropSquare;
//    @Bind(R.id.imageButtonCropPhone) ImageButton mButtonCropPhone;

    public static String EXTRA_SCREENSHOT_PATH = "EXTRA_SCREENSHOT_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        RedactoApp.getComponent(this).inject(this);

        CropImageView cropImageView = (CropImageView) findViewById(R.id.cropImageView);

        if(getIntent().hasExtra(EXTRA_SCREENSHOT_PATH)) {
            mPicasso.load("file:" + getIntent().getStringExtra(EXTRA_SCREENSHOT_PATH))
                    .resize(1000, 1000)
                    .centerInside()
                    .placeholder(R.drawable.redacto_placeholder)
                    .into(cropImageView);
        } else {
            Snackbar.make(mToolbar, "No image selected! :(", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_crop_crop) {
            // Do crop the image
            if(StorageUtils.isExternalStorageWritable()) {
                File cacheFile = getCacheDir();
                try {
                    Log.i(LOG_TAG, "Total Mem: " + Runtime.getRuntime().totalMemory());
                    Log.i(LOG_TAG, "Free Mem: " + Runtime.getRuntime().freeMemory());
                    File imageFile = new File(cacheFile, StorageUtils.CACHE_IMAGE_FILE);
                    FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                    if(mCropImageView.getCroppedBitmap()
                            .compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream)) {
                        Log.i(LOG_TAG, "Everything went well");
                        Log.i(LOG_TAG, "Total Mem: " + Runtime.getRuntime().totalMemory());
                        Log.i(LOG_TAG, "Free Mem: " + Runtime.getRuntime().freeMemory());
                        startActivity(new Intent(getApplicationContext(), PreviewActivity.class));
                    } else {
                        Snackbar.make(mCropImageView, "Image did not save :(", Snackbar.LENGTH_LONG)
                                .show();
                    }
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return true;
        } else if(id == R.id.action_crop_cancel) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.imageButtonCropSquare)
    public void CropSquareClick() {
        mCropImageView.setCropMode(CropImageView.CropMode.RATIO_1_1);
        mCropImageView.setInitialFrameScale(1);
    }

    @OnClick(R.id.imageButtonCropFree)
    public void CropFreeClick() {
        mCropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
        mCropImageView.setInitialFrameScale(1);
    }

//    @OnClick(R.id.imageButtonCropPhone)
//    public void CropPhoneClick() {
//        mCropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
//        mCropImageView.setInitialFrameScale(1);
//    }
}