package com.thunsaker.redacto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.isseiaoki.simplecropview.CropImageView;
import com.squareup.picasso.Picasso;
import com.thunsaker.redacto.app.RedactoApp;

import javax.inject.Inject;

public class CropActivity extends AppCompatActivity {

    @Inject Picasso mPicasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RedactoApp.getComponent(this).inject(this);

        CropImageView cropImageView = (CropImageView) findViewById(R.id.cropImageView);

        mPicasso.load(R.drawable.debug_screen)
//                .resizeDimen(R.dimen.redacto_preview_width, R.dimen.redacto_preview_height)
//                .resize(400,500)
//                .centerInside()
                .resize(1000,1000)
                .centerInside()
                .placeholder(R.drawable.redacto_placeholder)
                .into(cropImageView);
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
