package com.rica.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rica.popularmovies.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter rvAdapter;
    private static final int NUM_COUNT = 3;
    private static final int LOADER_ID = 1;

    private static final String[] Movie_Colums = {
        MovieContract.MovieEntry.TITLE,
        MovieContract.MovieEntry.SYNOPSIS,
        MovieContract.MovieEntry.POSTER_PATH,
        MovieContract.MovieEntry.VOTE_AVERAGE,
        MovieContract.MovieEntry.RELEASE_DATE,
        MovieContract.MovieEntry.DATE_ADDED
    };

    static final int TITLE = 0;
    static final int SYNOPSIS = 1;
    static final int POSTER_PATH = 2;
    static final int VOTE_AVERAGE = 3;
    static final int RELEASE_DATE = 4;
    static final int DATE_ADDED = 5;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        rvAdapter = new RecyclerViewAdapter(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        layoutManager = new GridLayoutManager(getActivity(),NUM_COUNT);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sort = MovieContract.MovieEntry.DATE_ADDED + " DESC";
        Uri moviesUri = MovieContract.MovieEntry.buildMovieUriWithStartDate(System.currentTimeMillis());
        return new CursorLoader(getContext(),moviesUri,Movie_Colums,null,null,sort);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       rvAdapter.swapCursor(data);
        Log.d("cursor: ",data.toString());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        rvAdapter.swapCursor(null);
    }
}
