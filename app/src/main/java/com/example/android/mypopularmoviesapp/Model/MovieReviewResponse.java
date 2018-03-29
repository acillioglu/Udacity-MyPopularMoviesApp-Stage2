package com.example.android.mypopularmoviesapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tonyukuk on 12.03.2018.
 */

public class MovieReviewResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<MovieReview> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieReview> getResults() {
        return results;
    }

    public void setResults(List<MovieReview> results) {
        this.results = results;
    }
}
