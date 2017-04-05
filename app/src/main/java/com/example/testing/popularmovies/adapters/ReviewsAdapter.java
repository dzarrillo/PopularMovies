package com.example.testing.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testing.popularmovies.R;
import com.example.testing.popularmovies.model.Review;

import java.util.ArrayList;

/**
 * Created by bharatmukkala on 20-02-2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {

    private ArrayList<Review> reviews;
    private Context mContext;
    // private MoviesAdapter.OnItemClickListener mListener;
    // final String IMAGE_SIZE = "w500";

    public class ReviewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public TextView authorView;

        public ReviewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.review_text_view);
            authorView = (TextView) itemView.findViewById(R.id.author_text_view);

        }
    }

    public ReviewsAdapter(Context mContext, ArrayList<Review> reviews) {
        this.mContext = mContext;
        this.reviews = reviews;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View reviewView = inflater.inflate(R.layout.review_item_layout, parent, false);
        return new ReviewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {

        holder.textView.setText(reviews.get(position).getContent());
        holder.authorView.setText(reviews.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
