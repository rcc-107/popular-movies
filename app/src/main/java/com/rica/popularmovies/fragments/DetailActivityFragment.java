package com.rica.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rica.popularmovies.R;
import com.rica.popularmovies.SettingsActivity;
import com.rica.popularmovies.Utility;
import com.rica.popularmovies.adapters.ReviewsAdapter;
import com.rica.popularmovies.adapters.VideosAdapter;
import com.rica.popularmovies.callbacks.DetailsCallback;
import com.rica.popularmovies.callbacks.ReviewsCallback;
import com.rica.popularmovies.callbacks.VideosCallback;
import com.rica.popularmovies.data.MovieContract.MovieEntry;
import com.rica.popularmovies.data.MovieContract.MovieReviews;
import com.rica.popularmovies.data.MovieContract.MovieVideos;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements VideosAdapter.VideoClickListener {

    public static final String MOVIE_URI = "movie_uri";
    private static Context mContext;
    private Uri mUri;
    private String movieID;

    private final int LOAD_DETAILS = 0;
    private final int LOAD_VIDEOS = 1;
    private final int LOAD_REVIEWS = 2;
    private DetailsCallback detailsCallback;
    private ReviewsCallback reviewsCallback;
    private VideosCallback videosCallback;
    private RecyclerView reviewRV;
    private RecyclerView videoRV;
    private static ReviewsAdapter reviewsAdapter;
    private static VideosAdapter videosAdapter;

    private static TextView title;
    private static TextView synopsis;
    private static TextView release;
    private static TextView popularity;
    private static TextView vote;
    private static ImageView poster;

    private final String[] MOVIE_COLUMNS = {
            MovieEntry.TITLE,
            MovieEntry.TABLE_NAME+"."+MovieEntry.MOVIE_ID,
            MovieEntry.SYNOPSIS,
            MovieEntry.POSTER_PATH,
            MovieEntry.POPULARITY,
            MovieEntry.VOTE_AVERAGE,
            MovieEntry.RELEASE_DATE,
            MovieEntry.BACKDROP_PATH,
            MovieEntry.DATE_ADDED,
    };

    private final String[] VIDEO_COLUMNS = {
            MovieVideos._ID,
            MovieVideos.VIDEO_TITLE,
            MovieVideos.VIDEO_PATH
    };

    private final String[] REVIEW_COLUMNS = {
            MovieReviews._ID,
            MovieReviews.REVIEW_ID,
            MovieReviews.REVIEW_AUTHOR,
            MovieReviews.REVIEW_CONTENT
    };

    static final int TITLE = 0;
    static final int MOVIE_ID = 1;
    static final int SYNOPSIS = 2;
    static final int POSTER_PATH = 3;
    static final int POPULARITY = 4;
    static final int VOTE_AVERAGE = 5;
    static final int RELEASE_DATE = 6;
    static final int BACKDROP = 7;
    static final int DATE_ADDED = 8;

    static final int VIDEO_TITLE = 1;
    static final int VIDEO_PATH = 2;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arg = getArguments();
        if(arg != null){
            mUri = arg.getParcelable(MOVIE_URI);
        }
        movieID = Utility.getIDFromUri(mUri);
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        title = (TextView) view.findViewById(R.id.movieTitle);
        synopsis = (TextView) view.findViewById(R.id.movieSynopsis);
        release = (TextView) view.findViewById(R.id.movieRelease);
        popularity = (TextView) view.findViewById(R.id.moviePopularity);
        vote = (TextView) view.findViewById(R.id.movieVote);
        poster = (ImageView) view.findViewById(R.id.moviePoster);

        reviewRV = (RecyclerView) view.findViewById(R.id.review_recyclerview);
        reviewRV.setNestedScrollingEnabled(false);
        videoRV = (RecyclerView) view.findViewById(R.id.video_recyclerview);
        videoRV.setNestedScrollingEnabled(false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        View view = getView();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity aca = (AppCompatActivity) getActivity();
        aca.setSupportActionBar(toolbar);
        aca.setTitle(null);
        aca.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        videosAdapter = new VideosAdapter(mContext,this);
        RecyclerView.LayoutManager videoLayout = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false);
        videoRV.setLayoutManager(videoLayout);
        videoRV.setAdapter(videosAdapter);

        reviewsAdapter = new ReviewsAdapter(mContext);
        RecyclerView.LayoutManager reviewLayout = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        reviewRV.setLayoutManager(reviewLayout);
        reviewRV.setAdapter(reviewsAdapter);

        detailsCallback = new DetailsCallback(mContext,movieID,MOVIE_COLUMNS);
        getLoaderManager().initLoader(LOAD_DETAILS,null,detailsCallback);

		videosCallback = new VideosCallback(mContext,movieID,VIDEO_COLUMNS);
		getLoaderManager().initLoader(LOAD_VIDEOS,null,videosCallback);
							
        reviewsCallback = new ReviewsCallback(mContext,movieID,REVIEW_COLUMNS);
        getLoaderManager().initLoader(LOAD_REVIEWS,null,reviewsCallback);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().supportFinishAfterTransition();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void updateReviewAdapter(Cursor cursor){
        reviewsAdapter.swapCursor(cursor);
    }
    
    public static void updateVideoAdapter(Cursor cursor) {
    				videosAdapter.swapCursor(cursor);
    }

    public static void loadMovieDetailsToUI(Cursor data){
        title.setText(data.getString(TITLE));
        synopsis.setText(data.getString(SYNOPSIS));
        release.setText(data.getString(RELEASE_DATE));
        popularity.setText(data.getString(POPULARITY));
        vote.setText(data.getString(VOTE_AVERAGE));
        Utility.setPoster(mContext,data.getString(BACKDROP),poster);
    }

    @Override
    public void itemClick(View v, int position) {
        Cursor cursor = videosCallback.getCursor();
        if(cursor != null) {
            if (cursor.moveToPosition(position)) {
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=").buildUpon().appendPath(cursor.getString(VIDEO_PATH)).build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);

                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }
            }
        }
    }

}