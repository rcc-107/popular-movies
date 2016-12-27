package com.rica.popularmovies;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Rica on 12/27/2016.
 */

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent)){
            String query = intent.getStringExtra(SearchManager.QUERY);
            //do your searching
        }
    }
}
