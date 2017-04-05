package com.example.testing.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.testing.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by bharatmukkala on 13-03-2017.
 */

public class MovieDetailFragment extends Fragment {

    public static Fragment newInstance(Movie movie) {

        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("movie",movie);
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_detail,container,false);
        Movie movie = (Movie) getArguments().getParcelable("movie");
        ImageView poster = (ImageView) view.findViewById(R.id.poster_detail);
        final String BASE_URL = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w500";
        Uri posterPath = Uri.parse(BASE_URL).buildUpon().appendPath(IMAGE_SIZE).appendEncodedPath(movie.getPosterPath()).build();
        Picasso.with(getContext()).load(posterPath).into(poster);
        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setText(movie.getTitle());
        TextView dateView = (TextView) view.findViewById(R.id.release_date);
        dateView.setText(movie.getReleaseDate());
        TextView voteView = (TextView) view.findViewById(R.id.vote_count);
        voteView.setText(Integer.toString(movie.getVoteCount()));
        TextView ratingText = (TextView) view.findViewById(R.id.rating_text_view);
        ratingText.setText(Double.toString(movie.getVoteAverage()));
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.vote_average);
        ratingBar.setNumStars(10);
        ratingBar.setStepSize(0.1f);
        ratingBar.setRating((float)movie.getVoteAverage());
        return view;

    }


}
