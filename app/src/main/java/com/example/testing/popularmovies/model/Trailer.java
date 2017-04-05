package com.example.testing.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bharatmukkala on 05-03-2017.
 */

public class Trailer {

    @Expose (deserialize = false)
    String id;

    @Expose (deserialize = false)
    String iso_3166_1;

    @Expose (deserialize = false)
    String iso_639_1;

    @Expose (deserialize = false)
    String name;

    @Expose (deserialize = false)
    int size;

    String type;

    String site;

    String key;

    public void setId(String id) {
        this.id = id;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }

    public static final class Response {

        @Expose
        int id;

        @Expose
        @SerializedName("results")
        public List<Trailer> trailers;

    }
}
