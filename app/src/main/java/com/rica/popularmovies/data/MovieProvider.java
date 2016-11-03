package com.rica.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rica.popularmovies.data.MovieContract.MovieEntry;
import com.rica.popularmovies.data.MovieContract.MovieReviews;
import com.rica.popularmovies.data.MovieContract.MovieVideos;

/**
 * Created by Rica on 8/18/2016.
 */
public class MovieProvider extends ContentProvider {

    private MovieDBHelper movieDBHelper;
    private static final UriMatcher mUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sqlBuilder;

    private static final int MOVIE_SORT_POPULARITY = 100;
    private static final int MOVIE_SORT_VOTE = 101;
    private static final int MOVIE_WITH_ID = 102;
    private static final int MOVIE_VIDEO_WITH_ID = 103;
    private static final int MOVIES = 104;
    private static final int VIDEOS = 105;
    private static final int REVIEWS = 106;
    private static final int MOVIE_FAVORITES = 107;
    private static final int REVIEWS_WITH_ID = 108;

    static {
        sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables( MovieEntry.TABLE_NAME + " LEFT JOIN " + MovieVideos.TABLE_NAME +
                " ON " + MovieEntry.TABLE_NAME + "." + MovieEntry.MOVIE_ID + " = " + MovieVideos.TABLE_NAME + "." + MovieVideos.MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        movieDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectArgs, String sort) {
        Cursor returnCursor;
        SQLiteDatabase db = movieDBHelper.getReadableDatabase();
        final int matcher = mUriMatcher.match(uri);
        switch(matcher){
            case MOVIE_FAVORITES:
                returnCursor = db.query(MovieEntry.TABLE_NAME,projection,selection,selectArgs,null,null,sort);
                break;
            case MOVIE_SORT_POPULARITY:
                returnCursor = db.query(MovieEntry.TABLE_NAME,
                        projection,selection,selectArgs,null,null,sort);
                break;
            case MOVIE_SORT_VOTE:
                returnCursor = db.query(MovieEntry.TABLE_NAME,
                        projection,selection,selectArgs,null,null,sort);
                break;
            case REVIEWS_WITH_ID:
                returnCursor = db.query(MovieReviews.TABLE_NAME,
                        projection,selection,selectArgs,null,null,null);
                Log.d("reviews returned: ", " number of rows: "+returnCursor.getCount());
                break;
            case MOVIE_WITH_ID:
                returnCursor = sqlBuilder.query(db,projection,selection,selectArgs,null,null,sort);
                Log.d("returned: ", " number of rows: "+returnCursor.getCount());
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match){
            case MOVIES:
                return MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int matcher = mUriMatcher.match(uri);
        Uri retUri;

        switch (matcher){
            case MOVIES:
                long _id = db.insert(MovieEntry.TABLE_NAME,null,contentValues);
                if(_id != -1){
                    retUri = MovieEntry.buildMovieUri(_id);
                }else{
                    throw new SQLException("Failed to insert row into "+uri);
                }
                break;
            case VIDEOS:
                long video_id = db.insert(MovieVideos.TABLE_NAME,null,contentValues);
                if(video_id != -1){
                    retUri = MovieEntry.buildMovieUri(video_id);
                }else{
                    throw new SQLException("Failed to insert row into "+uri);
                }
                break;
            case REVIEWS:
                long review_id = db.insert(MovieReviews.TABLE_NAME,null,contentValues);
                if(review_id != -1){
                    retUri = MovieEntry.buildMovieUri(review_id);
                }else{
                    throw new SQLException("Failed to insert row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        return retUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int matcher = mUriMatcher.match(uri);

        switch (matcher){
            case MOVIES:
                return bulkInsertData(db,uri,MovieEntry.TABLE_NAME,values);
            case VIDEOS:
                return bulkInsertData(db,uri,MovieVideos.TABLE_NAME,values);
            case REVIEWS:
                return bulkInsertData(db,uri,MovieReviews.TABLE_NAME,values);
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private int bulkInsertData(SQLiteDatabase db, Uri uri, String tableName, ContentValues[] values){
        db.beginTransaction();
        int retCount = 0;
        try{
            for(ContentValues value:values){
                long _id = db.insert(tableName,null,value);
                Log.d("Bulkinsert",value+" inserted");
                if(_id != -1){
                    retCount++;
                }
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return retCount;
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIES+"/popularity",MOVIE_SORT_POPULARITY);
        matcher.addURI(authority, MovieContract.PATH_MOVIES+"/vote_average",MOVIE_SORT_VOTE);
        matcher.addURI(authority, MovieContract.PATH_MOVIES+"/favorites",MOVIE_FAVORITES);
        matcher.addURI(authority, MovieContract.PATH_VIDEOS+"/*",MOVIE_VIDEO_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_MOVIES+"/*",MOVIE_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_REVIEWS+"/*",REVIEWS_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_REVIEWS,REVIEWS);
        matcher.addURI(authority, MovieContract.PATH_MOVIES,MOVIES);
        matcher.addURI(authority, MovieContract.PATH_VIDEOS,VIDEOS);

        return matcher;
    }

}
