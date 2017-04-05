package com.example.testing.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bharatmukkala on 05-03-2017.
 */

public class Review {

    @Expose(deserialize = false)
    String id;

    @Expose
    String author;

    @Expose
    String content;

    @Expose
    String url;

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public static final class Response {

        @Expose
        int id;

        @Expose
        @SerializedName("results")
        public List<Review> reviews;

    }

}
