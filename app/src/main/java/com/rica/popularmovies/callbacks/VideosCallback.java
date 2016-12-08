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
import com.rica.popularmovies.data.MovieContract.MovieVideos;
import com.rica.popularmovies.fragments.DetailActivityFragment;

public class VideosCallback implements LoaderManager.LoaderCallbacks<Cursor> {
			 private Context context;
    private Uri uri;
    private String[] VIDEO_COLUMNS;
    
    public VideosCallback(Context context, String movieID, String[] COLUMNS) {
        this.context = context;
        uri = ContentUris.withAppendedId(MovieVideos.CONTENT_URI,Long.parseLong(movieID));
        VIDEO_COLUMNS = COLUMNS;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(uri != null){
            String movieID = Utility.getIDFromUri(uri);
            String selection;
            String[] selectionArgs = {movieID};
            selection = MovieVideos.MOVIE_ID + " = ? ";
            CursorLoader cursorLoader = new CursorLoader(context,uri,VIDEO_COLUMNS,selection,selectionArgs,null);
            return cursorLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        DetailActivityFragment.updateVideoAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        DetailActivityFragment.updateVideoAdapter(null);
    }
}