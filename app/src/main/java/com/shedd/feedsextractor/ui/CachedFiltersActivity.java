package com.shedd.feedsextractor.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.shedd.feedsextractor.R;
import com.shedd.feedsextractor.adapter.FiltersRecyclerAdapter;
import com.shedd.feedsextractor.data.local.JsonProvider;
import com.shedd.feedsextractor.data.local.LocalExtra;
import com.shedd.feedsextractor.data.model.cache.Filter;
import com.shedd.feedsextractor.data.model.cache.Filters;

import java.util.ArrayList;

public class CachedFiltersActivity extends AppCompatActivity {

    private Filters cachedFilters;
    private ArrayList<Filter> allFilters;
    private FiltersRecyclerAdapter adapter;
    private RecyclerView filtersList;
    private TextView noFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cached_filters);
        initToolbar();
        initScreen();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initScreen() {
        noFilters = (TextView) findViewById(R.id.filters_empty);
        filtersList = (RecyclerView) findViewById(R.id.filters_list);
        allFilters = new ArrayList<>();
        adapter = new FiltersRecyclerAdapter(CachedFiltersActivity.this, allFilters);
        filtersList.setHasFixedSize(true);
        filtersList.setLayoutManager(new LinearLayoutManager(CachedFiltersActivity.this));
        filtersList.setAdapter(adapter);
        manageListListeners();
        setList();
    }

    private void manageListListeners() {
        adapter.setActionPerformedListener(new FiltersRecyclerAdapter.ActionPerformedListener() {
            @Override
            public void OnFilterClicked(int position) {
                Intent i = new Intent(CachedFiltersActivity.this, FilterActivity.class);
                i.putExtra(LocalExtra.EXTRA_FILTER, allFilters.get(position));
                startActivity(i);
            }
        });
        adapter.setLongClickPerformedListener(new FiltersRecyclerAdapter.LongClickPerformedListener() {
            @Override
            public void OnRemoveFilter(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CachedFiltersActivity.this, R.style.AppTheme_AlertDialogStyle);
                builder.setMessage(getString(R.string.dialog_remove_filter));
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        allFilters.remove(position);
                        adapter.notifyDataSetChanged();
                        if (allFilters.isEmpty()) {
                            filtersList.setVisibility(View.GONE);
                            noFilters.setVisibility(View.VISIBLE);
                        }
                        cachedFilters.getFilters().remove(position);
                        try {
                            JsonProvider.storeObject(CachedFiltersActivity.this, cachedFilters, LocalExtra.CACHED_FILTERS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.show();
            }
        });
    }

    private void setList() {
        try {
            cachedFilters = JsonProvider.getCachedObject(CachedFiltersActivity.this, Filters.class, LocalExtra.CACHED_FILTERS);
            if (!cachedFilters.getFilters().isEmpty()) {
                allFilters.clear();
                allFilters.addAll(cachedFilters.getFilters());
                adapter.notifyDataSetChanged();
                filtersList.setVisibility(View.VISIBLE);
                noFilters.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
