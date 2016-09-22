package com.rica.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rica.popularmovies.sync.MovieSyncAdapter;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private final String MAINFRAGMENTTAG = "MFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.framelayout, new MainActivityFragment(),MAINFRAGMENTTAG).commit();
        MovieSyncAdapter.initializeAdapter(getApplicationContext());
//        MovieSyncAdapter.syncImmediately(getApplicationContext());
    }

    @Override
    public void onItemClicked(Uri movieID) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.setData(movieID);
        startActivity(intent);
    }
}
