package com.example.testing.popularmovies.network;


import com.example.testing.popularmovies.model.Movie;
import com.example.testing.popularmovies.model.Review;
import com.example.testing.popularmovies.model.Trailer;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by bharatmukkala on 05-03-2017.
 */

public interface TMDBClient {


    @GET("{sort_order}")
    Observable<Movie.Response> getMovies(@Path("sort_order") String sortOrder);

    @GET("{id}/videos") Observable<Trailer.Response> getTrailers(
            @Path("id") String movieId);

    @GET("{id}/reviews") Observable<Review.Response> getReviews(
            @Path("id") String id,
            @Query("page") int page);

}
