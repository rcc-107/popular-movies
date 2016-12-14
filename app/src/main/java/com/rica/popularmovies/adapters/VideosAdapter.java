package com.rica.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.*;
import android.database.*;
import android.view.LayoutInflater;
import com.rica.popularmovies.R;

/**
 * Created by Rica on 12/2/2016.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder>{
    private Context mContext;
    private Cursor mCursor;
    private static VideoClickListener videoClickListener;
				
    static final int VIDEO_TITLE = 1;
    static final int VIDEO_PATH = 2;
    
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView videoTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            videoTitle = (TextView) itemView;
        }

        @Override
        public void onClick(View v) {
            videoClickListener.itemClick(v,this.getAdapterPosition());
        }
    }

    public interface VideoClickListener {
        void itemClick(View v, int position);
    }
    
    public VideosAdapter(Context context, VideoClickListener vcl) {
        mContext = context;
        this.videoClickListener = vcl;
    }

    @Override
    public VideosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_layout,parent,false);
        VideosAdapter.ViewHolder viewholder = new VideosAdapter.ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
							if(mCursor.moveToPosition(position)){
								holder.videoTitle.setText(mCursor.getString(VIDEO_TITLE));
							}
    }

    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0:mCursor.getCount();
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