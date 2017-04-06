package com.example.testing.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testing.popularmovies.model.Movie;

/**
 * Created by bharatmukkala on 06-04-2017.
 */

public class DetailFragment extends Fragment {

    FragmentPagerAdapter adapterViewPager;
    private FragmentActivity mContext;

    @Override
    public void onAttach(Context context) {
        mContext=(FragmentActivity) context;
        super.onAttach(context);
    }

    public static Fragment newInstance(Movie movie) {

        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("movie",movie);
        fragment.setArguments(args);
        return fragment;

    }

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment,container,false);
        Movie movie = (Movie) getArguments().getParcelable("movie");

        ViewPager vpPager = (ViewPager) view.findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(mContext.getSupportFragmentManager(),movie);
        vpPager.setAdapter(adapterViewPager);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        return view;
    }
}
