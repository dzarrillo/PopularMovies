package com.example.testing.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bharatmukkala on 01-02-2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movies.db";
    private static MoviesDbHelper moviesDbHelper;

    private MoviesDbHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

   public static synchronized MoviesDbHelper getInstance(Context context) {
        if (moviesDbHelper == null) {
            moviesDbHelper = new MoviesDbHelper(context);
        }
        return moviesDbHelper;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MoviesContract.MovieEntry.TABLE_NAME + " ( " +
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY ," +
                MoviesContract.MovieEntry.COLUMN_POSTERPATH + " TEXT UNIQUE ," +
                MoviesContract.MovieEntry.COLUMN_BACKDROPPATH + " TEXT UNIQUE ," +
                MoviesContract.MovieEntry.COLUMN_RELEASEDATE + " TEXT ," +
                MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT ," +
                MoviesContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT ," +
                MoviesContract.MovieEntry.COLUMN_FAVOURITE + " INTEGER, " +
                MoviesContract.MovieEntry.COLUMN_VOTEAVERAGE + " REAL, " +
                MoviesContract.MovieEntry.COLUMN_VOTECOUNT + " INTEGER " + " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }
}
