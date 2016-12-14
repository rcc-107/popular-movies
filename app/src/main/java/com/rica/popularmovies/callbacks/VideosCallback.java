package com.rica.popularmovies.callbacks;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.rica.popularmovies.Utility;
import com.rica.popularmovies.data.MovieContract.MovieVideos;
import com.rica.popularmovies.fragments.DetailActivityFragment;

public class VideosCallback implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context mContext;
    private Cursor mCursor;
    private Uri uri;
    private String[] VIDEO_COLUMNS;
    
    public VideosCallback(Context context, String movieID, String[] COLUMNS) {
        mContext = context;
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
            CursorLoader cursorLoader = new CursorLoader(mContext,uri,VIDEO_COLUMNS,selection,selectionArgs,null);
            return cursorLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        DetailActivityFragment.updateVideoAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        DetailActivityFragment.updateVideoAdapter(null);
    }

    public Cursor getCursor() {
        return mCursor;
    }

}