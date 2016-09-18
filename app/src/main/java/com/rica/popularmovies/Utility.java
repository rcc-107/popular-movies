package com.rica.popularmovies;

import android.content.Context;
import android.widget.ImageView;

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

    public static long normalizeDate(long dateInMili){
        GregorianCalendar gregorianCalendar = (GregorianCalendar) Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        gregorianCalendar.setTime(new Date(dateInMili));
        gregorianCalendar.add(Calendar.HOUR, 0);
        gregorianCalendar.add(Calendar.MINUTE, 0);
        gregorianCalendar.add(Calendar.SECOND, 0);
        return gregorianCalendar.getTimeInMillis();
    }

    public static void setPoster(final Context context, String relativePath, final ImageView img){
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
}
