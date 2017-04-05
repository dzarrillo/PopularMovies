package com.example.testing.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.widget.ImageView;

import com.example.testing.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by bharatmukkala on 13-03-2017.
 */

public class DetailActivity extends FragmentActivity {

    FragmentPagerAdapter adapterViewPager;
    Movie movie;

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;
        private static Movie movie;

        public MyPagerAdapter(FragmentManager fragmentManager, Movie movie) {
            super(fragmentManager);
            this.movie = movie;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return MovieDetailFragment.newInstance(movie);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ReviewDetailFragment.newInstance(movie.getId());
                case 2: // Fragment # 1 - This will show SecondFragment
                    return TrailerDetailFragment.newInstance(movie.getId());
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return "Movie Details";
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return "Reviews";
                case 2: // Fragment # 1 - This will show SecondFragment
                    return "Trailers";
                default:
                    return null;
            }
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

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

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(),movie);
        vpPager.setAdapter(adapterViewPager);


        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);



    }
}
