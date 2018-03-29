package com.example.android.mypopularmoviesapp.Rest;

import com.example.android.mypopularmoviesapp.BuildConfig;

/**
 * Created by Tonyukuk on 24.02.2018.
 */

public class ApiManager {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String BASE_URL_IMAGE_POSTER = "http://image.tmdb.org/t/p/w300";
    public static final String BASE_URL_IMAGE_BACKDROP = "http://image.tmdb.org/t/p/w342";
    private static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;

    public static String getApiKey() {
        return API_KEY;
    }
}
