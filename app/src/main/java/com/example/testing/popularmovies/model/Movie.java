package com.example.testing.popularmovies.model;

/**
 * Created by bharatmukkala on 09-03-2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie implements Parcelable
{

    @Expose
    @SerializedName("poster_path")
    String posterPath;

    @Expose (deserialize = false)
    boolean adult;

    @Expose
    String overview;

    @Expose
    @SerializedName("release_date")
    String releaseDate;

    @Expose (deserialize = false)
    List<Genre> genres;

    @Expose
    String id;

    @Expose (deserialize = false)
    String original_title;

    @Expose (deserialize = false)
    String original_language;

    @Expose
    String title;

    @Expose
    @SerializedName("backdrop_path")
    String backdropPath;

    @Expose (deserialize = false)
    double popularity;

    @Expose
    @SerializedName("vote_count")
    int voteCount;

    @Expose (deserialize = false)
    boolean video;

    @Expose
    @SerializedName("vote_average")
    double voteAverage;

    int favourite;

    public int isFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Movie(Parcel movie) {
        this.posterPath = movie.readString();
        this.title = movie.readString();
        this.voteCount = movie.readInt();
        this.voteAverage = movie.readDouble();
        this.id = movie.readString();
        this.releaseDate = movie.readString();
        this.backdropPath = movie.readString();
        this.favourite = movie.readInt();
    }

    public Movie() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeInt(voteCount);
        dest.writeDouble(voteAverage);
        dest.writeString(id);
        dest.writeString(releaseDate);
        dest.writeString(backdropPath);
        dest.writeInt(favourite);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    class Genre {
        public int id;
    }

    public static class Response {

        @SerializedName("page")
        @Expose(deserialize = false)
        public int page;

        @SerializedName("results")
        @Expose
        public List<Movie> movies = null;

        @SerializedName("total_results")
        @Expose(deserialize = false)
        public int totalResults;

        @SerializedName("total_pages")
        @Expose(deserialize = false)
        public int totalPages;

    }

}