package com.example.testing.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.testing.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by bharatmukkala on 13-03-2017.
 */

public class DetailActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    Movie movie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        scrollView.setFillViewport(true);

        movie = (Movie) getIntent().getParcelableExtra("movie");
        String movieId = movie.getId();
        String backgroundDrop = movie.getBackdropPath();

        ImageView backgroundPoster = (ImageView) findViewById(R.id.background_poster);
        final String BASE_URL = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w500";
        Uri posterPath = Uri.parse(BASE_URL).buildUpon().appendPath(IMAGE_SIZE).appendEncodedPath(backgroundDrop).build();
        Picasso.with(getApplicationContext()).load(posterPath.toString()).into(backgroundPoster);

        if (savedInstanceState == null) {
            // Restore last state for checked position.
            DetailFragment detailFragment = (DetailFragment) DetailFragment.newInstance(movie);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.detail_fragment_container, detailFragment);
            fragmentTransaction.commit();
        }
    }
}
