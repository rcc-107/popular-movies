package com.rica.popularmovies.fragments;

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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rica.popularmovies.R;
import com.rica.popularmovies.SettingsActivity;
import com.rica.popularmovies.adapters.RecyclerViewAdapter;
import com.rica.popularmovies.data.MovieContract.MovieEntry;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, RecyclerViewAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Cursor cursor;
    public static RecyclerViewAdapter rvAdapter;
    private static final int GRID_NUM_COUNT = 3;
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
    public static final int POSTER_PATH = 3;
    static final int POPULARITY = 4;
    static final int VOTE_AVERAGE = 5;
    static final int RELEASE_DATE = 6;
    static final int DATE_ADDED = 7;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static MainActivityFragment newInstance(int sectionNumber) {
        MainActivityFragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        rvAdapter = new RecyclerViewAdapter(getContext(), this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        layoutManager = new GridLayoutManager(getActivity(),GRID_NUM_COUNT);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(rvAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(SettingsActivity.sortOrder){
           updateList();
        }
    }

    public void updateList(){
        getLoaderManager().restartLoader(LOADER_ID,null,this);
    }

    @Override
    public void itemClicked(View v, int position) {
        if(!cursor.isClosed()) {
            if (cursor.moveToPosition(position)) {
                ((Callback) getActivity()).onItemClicked(MovieEntry.buildMovieUriWithMovieID(cursor.getInt(MOVIE_ID)),v);
            }
        }
    }

    public interface Callback {
        void onItemClicked(Uri movieID, View view);
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
