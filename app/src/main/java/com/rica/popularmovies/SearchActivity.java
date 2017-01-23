package com.rica.popularmovies;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.rica.popularmovies.adapters.RecyclerViewAdapter;
import com.rica.popularmovies.fragments.DetailActivityFragment;

import static com.rica.popularmovies.data.MovieContract.MovieEntry;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, RecyclerViewAdapter.ItemClickListener {

    private final int LOADER_ID = 1;
    private Uri mUri;
    private String query;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter rvAdapter;
    private boolean loaderInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        handleIntent(getIntent());
        mUri = MovieEntry.CONTENT_URI;

        rvAdapter = new RecyclerViewAdapter(this,this);
        recyclerView = (RecyclerView) findViewById(R.id.search_recyclerview);
        RecyclerView.LayoutManager rvLayout = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(rvLayout);
        recyclerView.setAdapter(rvAdapter);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("handle intent"," onNewIntent executed"+intent.getAction());
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            if(loaderInitialized){
                getLoaderManager().restartLoader(LOADER_ID, null, this);
            }else {
                getLoaderManager().initLoader(LOADER_ID, null, this);
                loaderInitialized = true;
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mUri != null){
            String[] selectionArgs = {"%"+query+"%"};
            String selection = MovieEntry.TITLE + " LIKE ? ";
            return new CursorLoader(this,mUri,DetailActivityFragment.MOVIE_COLUMNS,selection,selectionArgs,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        rvAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        rvAdapter.swapCursor(null);
    }

    @Override
    public void itemClicked(View v, int position) {

    }
}
