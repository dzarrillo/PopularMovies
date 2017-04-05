package com.example.testing.popularmovies;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
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

    /* Get Data */
    private DataManager dataManager;

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
        ImageView poster = (ImageView) view.findViewById(R.id.movie_poster_detail);
        AppCompatImageButton imageButton = (AppCompatImageButton)view.findViewById(R.id.fav_image_button_detail);

        if (movie.isFavourite() == 1) {
            final ContextThemeWrapper wrapper = new ContextThemeWrapper(getContext(), R.style.FavIconPressed);
            final Drawable drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.favourite, wrapper.getTheme());
            imageButton.setImageDrawable(drawable);
            Log.d("DEBUG","Binding Fav");
        }
        else {
            final ContextThemeWrapper wrapper = new ContextThemeWrapper(getContext(), R.style.FavIconNotPressed);
            final Drawable drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.favourite, wrapper.getTheme());
            imageButton.setImageDrawable(drawable);
            Log.d("DEBUG","Binding Un Fav");
        }

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (movie.isFavourite() != 1) {

                    final ContextThemeWrapper wrapper = new ContextThemeWrapper(getContext(), R.style.FavIconPressed);
                    final Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.favourite, wrapper.getTheme());
                    imageButton.setImageDrawable(drawable);
                    movie.setFavourite(1);
                    dataManager.saveMovieToDB(movie);

                } else {

                    final ContextThemeWrapper wrapper = new ContextThemeWrapper(getContext(), R.style.FavIconNotPressed);
                    final Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.favourite, wrapper.getTheme());
                    imageButton.setImageDrawable(drawable);
                    movie.setFavourite(0);
                    dataManager.deleteMovieFromDB(movie);

                }

            }
        });
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Initialize DataManager */
        dataManager = new DataManager(getContext());
        dataManager.openDbConnections();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /* Close Data Manager */
        // dataManager.closeDbConnections();
    }
}
