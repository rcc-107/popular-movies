package com.rica.popularmovies;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rica.popularmovies.data.MovieContract;

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

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TITLE,
            MovieContract.MovieEntry.MOVIE_ID,
            MovieContract.MovieEntry.SYNOPSIS,
            MovieContract.MovieEntry.POSTER_PATH,
            MovieContract.MovieEntry.POPULARITY,
            MovieContract.MovieEntry.VOTE_AVERAGE,
            MovieContract.MovieEntry.RELEASE_DATE,
            MovieContract.MovieEntry.BACKDROP_PATH,
            MovieContract.MovieEntry.DATE_ADDED
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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getLoaderManager().initLoader(LOADER_ID,null,this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings){
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(uri != null){
            String selection = MovieContract.MovieEntry.MOVIE_ID + " = ? ";
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
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
