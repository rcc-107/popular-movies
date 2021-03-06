package com.rica.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rica.popularmovies.data.MovieContract.MovieEntry;
import com.rica.popularmovies.data.MovieContract.MovieVideos;

import com.rica.popularmovies.data.MovieContract.MovieReviews;

/**
 * Created by Rica on 8/18/2016.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "movies";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.MOVIE_ID + " INTEGER NOT NULL UNIQUE," +
                MovieEntry.TITLE + " TEXT NOT NULL," +
                MovieEntry.RELEASE_DATE + " INTEGER," +
                MovieEntry.SYNOPSIS + " TEXT," +
                MovieEntry.POPULARITY + " INTEGER," +
                MovieEntry.VOTE_AVERAGE + " INTEGER," +
                MovieEntry.POSTER_PATH + " TEXT," +
                MovieEntry.BACKDROP_PATH + " TEXT," +
                MovieEntry.FAVORITES + " INTEGER NOT NULL DEFAULT 0," +
                MovieEntry.DATE_ADDED + " INTEGER NOT NULL);";

        final String CREATE_VIDEOS_TABLE = "CREATE TABLE " + MovieVideos.TABLE_NAME + " (" +
                MovieVideos._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MovieVideos.VIDEO_ID + " TEXT,"+
                MovieVideos.VIDEO_TITLE + " TEXT,"+
                MovieVideos.VIDEO_PATH + " TEXT NOT NULL UNIQUE,"+
                MovieVideos.MOVIE_ID + " TEXT,"+
                " FOREIGN KEY("+ MovieVideos.MOVIE_ID+")"+" REFERENCES "+
                MovieEntry.TABLE_NAME+"("+MovieEntry.MOVIE_ID+"));";

        final String CREATE_REVIEWS_TABLE = "CREATE TABLE " + MovieReviews.TABLE_NAME + " (" +
                MovieReviews._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MovieReviews.REVIEW_ID + " TEXT,"+
                MovieReviews.REVIEW_AUTHOR + " TEXT,"+
                MovieReviews.REVIEW_CONTENT + " TEXT NOT NULL UNIQUE,"+
                MovieReviews.MOVIE_ID + " TEXT,"+
                " FOREIGN KEY("+ MovieReviews.MOVIE_ID+")"+" REFERENCES "+
                MovieEntry.TABLE_NAME+"("+MovieEntry.MOVIE_ID+"));";

        sqLiteDatabase.execSQL(CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(CREATE_VIDEOS_TABLE);
        sqLiteDatabase.execSQL(CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
