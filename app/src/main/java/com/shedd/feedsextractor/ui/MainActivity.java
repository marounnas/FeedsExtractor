package com.shedd.feedsextractor.ui;

/**
 * Created by Admin on 4/11/2017.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.shedd.feedsextractor.R;
import com.shedd.feedsextractor.adapter.TweetsRecyclerAdapter;
import com.shedd.feedsextractor.data.api.task.GetTweetsTask;
import com.shedd.feedsextractor.data.model.Tweet;
import com.shedd.feedsextractor.listener.OnTaskCompletedListener;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private final String SEARCH_TWITTER = "Android";
    private CoordinatorLayout container;
    private RecyclerView tweetsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        container = (CoordinatorLayout) findViewById(R.id.container);
        tweetsList = (RecyclerView) findViewById(R.id.tweets_list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(SEARCH_TWITTER + " tweets");
        initScreen();
    }

    private void initScreen() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String, String> params = new HashMap<>(1);
        params.put("screen_name", SEARCH_TWITTER);
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
        TweetsRecyclerAdapter adapter = new TweetsRecyclerAdapter(MainActivity.this, tweets);
        tweetsList.setHasFixedSize(true);
        tweetsList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        tweetsList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                break;
            case R.id.action_settings:

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
