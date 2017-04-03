package com.rica.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.rica.popularmovies.sync.MovieSyncAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Rica on 8/21/2016.
 */
public class Utility {

    public static long normalizeDate(long dateInMili) {
        GregorianCalendar gregorianCalendar = (GregorianCalendar) Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        gregorianCalendar.setTime(new Date(dateInMili));
        gregorianCalendar.add(Calendar.HOUR, 0);
        gregorianCalendar.add(Calendar.MINUTE, 0);
        gregorianCalendar.add(Calendar.SECOND, 0);
        return gregorianCalendar.getTimeInMillis();
    }

    public static void setPoster(final Context context, String relativePath, final ImageView img) {
        final String url = "http://image.tmdb.org/t/p/w185"+relativePath;
        Picasso picasso = Picasso.with(context);
        picasso.setIndicatorsEnabled(true);
        picasso.load(url).networkPolicy(NetworkPolicy.OFFLINE).into(img, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(url).placeholder(R.drawable.poster_placeholder).into(img);
            }
        });
    }


    public static String getIDFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    public static Boolean isNetworkAvailable(Context c){
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @SuppressWarnings("ResourceType")
    public static @MovieSyncAdapter.LocationStatus int getLocationStatus(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(context.getString(R.string.pref_movie_status_key),MovieSyncAdapter.MOVIE_STATUS_UNKNOWN);
    }
}
