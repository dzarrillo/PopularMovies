package com.example.testing.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testing.popularmovies.adapters.ReviewsAdapter;
import com.example.testing.popularmovies.model.Review;
import com.example.testing.popularmovies.network.TMDBService;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bharatmukkala on 14-03-2017.
 */

public class ReviewDetailFragment extends Fragment {

    private ArrayList<Review> reviews = new ArrayList<>();

    public static Fragment newInstance(String movie_id) {

        ReviewDetailFragment fragment = new ReviewDetailFragment();
        Bundle args = new Bundle();
        args.putString("movie_id",movie_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.review_detail,container,false);
        String movie_id = getArguments().getString("movie_id");
        RecyclerView reviewRecyclerView = (RecyclerView) view.findViewById(R.id.review_recycler_view);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ReviewsAdapter adapter = new ReviewsAdapter(getContext(),reviews);
        reviewRecyclerView.setAdapter(adapter);

        TMDBService.getReviews(movie_id,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Review>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Review value) {
                                reviews.add(value);
                                Log.d("DEBUG", value.getAuthor());
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                adapter.notifyDataSetChanged();
                            }
                        }
                );
        return view;
    }
}
