package com.rica.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Rica on 12/2/2016.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder>{
				private Context mContext;
				private Cursor mCursor;
				
				static final int VIDEO_TITLE = 1;
    	static final int VIDEO_PATH = 2;
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView videoTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            videoTitle = (TextView) itemView;
        }
    }
    
    public VideosAdapter(Context context) {
    				mContext = context;
    }

    @Override
    public VideosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = Inflater.from(mContext).inflate(R.layout.video_layout,parent,false);
        VideoAdapter.ViewHolder viewholder = new VideoAdapter.ViewHolder(view);
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