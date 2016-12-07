package com.rica.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rica.popularmovies.R;

/**
 * Created by Rica on 12/3/2016.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    static final int REVIEW_ID = 1;
    static final int REVIEW_AUTHOR = 2;
    static final int REVIEW_CONTENT = 3;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthor;
        TextView reviewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = (TextView) itemView.findViewById(R.id.review_author);
            reviewContent = (TextView) itemView.findViewById(R.id.review_content);
        }
    }

    public ReviewsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_layout,parent,false);
        ReviewsAdapter.ViewHolder viewHolder = new ReviewsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mCursor.moveToPosition(position)){
            holder.reviewAuthor.setText(mCursor.getString(REVIEW_AUTHOR));
            holder.reviewContent.setText(mCursor.getString(REVIEW_CONTENT));
        }
    }

    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    public Cursor swapCursor(Cursor newCursor){
        if(mCursor == newCursor){
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if(mCursor != null){
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }
}