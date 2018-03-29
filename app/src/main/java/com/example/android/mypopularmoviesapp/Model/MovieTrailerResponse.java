package com.example.android.mypopularmoviesapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tonyukuk on 9.03.2018.
 */

public class MovieTrailerResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<MovieTrailer> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieTrailer> getResults() {
        return results;
    }

    public void setResults(List<MovieTrailer> results) {
        this.results = results;
    }
}
