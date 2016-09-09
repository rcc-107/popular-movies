package com.rica.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Rica on 8/21/2016.
 */
public class MovieSyncService extends Service {

    private static final Object syncAdapterLock = new Object();
    private static MovieSyncAdapter mMovieSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (syncAdapterLock){
            if(mMovieSyncAdapter == null){
                mMovieSyncAdapter = new MovieSyncAdapter(this,true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMovieSyncAdapter.getSyncAdapterBinder();
    }
}
