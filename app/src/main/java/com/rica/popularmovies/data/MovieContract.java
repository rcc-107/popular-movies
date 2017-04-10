package com.rica.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Rica on 8/18/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.rica.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_VIDEOS = "videos";
    public static final String PATH_REVIEWS = "reviews";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movies";
        public static final String MOVIE_ID = "movie_id";
        public static final String TITLE = "movie_title";
        public static final String RELEASE_DATE = "release_date";
        public static final String POPULARITY = "popularity";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String SYNOPSIS = "synopsis";
        public static final String POSTER_PATH = "poster_path";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String FAVORITES = "favorites";
        public static final String DATE_ADDED = "date_added";

        ///// content://com.rica.popularmovies/movies/10900908
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

       /* /////sort by date added to database content://com.rica.popularmovies/movies/date/10920898
        public static Uri buildMovieUriWithStartDate(long date){
            return CONTENT_URI.buildUpon().appendPath("date").appendPath(Long.toString(Utility.normalizeDate(date))).build();
        }*/

        public static Uri buildMovieListUri() {
            return CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        }

        public static Uri buildMovieUriWithMovieID(int movieID) {
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(movieID)).build();
        }

        public static Uri buildMovieUriWithFavorites() {
            return CONTENT_URI.buildUpon().appendPath(FAVORITES).build();
        }

    }

    public static final class MovieVideos implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEOS).build();

        public static final String TABLE_NAME = "videos";
        public static final String MOVIE_ID = "movie_id"; //foreign Key
        public static final String VIDEO_ID = "video_id";
        public static final String VIDEO_TITLE = "title";
        public static final String VIDEO_PATH = "path";
    }

    public static final class MovieReviews implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String TABLE_NAME = "reviews";
        public static final String MOVIE_ID = "movie_id"; //foreign Key
        public static final String REVIEW_ID = "review_id";
        public static final String REVIEW_AUTHOR = "author";
        public static final String REVIEW_CONTENT = "content";
    }

}
