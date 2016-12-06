package com.rica.popularmovies.callbacks;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.rica.popularmovies.Utility;
import com.rica.popularmovies.data.MovieContract;

/**
 * Created by Rica on 11/5/2016.
 */

public class DetailsCallback implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;
    private Uri uri;
    private String[] MOVIE_COLUMNS;

    public  DetailsCallback(Context context, Uri uri, String[] COLUMNS) {
        this.context = context;
        this.uri = uri;
        MOVIE_COLUMNS = COLUMNS;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(uri != null){
            String movieID = Utility.getIDFromUri(uri);
            String selection;
            String[] selectionArgs = {movieID};
            Log.d("daf"," details oncreates");
            selection = MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.MOVIE_ID + " = ? ";
            return new CursorLoader(context,uri,MOVIE_COLUMNS,selection,selectionArgs,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()) {
            Log.d("daf", " details onloadfinished");
//            DetailActivityFragment.loadMovieDetailsToUI(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
