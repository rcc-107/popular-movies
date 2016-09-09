package com.rica.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.rica.popularmovies.Utility;

/**
 * Created by Rica on 8/18/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.rica.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movies";
        public static final String MOVIE_ID = "movie_id";
        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "release_date";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String SYNOPSIS = "synopsis";
        public static final String POSTER_PATH = "poster_path";
        public static final String DATE_ADDED = "date_added";

        ///// content://com.rica.popularmovies/movies/10900908
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        ///// content://com.rica.popularmovies/movies/date/10920898
        public static Uri buildMovieUriWithStartDate(long date){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(Utility.normalizeDate(date))).build();
        }
    }

}
