package com.shedd.feedsextractor.ui;

/**
 * Created by Admin on 4/11/2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.shedd.feedsextractor.R;
import com.shedd.feedsextractor.adapter.TweetsRecyclerAdapter;
import com.shedd.feedsextractor.data.api.task.GetTweetsTask;
import com.shedd.feedsextractor.data.local.JsonProvider;
import com.shedd.feedsextractor.data.local.LocalExtra;
import com.shedd.feedsextractor.data.model.Tweet;
import com.shedd.feedsextractor.data.model.cache.Filter;
import com.shedd.feedsextractor.data.model.cache.Filters;
import com.shedd.feedsextractor.listener.OnTaskCompletedListener;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout container;
    private RecyclerView tweetsList;
    private TweetsRecyclerAdapter adapter;
    private ArrayList<Tweet> mTweets = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initScreen();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(LocalExtra.TWITTER_NAME + " tweets");
    }

    private void initScreen() {
        container = (CoordinatorLayout) findViewById(R.id.container);
        tweetsList = (RecyclerView) findViewById(R.id.tweets_list);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String, String> params = new HashMap<>(1);
        params.put("screen_name", LocalExtra.TWITTER_NAME);
        new GetTweetsTask(MainActivity.this, params, new OnTaskCompletedListener() {
            @Override
            public void onError(String tag, String message) {
                Snackbar.make(container, message, Snackbar.LENGTH_LONG)
                        .show();
                progressDialog.dismiss();
            }

            @Override
            public void onEmptyResponse(String tag, String message) {
                Snackbar.make(container, message, Snackbar.LENGTH_LONG)
                        .show();
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(String tag, ArrayList<Tweet> response) {
                populateListView(response);
                progressDialog.dismiss();
            }
        });
    }

    private void populateListView(ArrayList<Tweet> tweets) {
        mTweets.clear();
        mTweets.addAll(tweets);
        adapter = new TweetsRecyclerAdapter(MainActivity.this, mTweets);
        tweetsList.setHasFixedSize(true);
        tweetsList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        tweetsList.setAdapter(adapter);
        adapter.setActionPerformedListener(new TweetsRecyclerAdapter.ActionPerformedListener() {
            @Override
            public void OnTweetClicked(String url) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter.getItems() != null && !adapter.getItems().isEmpty()) {
                    try {
                        Filters cachedFilters = JsonProvider.getCachedObject(MainActivity.this, Filters.class, LocalExtra.CACHED_FILTERS);
                        Filter filter = new Filter();
                        filter.setName(query);
                        filter.setTweets(adapter.getItems());
                        cachedFilters.getFilters().add(filter);
                        JsonProvider.storeObject(MainActivity.this, cachedFilters, LocalExtra.CACHED_FILTERS);
                    } catch (Exception e) {
                        Filters filters = new Filters();
                        Filter filter = new Filter();
                        filter.setName(query);
                        filter.setTweets(adapter.getItems());
                        ArrayList<Filter> filtersArray = new ArrayList<>();
                        filtersArray.add(filter);
                        filters.setFilters(filtersArray);
                        try {
                            JsonProvider.storeObject(MainActivity.this, filters, LocalExtra.CACHED_FILTERS);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                    Snackbar.make(container, getString(R.string.snack_filter_added), Snackbar.LENGTH_LONG)
                            .show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        ArrayList<Tweet> temp = new ArrayList();
        for (Tweet d : mTweets)
            if (d.getMessage().toLowerCase().contains(text.toLowerCase()))
                temp.add(d);
        adapter.updateList(temp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, CachedFiltersActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
