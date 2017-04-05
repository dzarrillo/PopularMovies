package com.example.testing.popularmovies.db;

/**
 * Created by bharatmukkala on 01-02-2017.
 */

public class MoviesContract {

    public static final class MovieEntry {

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_TITLE = "original_title";
        public static final String COLUMN_POSTERPATH = "poster_path";
        public static final String COLUMN_SYNOPSIS = "overview";
        public static final String COLUMN_RELEASEDATE = "release_date";
        public static final String COLUMN_VOTECOUNT = "vote_count";
        public static final String COLUMN_VOTEAVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_FAVOURITE = "favourite";
        public static final String COLUMN_BACKDROPPATH = "backdrop_path";
    }
}
