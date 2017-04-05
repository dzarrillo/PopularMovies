package com.example.testing.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.testing.popularmovies.db.MoviesContract;
import com.example.testing.popularmovies.db.MoviesDbHelper;
import com.example.testing.popularmovies.model.Movie;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

import static com.example.testing.popularmovies.network.TMDBService.getTMDBClient;

/**
 * Created by bharatmukkala on 19-03-2017.
 */

public class DataManager {

    private Context ctx;
    private SQLiteDatabase moviedb;
    private MoviesDbHelper moviesDbHelper;

    public DataManager(Context context) {
        this.ctx = context;
    }

    public void openDbConnections() {
        moviesDbHelper = MoviesDbHelper.getInstance(ctx);
        moviedb = moviesDbHelper.getWritableDatabase();
    }

    public Observable<Movie> requestTopRatedMovies() {

        return getTMDBClient().getMovies("top_rated")
                .flatMap(response -> Observable.just(response.movies))
                .flatMap(Observable::fromIterable)
                .map((movie) -> {
                            if (isMovieinDb(movie)) {
                                movie.setFavourite(1);
                            }
                            return movie;
                        }
                )
                .subscribeOn(Schedulers.io());

    }

    public Observable<Movie> requestPopularMovies() {

        return getTMDBClient().getMovies("popular")
                .flatMap(response -> Observable.just(response.movies))
                .flatMap(Observable::fromIterable)
                .map((movie) -> {
                        if (isMovieinDb(movie)) {
                            movie.setFavourite(1);
                        }
                        return movie;
                    }
                )
                .subscribeOn(Schedulers.io());
    }

    public Observable<Movie> requestFavouriteMovies() {

        MoviesDbHelper dbHelper = MoviesDbHelper.getInstance(ctx);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MoviesContract.MovieEntry.TABLE_NAME, null);
        Log.d("FAV CURSOR: ",DatabaseUtils.dumpCursorToString(cursor));
        return Observable.create(new ObservableOnSubscribe<Movie>() {
            @Override
            public void subscribe(ObservableEmitter<Movie> e) throws Exception {

                if (cursor != null) {
                    try {
                        while (cursor.moveToNext() && !e.isDisposed()) {

                            int backDropPath = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_BACKDROPPATH);
                            int movieId = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID);
                            int title = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE);
                            int releaseDate = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASEDATE);
                            int voteCount = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTECOUNT);
                            int voteAvg = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTEAVERAGE);
                            int posterPath = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTERPATH);

                            Movie movie = new Movie();
                            movie.setBackdropPath(cursor.getString(backDropPath));
                            movie.setId(cursor.getString(movieId));
                            movie.setTitle(cursor.getString(title));
                            movie.setReleaseDate(cursor.getString(releaseDate));
                            movie.setVoteCount(cursor.getInt(voteCount));
                            movie.setVoteAverage(cursor.getDouble(voteAvg));
                            movie.setPosterPath(cursor.getString(posterPath));
                            movie.setFavourite(1);

                            e.onNext(movie);

                            Log.d("Debug","Calling on Next" + " " + movie.getId());
                        }
                    } catch (Exception exception) {
                        e.onError(exception);
                    } finally {
                        cursor.close();
                    }

                }
                if (!e.isDisposed()) {
                    e.onComplete();
                    Log.d("Debug","Calling on Complete");
                }
            }
        }).subscribeOn(Schedulers.computation());
    }

    public void saveMovieToDB(Movie value) {

        if(!isMovieinDb(value)) {

            ContentValues eachMovie = new ContentValues();

            eachMovie.put(MoviesContract.MovieEntry.COLUMN_BACKDROPPATH, value.getBackdropPath());
            eachMovie.put(MoviesContract.MovieEntry.COLUMN_FAVOURITE, false);
            eachMovie.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, value.getId());
            eachMovie.put(MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE, value.getTitle());
            eachMovie.put(MoviesContract.MovieEntry.COLUMN_POSTERPATH, value.getPosterPath());
            eachMovie.put(MoviesContract.MovieEntry.COLUMN_FAVOURITE, 1);
            eachMovie.put(MoviesContract.MovieEntry.COLUMN_SYNOPSIS, value.getOverview());
            eachMovie.put(MoviesContract.MovieEntry.COLUMN_RELEASEDATE, value.getReleaseDate());
            eachMovie.put(MoviesContract.MovieEntry.COLUMN_VOTEAVERAGE, value.getVoteAverage());
            eachMovie.put(MoviesContract.MovieEntry.COLUMN_VOTECOUNT, value.getVoteCount());

            moviedb.insert(MoviesContract.MovieEntry.TABLE_NAME, null, eachMovie);

        }
    }

    public void deleteMovieFromDB(Movie movie) {

        if(isMovieinDb(movie)) {
        /* Go ahead and delete, if movie is present */
            moviedb.delete(MoviesContract.MovieEntry.TABLE_NAME, MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " = " + movie.getId(), null);

        }
    }

    public boolean isMovieinDb(Movie movie)  {

        Cursor cursor = moviedb.rawQuery("select * from " + MoviesContract.MovieEntry.TABLE_NAME + " where " + MoviesContract.MovieEntry.COLUMN_MOVIE_ID +  " = " + movie.getId(), null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        }
        else {
            return false;
        }
    }

    public void closeDbConnections() {
        moviesDbHelper.close();
        moviedb.close();
    }
}
