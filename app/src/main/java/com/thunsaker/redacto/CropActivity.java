package com.thunsaker.redacto;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.isseiaoki.simplecropview.CropImageView;
import com.squareup.picasso.Picasso;
import com.thunsaker.redacto.app.RedactoApp;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CropActivity extends AppCompatActivity {

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RedactoApp.getComponent(this).inject(this);

        CropImageView cropImageView = (CropImageView) findViewById(R.id.cropImageView);

        if(getIntent().hasExtra(EXTRA_SCREENSHOT_PATH)) {
            mPicasso.load("file:" + getIntent().getStringExtra(EXTRA_SCREENSHOT_PATH))
                    .resize(1000, 1000)
                    .centerInside()
                    .placeholder(R.drawable.redacto_placeholder)
                    .into(cropImageView);
        } else {
            Snackbar.make(toolbar, "No image selected! :(", Snackbar.LENGTH_LONG)
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
            return true;
        } else if(id == R.id.action_crop_cancel) {
            // Do nothing
            return true;
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