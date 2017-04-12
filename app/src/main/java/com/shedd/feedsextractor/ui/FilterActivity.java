package com.shedd.feedsextractor.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.shedd.feedsextractor.R;
import com.shedd.feedsextractor.adapter.TweetsRecyclerAdapter;
import com.shedd.feedsextractor.data.local.LocalExtra;
import com.shedd.feedsextractor.data.model.cache.Filter;

public class FilterActivity extends AppCompatActivity {

    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        initToolbar();
        initScreen();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        filter = getIntent().getExtras().getParcelable(LocalExtra.EXTRA_FILTER);
        getSupportActionBar().setTitle(filter.getName());
    }

    private void initScreen() {
        RecyclerView filterTweetsList = (RecyclerView) findViewById(R.id.filter_tweets_list);
        TweetsRecyclerAdapter adapter = new TweetsRecyclerAdapter(FilterActivity.this, filter.getTweets());
        filterTweetsList.setHasFixedSize(true);
        filterTweetsList.setLayoutManager(new LinearLayoutManager(FilterActivity.this));
        filterTweetsList.setAdapter(adapter);
        adapter.setActionPerformedListener(new TweetsRecyclerAdapter.ActionPerformedListener() {
            @Override
            public void OnTweetClicked(String url) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + url));
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
