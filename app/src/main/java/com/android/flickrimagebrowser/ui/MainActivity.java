package com.android.flickrimagebrowser.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.flickrimagebrowser.DownloadStatus;
import com.android.flickrimagebrowser.R;
import com.android.flickrimagebrowser.data.ParseFlickrJsonData;
import com.android.flickrimagebrowser.data.Photo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends BaseActivity
        implements ParseFlickrJsonData.OnDataAvailable, RecyclerItemClickListener.OnClick {
    private static final String TAG = "MainActivity";
    private String baseURL = "https://www.flickr.com/services/feeds/photos_public.gne";
    private String tags = randomTags();

    private PhotoRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().clear().apply();
        setContentView(R.layout.activity_main);
        activateToolbar(false);

        //Swipe to refresh

        SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                        updateContent();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        mAdapter = new PhotoRecyclerAdapter(new ArrayList<Photo>(), this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.searchBtn) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String searchQuery = sharedPreferences.getString(FLICKR_QUERY, "");
        ParseFlickrJsonData parser = new ParseFlickrJsonData(this, baseURL, false);

        if (!(searchQuery.length() > 0)) {
            parser.execute(randomTags());
        } else {
            tags = parseSearchQuery(searchQuery);
            parser.execute(tags);
            Log.d(TAG, "onResume: Search Tags - " + tags);
        }

    }

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus downloadStatus) {
        if(downloadStatus == DownloadStatus.OK) {
            mAdapter.loadNewData(data);
        } else {
            Log.e(TAG, "onDataAvailable: Error in getting data, Status : " + downloadStatus);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: Item Clicked");
        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, mAdapter.getPhoto(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: Item Long Clicked");
        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, mAdapter.getPhoto(position));
        startActivity(intent);
    }

    private String parseSearchQuery(String searchQuery) {
        StringBuilder tags = new StringBuilder();
        if (!searchQuery.contains(",")) {
            String[] arr = searchQuery.trim().toLowerCase().split(" ");
            for (String tag : arr) {
                tags.append(tag).append(",");
            }
            return tags.substring(0, tags.toString().length() - 1);
        }

        return searchQuery.toLowerCase().trim();
    }

    private String randomTags() {
        String[] arr = {"all", "actors", "bitcoin", "cars", "party", "sports", "festival", "gaming", "social", "space"};
        return arr[(new Random()).nextInt(arr.length)];
    }

    private void updateContent() {
        ParseFlickrJsonData parser = new ParseFlickrJsonData(this, baseURL, false);
        parser.execute(tags);
        Log.d(TAG, "updateContent: " + tags);
    }
}