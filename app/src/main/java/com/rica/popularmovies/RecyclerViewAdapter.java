package com.rica.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Rica on 9/2/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout root;
        ImageView imageView;
        TextView textView;
        TextView textView2;
        public ViewHolder(View itemView) {
            super(itemView);
            root = (LinearLayout) itemView;
            imageView = (ImageView) root.findViewById(R.id.imageView);
            textView = (TextView) root.findViewById(R.id.text);
            textView2 = (TextView) root.findViewById(R.id.text2);
        }
    }

    RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list,parent,false);
        RecyclerViewAdapter.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(cursor.moveToPosition(position)){
            Utility.setPoster(context,cursor.getString(MainActivityFragment.POSTER_PATH),holder.imageView);
            holder.textView.setText(cursor.getString(MainActivityFragment.VOTE_AVERAGE));
            holder.textView2.setText(cursor.getString(MainActivityFragment.POPULARITY));
        }
    }

    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    public Cursor swapCursor(Cursor newCursor){
        if(cursor == newCursor){
            return null;
        }
        Cursor oldCursor = cursor;
        cursor = newCursor;
        if(cursor != null){
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }
}
