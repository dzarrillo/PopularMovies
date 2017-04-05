package com.example.testing.popularmovies.adapters;

/**
 * Created by bharatmukkala on 29-03-2017.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.testing.popularmovies.R;
import com.example.testing.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by bharatmukkala on 19-02-2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private ArrayList<Movie> movies;
    private Context mContext;
    private OnItemClickListener mClickListener;
    private OnFavButtonClickListener mFavListener;
    final String IMAGE_SIZE = "w500";

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView mImageView;
        public ImageButton mImageButton;


        public MoviesViewHolder(View itemView) {

            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.movie_poster);
            mImageButton = (AppCompatImageButton) itemView.findViewById(R.id.fav_image_button);

            mImageView.setOnClickListener(this);
            mImageButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            if (v.getClass().getName().equalsIgnoreCase("android.support.v7.widget.AppCompatImageButton")) {
                mFavListener.onFavButtonClick(v, position);
            } else {
                mClickListener.onItemClick(v, position);
            }

        }
    }

    public interface OnItemClickListener {

        public void onItemClick(View view, int position);

    }

    public interface OnFavButtonClickListener {

        public void onFavButtonClick(View view, int position);

    }

    public MoviesAdapter(Context context, ArrayList<Movie> movies, OnItemClickListener listener, OnFavButtonClickListener favListener) {

        this.movies = movies;
        mContext = context;
        mClickListener = listener;
        mFavListener = favListener;

    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View moviesView = inflater.inflate(R.layout.movie_item_layout, parent, false);
        return new MoviesViewHolder(moviesView);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {

        String moviePosterPath = movies.get(position).getPosterPath();
        final String BASE_URL = "http://image.tmdb.org/t/p/";
        Uri posterPath = Uri.parse(BASE_URL).buildUpon().appendPath(IMAGE_SIZE).appendEncodedPath(moviePosterPath).build();
        Picasso.with(mContext).load(posterPath.toString()).into(holder.mImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                Log.d("DEBUG","Image loaded successfully" + " " + posterPath.toString());
            }

            @Override
            public void onError() {
                Log.d("DEBUG","Image loaded unsuccessfully");
            }
        });
        Log.d("DEBUG","Binding Image");

        if (movies.get(position).isFavourite() == 1) {
            final ContextThemeWrapper wrapper = new ContextThemeWrapper(mContext, R.style.FavIconPressed);
            final Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.favourite, wrapper.getTheme());
            holder.mImageButton.setImageDrawable(drawable);
            Log.d("DEBUG","Binding Fav");
        }
        else {
            final ContextThemeWrapper wrapper = new ContextThemeWrapper(mContext, R.style.FavIconNotPressed);
            final Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.favourite, wrapper.getTheme());
            holder.mImageButton.setImageDrawable(drawable);
            Log.d("DEBUG","Binding Un Fav");
        }

    }
}