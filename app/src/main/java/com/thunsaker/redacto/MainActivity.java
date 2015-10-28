package com.thunsaker.redacto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.thunsaker.redacto.app.RedactoApp;
import com.thunsaker.redacto.models.Redaction;
import com.thunsaker.redacto.util.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.recyclerRedactions) RecyclerView mRecyclerView;

    private RedactionsAdaptor mRecyclerViewAdapter;
    private GridLayoutManager mLayoutManager;
    private String LOG_TAG = "MainActivity";

    public List<Redaction> mRedactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RedactoApp.getComponent(this).inject(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//                startActivity(new Intent(getApplicationContext(), CropActivity.class));
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        refreshScreenshots();

        final ItemClickSupport itemClick = ItemClickSupport.addTo(mRecyclerView);

        itemClick.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Redaction redaction = mRedactions.get(position);
                Intent screenshotIntent =
                        new Intent(getApplicationContext(), CropActivity.class);
                screenshotIntent.putExtra(
                        CropActivity.EXTRA_SCREENSHOT_PATH, redaction.ImageFile.getPath());
                startActivity(screenshotIntent);
            }
        });
    }

    private void refreshScreenshots() {
        List<Redaction> mRedactionsList = getScreenshots();
        mRedactions = mRedactionsList;

        mRecyclerViewAdapter =
                new RedactionsAdaptor(getApplicationContext(), mRedactionsList);
        mRecyclerViewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        this.setProgressBarVisibility(false);
    }

    private List<Redaction> getScreenshots() {
        List<Redaction> mRedactionsList = new ArrayList<>();
        if(StorageUtils.isExternalStorageReadable()) {
            File file = StorageUtils.getFileDirectory(StorageUtils.DIRECTORY_SCREENSHOTS);
            if(file != null) {
                File[] files = file.listFiles();
                Arrays.sort(files, Collections.reverseOrder());
                for (File f : files) {
                    Redaction r = new Redaction();
                    r.ImageFile = f;
                    Date date = new Date();
                    date.setTime(f.lastModified());
                    r.DateCreated = date;
                    mRedactionsList.add(r);
                }
            }
        }

        return mRedactionsList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerViewAdapter.clear();
        this.setProgressBarVisibility(true);
        refreshScreenshots();
    }
}