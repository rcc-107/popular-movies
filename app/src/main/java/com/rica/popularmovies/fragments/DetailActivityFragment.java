package com.rica.popularmovies.fragments;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rica.popularmovies.R;
import com.rica.popularmovies.SettingsActivity;
import com.rica.popularmovies.Utility;
import com.rica.popularmovies.data.MovieContract.MovieEntry;
import com.rica.popularmovies.data.MovieContract.MovieReviews;
import com.rica.popularmovies.data.MovieContract.MovieVideos;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String MOVIE_URI = "movie_uri";
    private final int DETAILS_LOADER_ID = 2;
    private final int REVIEW_LOADER_ID = 3;
    private Uri uri;
    private String fetch = "details";

    private TextView title;
    private TextView synopsis;
    private TextView release;
    private TextView popularity;
    private TextView vote;
    private ImageView poster;
    private LinearLayout trailer_container;
    private LinearLayout reviews_container;

    private static final String[] MOVIE_COLUMNS = {
            MovieEntry.TITLE,
            MovieEntry.TABLE_NAME+"."+MovieEntry.MOVIE_ID,
            MovieEntry.SYNOPSIS,
            MovieEntry.POSTER_PATH,
            MovieEntry.POPULARITY,
            MovieEntry.VOTE_AVERAGE,
            MovieEntry.RELEASE_DATE,
            MovieEntry.BACKDROP_PATH,
            MovieEntry.DATE_ADDED,
            MovieVideos.VIDEO_TITLE,
            MovieVideos.VIDEO_PATH,
    };

    private static final String[] REVIEW_COLUMNS = {
            MovieReviews.REVIEW_ID,
            MovieReviews.REVIEW_AUTHOR,
            MovieReviews.REVIEW_CONTENT
    };

    static final int TITLE = 0;
    static final int MOVIE_ID = 1;
    static final int SYNOPSIS = 2;
    static final int POSTER_PATH = 3;
    static final int POPULARITY = 4;
    static final int VOTE_AVERAGE = 5;
    static final int RELEASE_DATE = 6;
    static final int BACKDROP = 7;
    static final int DATE_ADDED = 8;
    static final int VIDEO_TITLE = 9;
    static final int VIDEO_PATH = 10;

    static final int REVIEW_ID = 0;
    static final int REVIEW_AUTHOR = 1;
    static final int REVIEW_CONTENT = 2;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arg = getArguments();
        if(arg != null){
            uri = arg.getParcelable(MOVIE_URI);
        }
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        title = (TextView) view.findViewById(R.id.movieTitle);
        synopsis = (TextView) view.findViewById(R.id.movieSynopsis);
        release = (TextView) view.findViewById(R.id.movieRelease);
        popularity = (TextView) view.findViewById(R.id.moviePopularity);
        vote = (TextView) view.findViewById(R.id.movieVote);
        poster = (ImageView) view.findViewById(R.id.moviePoster);
        trailer_container = (LinearLayout) view.findViewById(R.id.trailers_container);
        reviews_container = (LinearLayout) view.findViewById(R.id.reviews_container);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity aca = (AppCompatActivity) getActivity();
        aca.setSupportActionBar(toolbar);
        aca.setTitle(null);
        aca.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getLoaderManager().initLoader(DETAILS_LOADER_ID,null,this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().supportFinishAfterTransition();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(uri != null){
            String selection;
            String[] selectionArgs = {Utility.getIDFromUri(uri)};;
            if(fetch == "details") {
                selection = MovieEntry.TABLE_NAME + "." + MovieEntry.MOVIE_ID + " = ? ";
                return new CursorLoader(getContext(),uri,MOVIE_COLUMNS,selection,selectionArgs,null);
            }else{
                selection = MovieReviews.MOVIE_ID + " = ? ";
                return new CursorLoader(getContext(),uri,REVIEW_COLUMNS,selection,selectionArgs,null);
            }

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()){
            if(fetch == "details"){
                loadMovieDetailsToUI(data);
            }else{
                loadMovieReviesToUI(data);
            }
        }
    }

    private void loadMovieDetailsToUI(Cursor data){
        this.uri = ContentUris.withAppendedId(MovieReviews.CONTENT_URI,Long.parseLong(data.getString(MOVIE_ID)));

        title.setText(data.getString(TITLE));
        synopsis.setText(data.getString(SYNOPSIS));
        release.setText(data.getString(RELEASE_DATE));
        popularity.setText(data.getString(POPULARITY));
        vote.setText(data.getString(VOTE_AVERAGE));
        Utility.setPoster(getContext(),data.getString(BACKDROP),poster);
        for(int i=0; i<data.getCount(); i++) {
            if(data.getString(VIDEO_TITLE) != null) {
                TextView trailer_title = new TextView(getContext());
                trailer_title.setGravity(Gravity.VERTICAL_GRAVITY_MASK);
                trailer_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.play, 0, 0, 0);
                trailer_title.setText(data.getString(VIDEO_TITLE));
                final String path = data.getString(VIDEO_PATH);
                trailer_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse("https://www.youtube.com/watch?v=").buildUpon().appendPath(path).build();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(uri);

                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
                trailer_container.addView(trailer_title);
            }
            data.moveToNext();
        }
        fetch = "reviews";

        getLoaderManager().initLoader(REVIEW_LOADER_ID,null,this);
    }

    private void loadMovieReviesToUI(Cursor data){
        for(int i=0; i<data.getCount(); i++) {
            if(data.getString(REVIEW_AUTHOR) != null) {
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,40,0,40);
                linearLayout.setLayoutParams(params);
                TextView review_author = new TextView(getContext());
                review_author.setTypeface(null, Typeface.BOLD);
                review_author.setTextColor(Color.BLACK);
                TextView review_content = new TextView(getContext());
                review_author.setText(data.getString(REVIEW_AUTHOR));
                review_content.setText(data.getString(REVIEW_CONTENT));
                linearLayout.addView(review_author);
                linearLayout.addView(review_content);
                reviews_container.addView(linearLayout);
            }
            data.moveToNext();
        }
        fetch = "details";
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
