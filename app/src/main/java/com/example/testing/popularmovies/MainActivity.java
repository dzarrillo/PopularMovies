package com.example.testing.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.testing.popularmovies.adapters.MoviesAdapter;
import com.example.testing.popularmovies.model.Movie;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

import static com.example.testing.popularmovies.R.array.menu_array;

public class MainActivity extends AppCompatActivity {

    /* Data for the Recycler View Adapter */
    private ArrayList<Movie> movies = new ArrayList<>();

    /* UI Elements in Main Activity */
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> mRecyclerViewAdapter;

    private Spinner mSpinner;
    private ArrayAdapter<CharSequence> mSpinnerAdapter;

    /* Get Data */
    private DataManager dataManager;

    /* Observable Subscriptions */
    private CompositeDisposable mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        /* Initialize action tool bar */
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSpinner = (Spinner) findViewById(R.id.spinner_nav);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        /* Initialize DataManager */
        dataManager = new DataManager(getApplication());

        /* Initialize  RecyclerView*/
        initializeRecycleView(savedInstanceState);

        /* Initialize Spinner */
        initializeSpinner();


    }

    public void initializeDetailedFragment (Bundle savedInstanceState, Movie movie) {
        /* Initialize Detailed Fragment, if it is in Landscape Mode */
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            if (savedInstanceState == null) {

                if (mRecyclerViewAdapter.getItemCount() != 0)
                {
                    DetailFragment detailFragment = (DetailFragment) DetailFragment.newInstance(movie);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.detail_fragment_container, detailFragment);
                    fragmentTransaction.commit();
                }

            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        dataManager.openDbConnections();
        mSubscriptions = new CompositeDisposable();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // dataManager.closeDbConnections();
        mSubscriptions.clear();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // test for connection
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void initializeRecycleView(Bundle savedInstanceState) {

        /* Initialize recycler view */
        mRecyclerView = (RecyclerView) findViewById(R.id.movie_recycler_view);

        mRecyclerViewAdapter = new MoviesAdapter(
                getApplicationContext(),
                movies,
                (view, position) -> {

                    if (getResources().getConfiguration().orientation
                            == Configuration.ORIENTATION_LANDSCAPE) {

                        // However, if we're being restored from a previous state,
                        // then we don't need to do anything and should return or else
                        // we could end up with overlapping fragments.
                        initializeDetailedFragment(savedInstanceState, movies.get(position));
                    } else {

                        /* Start detail activity */
                        Intent detailActivity = new Intent();
                        detailActivity.putExtra("movie", movies.get(position));
                        detailActivity.setClass(getApplicationContext(), DetailActivity.class);
                        startActivity(detailActivity);

                    }

                    },
                (view, position) -> {

                    ImageButton button = (ImageButton) view;

                    if (movies.get(position).isFavourite() != 1) {

                        final ContextThemeWrapper wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.FavIconPressed);
                        final Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.favourite, wrapper.getTheme());
                        button.setImageDrawable(drawable);
                        movies.get(position).setFavourite(1);
                        dataManager.saveMovieToDB(movies.get(position));

                    } else {

                        final ContextThemeWrapper wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.FavIconNotPressed);
                        final Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.favourite, wrapper.getTheme());
                        button.setImageDrawable(drawable);
                        movies.get(position).setFavourite(0);
                        dataManager.deleteMovieFromDB(movies.get(position));

                        if (mSpinner.getSelectedItem().toString() == getString(R.string.fav)) {
                            movies.remove(position);
                            mRecyclerViewAdapter.notifyItemRemoved(position);
                        }

                    }
                }
        );

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        /* Recycler View layout manager */
        /* TODO: option to view movies in various layouts: list, grid */
        final int columns = getResources().getInteger(R.integer.gallery_columns);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), columns);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void initializeSpinner() {

        mSpinnerAdapter = ArrayAdapter.createFromResource(this, menu_array, android.R.layout.simple_spinner_item);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSpinnerAdapter);

        /* Respond to user selections on Spinner */
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String choice = parent.getItemAtPosition(position).toString();

                if (choice.equals(getString(R.string.popular))) {
                    if (isOnline()) {
                        fillPopularMoviesData();
                    }

                } else if (choice.equals(getString(R.string.top_rated))) {
                    if (isOnline()) {
                        fillTopMoviesData();
                    }

                } else if (choice.equals(getString(R.string.fav))) {
                    fillFavouriteMoviesData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void fillPopularMoviesData() {

        mSubscriptions.add(dataManager.requestPopularMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Movie>() {
                    @Override
                    public void onNext(Movie value) {
                        movies.add(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    protected void onStart() {
                        super.onStart();
                        movies.clear();
                    }

                    @Override
                    public void onComplete() {
                        mRecyclerViewAdapter.notifyDataSetChanged();
                        Log.d("DEBUG", "adapter notified");
                    }
                }));

    }

    public void fillTopMoviesData() {


        mSubscriptions.add(dataManager.requestTopRatedMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Movie>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        movies.clear();
                    }

                    @Override
                    public void onNext(Movie value) {
                        movies.add(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mRecyclerViewAdapter.notifyDataSetChanged();
                        Log.d("DEBUG", "adapter notified");
                    }
                }));

    }

    public void fillFavouriteMoviesData() {

        mSubscriptions.add(dataManager.requestFavouriteMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Movie>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        movies.clear();
                    }

                    @Override
                    public void onNext(Movie value) {
                        movies.add(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mRecyclerViewAdapter.notifyDataSetChanged();
                        Log.d("DEBUG", "adapter notified");
                    }
                }));

    }
}


