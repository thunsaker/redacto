package com.thunsaker.redacto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.larswerkman.lobsterpicker.OnColorListener;
import com.larswerkman.lobsterpicker.adapters.BitmapColorAdapter;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;
import com.squareup.picasso.Picasso;
import com.thunsaker.redacto.app.RedactoApp;
import com.thunsaker.redacto.util.StorageUtils;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PreviewActivity extends AppCompatActivity {
    private String LOG_TAG = "PreviewActivity";
    public static String EXTRA_CROPPED_IMAGE = "EXTRA_CROPPED_IMAGE";

    @Inject Picasso mPicasso;

    @Bind(R.id.imageViewPreviewRedaction) ImageView mPreviewImage;
    @Bind(R.id.colorPickerPreview) LobsterShadeSlider mColorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);
        RedactoApp.getComponent(this).inject(this);

        File cacheFile = getCacheDir();
        if(cacheFile != null && cacheFile.listFiles().length > 0) {
            Log.i(LOG_TAG, "Total Mem: " + Runtime.getRuntime().totalMemory());
            Log.i(LOG_TAG, "Free Mem: " + Runtime.getRuntime().freeMemory());
            File[] cacheFiles = cacheFile.listFiles(new StorageUtils.CacheFileFilter());
            mPicasso.load("file:" + cacheFiles[0])
                    .resize(1760,880)
                    .centerInside()
                    .placeholder(R.drawable.redacto_placeholder_sm_light)
                    .into(mPreviewImage);
        }

        mColorPicker.getColor();
        mColorPicker.setColorAdapter(new BitmapColorAdapter(this, R.drawable.redacto_palette_picker));
        mColorPicker.setShadePosition(5);
        mColorPicker.addOnColorListener(new OnColorListener() {
            @Override
            public void onColorChanged(int color) {
                mPreviewImage.setBackgroundColor(color);
            }

            @Override
            public void onColorSelected(int color) {
                mPreviewImage.setBackgroundColor(color);
            }
        });
    }
}
