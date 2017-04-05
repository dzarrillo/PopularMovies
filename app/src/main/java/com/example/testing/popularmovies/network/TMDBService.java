package com.example.testing.popularmovies.network;

import android.util.Log;

import com.example.testing.popularmovies.BuildConfig;
import com.example.testing.popularmovies.model.Movie;
import com.example.testing.popularmovies.model.Review;
import com.example.testing.popularmovies.model.Trailer;

import java.io.IOException;

import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bharatmukkala on 14-03-2017.
 */

public class TMDBService {

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static TMDBClient tmdbClient;

    public static TMDBClient getTMDBClient () {

        if (tmdbClient == null) {
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(
                    new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();

                            HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY).build();
                            request = request.newBuilder().url(url).build();

                            long t1 = System.nanoTime();
                            Log.d("RETROFIT", String.format("Sending request %s on %s%n%s",
                                    request.url(), chain.connection(), request.headers()));

                            Response response = chain.proceed(request);

                            long t2 = System.nanoTime();
                            Log.d("RETROFIT", String.format("Received response for %s in %.1fms%n%s",
                                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

                            return response;
                        }

                    }
            ).build();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();

            tmdbClient = retrofit.create(TMDBClient.class);
            return tmdbClient;
        }
        else {
            return tmdbClient;
        }

    }

    public static Observable<Movie> getMovies (String sortOrder) {

        return getTMDBClient().getMovies(sortOrder)
                .flatMap(response -> Observable.just(response.movies))
                .flatMap(Observable::fromIterable);

    }

    public static Observable<Review> getReviews (String movie_id, int page) {

        return getTMDBClient().getReviews(movie_id, page)
                .flatMap(response -> Observable.just(response.reviews))
                .flatMap(Observable::fromIterable);
    }

    public static Observable<Trailer> getTrailers (String movie_id) {

        return getTMDBClient().getTrailers(movie_id)
                .flatMap(response -> Observable.just(response.trailers))
                .flatMap(Observable::fromIterable);
    }

}
