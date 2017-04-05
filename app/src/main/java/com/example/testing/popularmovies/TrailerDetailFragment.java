package com.example.testing.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testing.popularmovies.adapters.TrailersAdapter;
import com.example.testing.popularmovies.model.Trailer;
import com.example.testing.popularmovies.network.TMDBService;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bharatmukkala on 14-03-2017.
 */

public class TrailerDetailFragment extends Fragment {

    private ArrayList<Trailer> trailers = new ArrayList<>();

    public static Fragment newInstance(String movie_id) {
        TrailerDetailFragment fragment = new TrailerDetailFragment();
        Bundle args = new Bundle();
        args.putString("movie_id",movie_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.trailer_detail,container,false);
        String movie_id = getArguments().getString("movie_id");
        RecyclerView trailerRecyclerView = (RecyclerView) view.findViewById(R.id.trailer_recycler_view);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TrailersAdapter adapter = new TrailersAdapter(getContext(), trailers, new TrailersAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailers.get(position).getKey()));
                startActivity(appIntent);
            }
        });
        trailerRecyclerView.setAdapter(adapter);

        TMDBService.getTrailers(movie_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Trailer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Trailer value) {
                                trailers.add(value);
                                Log.d("DEBUG", value.getSite());
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
