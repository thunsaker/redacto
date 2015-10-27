package com.thunsaker.redacto;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.isseiaoki.simplecropview.CropImageView;
import com.squareup.picasso.Picasso;
import com.thunsaker.redacto.app.RedactoApp;

import javax.inject.Inject;

public class CropActivity extends AppCompatActivity {

    public static String EXTRA_SCREENSHOT_PATH = "EXTRA_SCREENSHOT_PATH";
    @Inject Picasso mPicasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
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
}
