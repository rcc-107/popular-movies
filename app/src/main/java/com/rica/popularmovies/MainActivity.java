package com.rica.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.stetho.Stetho;
import com.rica.popularmovies.sync.MovieSyncAdapter;

import static com.facebook.stetho.Stetho.newInitializerBuilder;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private final String MAINFRAGMENTTAG = "MFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initialize(newInitializerBuilder(getBaseContext())
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(getBaseContext())).build());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.framelayout, new MainActivityFragment(),MAINFRAGMENTTAG).commit();
        MovieSyncAdapter.initializeAdapter(getApplicationContext());
    }

    @Override
    public void onItemClicked(Uri movieID) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.setData(movieID);
        startActivity(intent);
    }
}
