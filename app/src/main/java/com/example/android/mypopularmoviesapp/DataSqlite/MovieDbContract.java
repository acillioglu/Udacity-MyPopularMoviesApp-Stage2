package com.example.android.mypopularmoviesapp.DataSqlite;

import android.provider.BaseColumns;

/**
 * Created by Tonyukuk on 14.03.2018.
 */

public class MovieDbContract implements BaseColumns {

    public static final String TABLE_NAME = "movie_favourite";

    public static final String COLUMB_MOVIE_ID = "movie_id";
    public static final String COLUMB_TITLE = "movie_title";
    public static final String COLUMB_POSTER_IMAGE = "movie_poster_image";
    public static final String COLUMB_BACKDROP_IMAGE = "movie_backdrop_image";
    public static final String COLUMB_AVERAGE_RATING = "movie_average_rating";
    public static final String COLUMB_OVERVIEW = "movie_overview";
    public static final String COLUMB_RELEASE_DATE = "movie_release_date";





}
