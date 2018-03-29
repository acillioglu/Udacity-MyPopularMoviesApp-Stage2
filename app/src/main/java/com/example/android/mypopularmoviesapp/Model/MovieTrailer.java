package com.example.android.mypopularmoviesapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tonyukuk on 9.03.2018.
 */

public class MovieTrailer {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("key")
    private String key;
    @SerializedName("site")
    private String site;


    public MovieTrailer(String id, String name, String key, String site) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.site = site;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}


