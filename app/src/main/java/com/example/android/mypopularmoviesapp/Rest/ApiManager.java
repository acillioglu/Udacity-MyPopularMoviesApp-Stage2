package com.example.android.mypopularmoviesapp.Rest;

/**
 * Created by Tonyukuk on 24.02.2018.
 */

public class ApiManager {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String BASE_URL_IMAGE_POSTER = "http://image.tmdb.org/t/p/w185";
    private static final String API_KEY = "Write Here MovieDB Api";
    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w185";

    // final url http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg


    public static String getApiKey() {
        return API_KEY;
    }
}
