package com.rica.popularmovies.fragments;

import android.content.Intent;
import android.database.Cursor;
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
import com.rica.popularmovies.data.MovieContract.MovieVideos;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String MOVIE_URI = "movie_uri";
    private final int LOADER_ID = 2;
    private Uri uri;

    private TextView title;
    private TextView synopsis;
    private TextView release;
    private TextView popularity;
    private TextView vote;
    private ImageView poster;
    private LinearLayout trailer_container;

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
            MovieVideos.VIDEO_PATH
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
        getLoaderManager().initLoader(LOADER_ID,null,this);
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
            String selection = MovieEntry.TABLE_NAME+"."+MovieEntry.MOVIE_ID + " = ? ";
            String[] selectionArgs = {Utility.getIDFromUri(uri)};
            return new CursorLoader(getContext(),uri,MOVIE_COLUMNS,selection,selectionArgs,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()){
            title.setText(data.getString(TITLE));
            synopsis.setText(data.getString(SYNOPSIS));
            release.setText(data.getString(RELEASE_DATE));
            popularity.setText(data.getString(POPULARITY));
            vote.setText(data.getString(VOTE_AVERAGE));
            Utility.setPoster(getContext(),data.getString(BACKDROP),poster);
            for(int i=0; i<data.getCount(); i++) {
                TextView trailer_title = new TextView(getContext());
                trailer_title.setGravity(Gravity.VERTICAL_GRAVITY_MASK);
                trailer_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.play,0,0,0);
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
                data.moveToNext();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
