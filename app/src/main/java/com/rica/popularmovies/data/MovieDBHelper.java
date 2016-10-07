package com.rica.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rica.popularmovies.data.MovieContract.MovieEntry;
import com.rica.popularmovies.data.MovieContract.MovieVideos;

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
                MovieEntry.MOVIE_ID + " INTEGER NOT NULL," +
                MovieEntry.TITLE + " TEXT NOT NULL," +
                MovieEntry.RELEASE_DATE + " INTEGER NOT NULL," +
                MovieEntry.SYNOPSIS + " TEXT NOT NULL," +
                MovieEntry.POPULARITY + " INTEGER NOT NULL," +
                MovieEntry.VOTE_AVERAGE + " INTEGER NOT NULL," +
                MovieEntry.POSTER_PATH + " TEXT NOT NULL," +
                MovieEntry.BACKDROP_PATH + " TEXT NOT NULL," +
                MovieEntry.FAVORITES + " BOOLEAN NOT NULL DEFAULT TRUE," +
                MovieEntry.DATE_ADDED + " INTEGER NOT NULL);";

        final String CREATE_VIDEOS_TABLE = "CREATE TABLE " + MovieVideos.TABLE_NAME + " (" +
                MovieVideos._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MovieVideos.VIDEO_ID + " TEXT,"+
                MovieVideos.VIDEO_TITLE + " TEXT,"+
                MovieVideos.VIDEO_PATH + " TEXT,"+
                MovieVideos.MOVIE_ID + " TEXT,"+
                " FOREIGN KEY("+ MovieVideos.MOVIE_ID+")"+" REFERENCES "+
                MovieEntry.TABLE_NAME+"("+MovieEntry.MOVIE_ID+"));";

        sqLiteDatabase.execSQL(CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(CREATE_VIDEOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
