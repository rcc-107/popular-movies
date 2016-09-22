package com.rica.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private final String FRAGMENTTAG = "DAFTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(savedInstanceState == null) {
            Bundle arg = new Bundle();
            arg.putParcelable(DetailActivityFragment.MOVIE_URI, getIntent().getData());
            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arg);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_framelayout, fragment, FRAGMENTTAG).commit();
        }

    }

}
