package com.rica.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rica.popularmovies.sync.MovieSyncAdapter;

public class MainActivity extends AppCompatActivity {

    private final String MAINFRAGMENTTAG = "MFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, new MainActivityFragment(),MAINFRAGMENTTAG).commit();
        MovieSyncAdapter.initializeAdapter(getApplicationContext());
//        MovieSyncAdapter.syncImmediately(getApplicationContext());
    }
}
