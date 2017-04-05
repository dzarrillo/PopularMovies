package com.example.testing.popularmovies.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.testing.popularmovies.R;
import com.example.testing.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by bharatmukkala on 20-02-2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerHolder> {

    private ArrayList<Trailer> youTubeLinks;
    private Context mContext;
    private OnClickListener mListener;

    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mListener.onClick(v,getAdapterPosition());
        }

        public ImageView imageView;

        public TrailerHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.trailer_image_view);
            imageView.setOnClickListener(this);
        }
    }

    public TrailersAdapter(Context context, ArrayList<Trailer> links, OnClickListener onClickListener) {
        mContext = context;
        youTubeLinks = links;
        mListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(View view, int position);
    }
    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View trailerView = inflater.inflate(R.layout.trailer_item_layout, parent, false);
        return new TrailerHolder(trailerView);
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        String id = youTubeLinks.get(position).getKey();
        Uri url = Uri.parse("https://img.youtube.com/vi/").buildUpon().appendPath(id).appendPath("hqdefault.jpg").build();
        Picasso.with(mContext).load(url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return youTubeLinks.size();
    }
}
