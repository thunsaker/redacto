package com.thunsaker.redacto;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.thunsaker.redacto.app.RedactoApp;
import com.thunsaker.redacto.models.Redaction;
import com.thunsaker.redacto.util.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.recyclerRedactions) RecyclerView mRecyclerView;

    private RecyclerView.Adapter mRecyclerViewAdapter;
    private GridLayoutManager mLayoutManager;
    private String LOG_TAG = "MainActivity";

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
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Redaction> mRedactionsList = getScreenshots();

        mRecyclerViewAdapter =
                new RedactionsAdaptor(getApplicationContext(), mRedactionsList);
        mRecyclerViewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    private ArrayList<Redaction> getScreenshots() {
        ArrayList<Redaction> mRedactionsList = new ArrayList<>();
        if(StorageUtils.isExternalStorageReadable()) {
            File file = StorageUtils.getScreenshotDirectory();
            if(file != null) {
                for (File f : StorageUtils.sortFilesByDateDescending(file.listFiles())) {
                    Redaction r = new Redaction();
                    r.ImageFile = f;
                    Date date = new Date();
                    date.setTime(f.lastModified());
                    r.DateCreated = date;
                    r.SourceUrl = "https://theverge.com";
                    Log.i(LOG_TAG, "Redaction date " + date.toString());
                    mRedactionsList.add(r);
                }
            }
        }

        return mRedactionsList;
    }

    private ArrayList<Redaction> getScreenshotsDebug() {
        ArrayList<Redaction> mRedactionsList = new ArrayList<>();
        Redaction testRedaction;

        for(int i = 0; i < 7; i++) {
            testRedaction = new Redaction();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, -1 * (i * 86400));
            testRedaction.DateCreated = calendar.getTime();
            testRedaction.SourceUrl =
                    i % 3 == 0
                            ? "https://medium.com"
                            : "https://theverge.com";
            mRedactionsList.add(testRedaction);
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
}
