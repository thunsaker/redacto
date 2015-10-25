package com.thunsaker.redacto;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.thunsaker.redacto.models.Redaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.recyclerRedactions) RecyclerView mRecyclerView;

    private RecyclerView.Adapter mRecyclerViewAdapter;
//    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager mLayoutManager;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
//        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

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

        mContext = getApplicationContext();
        mRecyclerViewAdapter = new RedactionsAdaptor(mContext, mRedactionsList);
        mRecyclerViewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
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
