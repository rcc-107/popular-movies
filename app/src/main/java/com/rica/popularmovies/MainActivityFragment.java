package com.rica.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rica.popularmovies.data.MovieContract.MovieEntry;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, RecyclerViewAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Cursor cursor;
    static RecyclerViewAdapter rvAdapter;
    private static final int NUM_COUNT = 3;
    private static final int LOADER_ID = 1;

    private static final String[] Movie_Colums = {
        MovieEntry.TITLE,
        MovieEntry.MOVIE_ID,
        MovieEntry.SYNOPSIS,
        MovieEntry.POSTER_PATH,
        MovieEntry.POPULARITY,
        MovieEntry.VOTE_AVERAGE,
        MovieEntry.RELEASE_DATE,
        MovieEntry.DATE_ADDED
    };

    static final int TITLE = 0;
    static final int MOVIE_ID = 1;
    static final int SYNOPSIS = 2;
    static final int POSTER_PATH = 3;
    static final int POPULARITY = 4;
    static final int VOTE_AVERAGE = 5;
    static final int RELEASE_DATE = 6;
    static final int DATE_ADDED = 7;

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        rvAdapter = new RecyclerViewAdapter(getContext(), this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        layoutManager = new GridLayoutManager(getActivity(),NUM_COUNT);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(rvAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID,null,this);
        View view = getView();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(SettingsActivity.sortOrder){
           updateList();
            SettingsActivity.sortOrder = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings){
            Intent intent = new Intent(getActivity(),SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateList(){
        getLoaderManager().restartLoader(LOADER_ID,null,this);
    }

    @Override
    public void itemClicked(View v, int position) {
        if(!cursor.isClosed()) {
            if (cursor.moveToPosition(position)) {
                ((Callback) getActivity()).onItemClicked(MovieEntry.buildMovieUriWithMovieID(cursor.getInt(MOVIE_ID)));
            }
        }
    }

    public interface Callback {
        void onItemClicked(Uri movieID);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sort = sp.getString(getString(R.string.pref_sort_key),MovieEntry.POPULARITY)+" Desc";
        Uri moviesUri;

        if(getString(R.string.popularity)==sort) {
            moviesUri = MovieEntry.buildMovieUriSortByPopularity();
        }else /*if(getString(R.string.vote_desc).equals(sort))*/{
            moviesUri = MovieEntry.buildMovieUriSortByVote();
        }

        return new CursorLoader(getContext(),moviesUri,Movie_Colums,null,null,sort);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       rvAdapter.swapCursor(data);
       cursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        rvAdapter.swapCursor(null);
    }

}
