package com.rica.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Rica on 12/2/2016.
 */

public class VideosAdapter extends RecyclerView.Adapter{

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView videoTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            videoTitle = (TextView) itemView;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
