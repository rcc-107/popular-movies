package com.rica.popularmovies.callbacks;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.rica.popularmovies.Utility;
import com.rica.popularmovies.adapters.ReviewsAdapter;
import com.rica.popularmovies.data.MovieContract.MovieReviews;
import com.rica.popularmovies.fragments.DetailActivityFragment;

/**
 * Created by Rica on 11/5/2016.
 */

public class ReviewsCallback implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;
    private Uri uri;
    private String[] REVIEW_COLUMNS;

    public  ReviewsCallback(Context context, String movieID, String[] COLUMNS, ReviewsAdapter reviewsAdapter) {
        this.context = context;
        uri = ContentUris.withAppendedId(MovieReviews.CONTENT_URI,Long.parseLong(movieID));
        REVIEW_COLUMNS = COLUMNS;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(uri != null){
            String movieID = Utility.getIDFromUri(uri);
            String selection;
            String[] selectionArgs = {movieID};
            Log.d("daf"," reviews oncreateloader");
            selection = MovieReviews.MOVIE_ID + " = ? ";
            CursorLoader cursorLoader = new CursorLoader(context,uri,REVIEW_COLUMNS,selection,selectionArgs,null);
            return cursorLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("daf"," onLoadFinished called");
        DetailActivityFragment.updateAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        DetailActivityFragment.updateAdapter(null);
    }
}
