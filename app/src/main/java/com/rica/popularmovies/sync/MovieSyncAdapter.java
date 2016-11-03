package com.rica.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.rica.popularmovies.R;
import com.rica.popularmovies.Utility;
import com.rica.popularmovies.data.MovieContract.MovieEntry;
import com.rica.popularmovies.data.MovieContract.MovieReviews;
import com.rica.popularmovies.data.MovieContract.MovieVideos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import static com.rica.popularmovies.sync.MovieSyncAdapter.Options.LIST;

/**
 * Created by Rica on 8/21/2016.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    private Context context;
    private Options fetch;
    private ArrayList<Integer> movieIDList = new ArrayList<>();

    private static final int SYNC_INTERVAL = 30;
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final String MOVIE_ID = "movieID";

    public enum Options{
        LIST,VIDEOS,REVIEWS
    }

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.context = context;
        fetch = LIST;
    }

    public static void initializeAdapter(Context context){
        getSyncAccount(context);
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),context.getString(R.string.authority),bundle);
    }

    private static Account getSyncAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account account = new Account(context.getString(R.string.app_name), context.getString(R.string.account_type));
        if(null == accountManager.getPassword(account)){ //if the password of account doesnt exist, account doesnt exist
            if(!accountManager.addAccountExplicitly(account,"",null)){
                return null; //If adding the new account fails, return null
            }
            onAccountCreated(account,context);
        }
        return account;
    }

    private static void onAccountCreated(Account account, Context context) {
//        configurePeriodicSync(context,SYNC_INTERVAL,SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(account,context.getString(R.string.authority),false);
        syncImmediately(context);
    }

 /*   private static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.authority);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            SyncRequest syncRequest = new SyncRequest.Builder().syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account,authority)
                    .setExtras(new Bundle()).build();
            ContentResolver.requestSync(syncRequest);
        }else{
            ContentResolver.addPeriodicSync(account,authority,new Bundle(),syncInterval);
        }
    }*/

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        try{
            if(fetch == Options.VIDEOS) {
                for (int i = 0; i < movieIDList.size(); i++) {
                    Uri uri = Uri.parse("https://api.themoviedb.org/3/movie").buildUpon()
                            .appendPath(Integer.toString(movieIDList.get(i))).appendPath("videos")
                            .appendQueryParameter("api_key", "62e177741ae6467e6af95e2c21d57c6e").build();
                    URL url = new URL(uri.toString());
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    if (inputStream == null) {
                        return;
                    }
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line + "\n");
                    }
                    if (stringBuffer.length() == 0) {
                        return;
                    }
                    getVideoDataFromJSON(stringBuffer.toString());
                }
            }else if(fetch == Options.REVIEWS) {
                for(int i = 0; i< movieIDList.size(); i++) {
                    Uri uri = Uri.parse("https://api.themoviedb.org/3/movie").buildUpon()
                            .appendPath(Integer.toString(movieIDList.get(i))).appendPath("reviews")
                            .appendQueryParameter("api_key","62e177741ae6467e6af95e2c21d57c6e").build();
                    URL url = new URL(uri.toString());
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    if (inputStream == null) {
                        return;
                    }
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line + "\n");
                    }
                    if (stringBuffer.length() == 0) {
                        return;
                    }
                    Log.d("httpURLConnection Rev","url: "+uri.toString()+ " returned: " + stringBuffer.toString());
                    getReviewsDataFromJSON(stringBuffer.toString());
                }
            }else{
                Uri uri = Uri.parse("https://api.themoviedb.org/3/discover/movie").buildUpon()
                        .appendQueryParameter("sort_by", this.context.getString(R.string.popularity))
                        .appendQueryParameter("api_key","62e177741ae6467e6af95e2c21d57c6e").build();
                URL url = new URL(uri.toString());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if (inputStream == null) {
                    return;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
                if (stringBuffer.length() == 0) {
                    return;
                }
                getMovieDataFromJSON(stringBuffer.toString());
            }
        }catch (IOException e){
            Log.e("IOException", "Error ", e);
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
    }

    private void getMovieDataFromJSON(String movieJSONStr) {
        final String TMDB_RESULTS = "results";

        final String TMDB_OVERVIEW = "overview";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_MOVIE_ID = "id";
        final String TMDB_ORIGINAL_LANGUAGE = "original_language";
        final String TMDB_TITLE = "title";
        final String TMDB_POPULARITY = "popularity";
        final String TMDB_VOTE_AVERAGE = "vote_average";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_BACKDROP_PATH = "backdrop_path";
        //for debugging
        Log.d("movie json",movieJSONStr);
        try{
            JSONObject movieJSON = new JSONObject(movieJSONStr);
            JSONArray resultsJA = movieJSON.getJSONArray(TMDB_RESULTS);

            Vector<ContentValues> movieVector = new Vector<>(resultsJA.length());

            for(int i=0; i < resultsJA.length(); i++){
                JSONObject resultsJO = resultsJA.getJSONObject(i);
                String overview = resultsJO.getString(TMDB_OVERVIEW);
                String release_date = resultsJO.getString(TMDB_RELEASE_DATE);
                int movie_id = resultsJO.getInt(TMDB_MOVIE_ID);
                movieIDList.add(movie_id);
                String title = resultsJO.getString(TMDB_TITLE);
                Double popularity = resultsJO.getDouble(TMDB_POPULARITY);
                Double vote_average = resultsJO.getDouble(TMDB_VOTE_AVERAGE);
                String poster = resultsJO.getString(TMDB_POSTER_PATH);
                String backdrop = resultsJO.getString(TMDB_BACKDROP_PATH);
                long date_today = Utility.normalizeDate(System.currentTimeMillis());

                ContentValues movieValues = new ContentValues();
                movieValues.put(MovieEntry.MOVIE_ID,movie_id);
                movieValues.put(MovieEntry.RELEASE_DATE,release_date);
                movieValues.put(MovieEntry.SYNOPSIS,overview);
                movieValues.put(MovieEntry.TITLE,title);
                movieValues.put(MovieEntry.POPULARITY,popularity);
                movieValues.put(MovieEntry.VOTE_AVERAGE,vote_average);
                movieValues.put(MovieEntry.POSTER_PATH,poster);
                movieValues.put(MovieEntry.BACKDROP_PATH,backdrop);
                movieValues.put(MovieEntry.DATE_ADDED,date_today);

                movieVector.add(movieValues);
            }

            if(movieVector.size() > 0){
                ContentValues[] cvArray = new ContentValues[movieVector.size()];
                movieVector.toArray(cvArray);
                this.context.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI,cvArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fetch = Options.VIDEOS;
        syncImmediately(this.context);

    }

    private void getVideoDataFromJSON(String movieJSONStr) {
        final String VIDEOS_RESULT = "results";
        final String VIDEOS_ID = "id";
        final String VIDEOS_PATH = "key";
        final String VIDEOS_TITLE = "name";
        try{
            JSONObject jsonObject = new JSONObject(movieJSONStr);
            JSONArray results = jsonObject.getJSONArray(VIDEOS_RESULT);

            Vector<ContentValues> vector = new Vector<>(results.length());
            for (int index=0;index<results.length();index++) {
                JSONObject videoJObj = results.getJSONObject(index);
                String movie_id = jsonObject.getString("id");
                String video_id = videoJObj.getString(VIDEOS_ID);
                String path = videoJObj.getString(VIDEOS_PATH);
                String title = videoJObj.getString(VIDEOS_TITLE);

                ContentValues cv = new ContentValues();
                cv.put(MovieVideos.VIDEO_ID, video_id);
                cv.put(MovieVideos.MOVIE_ID, movie_id);
                cv.put(MovieVideos.VIDEO_TITLE, title);
                cv.put(MovieVideos.VIDEO_PATH, path);
                vector.add(cv);
            }
            if(vector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[vector.size()];
                vector.toArray(cvArray);
                this.context.getContentResolver().bulkInsert(MovieVideos.CONTENT_URI,cvArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fetch = Options.REVIEWS;
        syncImmediately(this.context);
    }

    private void getReviewsDataFromJSON(String movieJSONStr) {
        final String REVIEWS_RESULT = "results";
        final String REVIEWS_ID = "id";
        final String REVIEWS_AUTHOR = "author";
        final String REVIEWS_CONTENT = "content";
        try{
            JSONObject jsonObject = new JSONObject(movieJSONStr);
            JSONArray results = jsonObject.getJSONArray(REVIEWS_RESULT);

            Vector<ContentValues> vector = new Vector<>(results.length());
            for (int index=0;index<results.length();index++) {
                JSONObject reviewJObj = results.getJSONObject(index);
                String movie_id = jsonObject.getString("id");
                String review_id = reviewJObj.getString(REVIEWS_ID);
                String author = reviewJObj.getString(REVIEWS_AUTHOR);
                String content = reviewJObj.getString(REVIEWS_CONTENT);

                ContentValues cv = new ContentValues();
                cv.put(MovieReviews.REVIEW_ID, review_id);
                cv.put(MovieReviews.MOVIE_ID, movie_id);
                cv.put(MovieReviews.REVIEW_AUTHOR, author);
                cv.put(MovieReviews.REVIEW_CONTENT, content);
                vector.add(cv);
            }
            if(vector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[vector.size()];
                vector.toArray(cvArray);
                this.context.getContentResolver().bulkInsert(MovieReviews.CONTENT_URI,cvArray);
                Log.d("reviewList",cvArray.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fetch = Options.LIST;
    }

}
